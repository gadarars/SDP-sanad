package com.sanad.controller;

import com.sanad.db.DatabaseConnection;
import com.sanad.model.user.*;

import java.util.Map;

/**
 * CONTROLLER: checks the username and password and starts the session.
 */
public class LoginController {

    public void login(String username, String password) {
        UserRepository repo = DatabaseConnection.getInstance().getJdbi().onDemand(UserRepository.class);
        ensureDefaultAdmin(repo);
        String name = username == null ? "" : username.strip();
        User user = repo.findByUsername(name).orElse(null);
        String stored = repo.findPasswordHash(name).orElse(null);
        if (user == null || !PasswordHasher.verify(password == null ? "" : password, stored))
            throw new IllegalArgumentException("Wrong username or password");
        Session.getInstance().login(user);
    }

    public void logout() {
        Session.getInstance().logout();
    }

    /**
     * First run: if nobody has a password yet, makes admin / admin123 an admin account.
     */
    private static void ensureDefaultAdmin(UserRepository repo) {
        if (repo.countWithPassword() > 0) return;
        String hash = PasswordHasher.hash("admin123");
        repo.findByUsername("admin").ifPresentOrElse(
                existing -> repo.update(UserFactory.create(existing.getId(), UserType.ADMIN, "admin", existing.getEmail(), Map.of()), hash),
                () -> repo.add(UserFactory.create(0, UserType.ADMIN, "admin", "admin@sanad.org", Map.of()), hash));
    }
}
