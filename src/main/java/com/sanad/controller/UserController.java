package com.sanad.controller;

import com.sanad.db.DatabaseConnection;
import com.sanad.model.user.PasswordHasher;
import com.sanad.model.user.User;
import com.sanad.model.user.UserFactory;
import com.sanad.model.user.UserRepository;

import java.util.List;

/**
 * CONTROLLER: validates input from the view and calls the repository.
 */
public class UserController {

    private static final int MIN_PASSWORD = 4;

    public List<User> getAll() {
        return repo().findAll();
    }

    public void add(String username, String email, String role, String password) {
        check(username);
        if (password == null || password.length() < MIN_PASSWORD)
            throw new IllegalArgumentException("Password must be at least " + MIN_PASSWORD + " characters");
        if (repo().findByUsername(username.trim()).isPresent())
            throw new IllegalArgumentException("Username '" + username.trim() + "' is already taken");
        repo().add(UserFactory.create(0, role, username.trim(), email), PasswordHasher.hash(password));   // FACTORY
    }

    /**
     * Leave the password empty to keep the current one.
     */
    public void update(int id, String username, String email, String role, String password) {
        check(username);
        repo().findByUsername(username.trim()).ifPresent(other -> {
            if (other.getId() != id)
                throw new IllegalArgumentException("Username '" + username.trim() + "' is already taken");
        });
        User user = UserFactory.create(id, role, username.trim(), email);                                  // FACTORY
        if (password == null || password.isEmpty()) {
            repo().update(user);
        } else {
            if (password.length() < MIN_PASSWORD)
                throw new IllegalArgumentException("Password must be at least " + MIN_PASSWORD + " characters");
            repo().updateWithPassword(user, PasswordHasher.hash(password));
        }
    }

    public void delete(int id) {
        repo().delete(id);
    }

    /**
     * First run: if nobody has a password yet, create the admin so someone can log in.
     */
    public void ensureDefaultAdmin() {
        if (repo().countWithPassword() > 0) return;
        String hash = PasswordHasher.hash("admin123");
        repo().findByUsername("admin").ifPresentOrElse(
                existing -> repo().updateWithPassword(UserFactory.create(existing.getId(), "ADMIN", "admin", existing.getEmail()), hash),
                () -> repo().add(UserFactory.create(0, "ADMIN", "admin", "admin@sanad.org"), hash));
    }

    private void check(String username) {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username is required");
    }

    private static UserRepository repo() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(UserRepository.class);
    }
}
