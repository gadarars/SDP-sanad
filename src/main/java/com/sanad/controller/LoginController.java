package com.sanad.controller;

import com.sanad.db.DatabaseConnection;
import com.sanad.model.user.PasswordHasher;
import com.sanad.model.user.Session;
import com.sanad.model.user.User;
import com.sanad.model.user.UserRepository;

/**
 * CONTROLLER: checks the username and password and starts the session.
 */
public class LoginController {

    public User login(String username, String password) {
        String name = username == null ? "" : username.trim();
        UserRepository repo = DatabaseConnection.getInstance().getJdbi().onDemand(UserRepository.class);
        User user = repo.findByUsername(name).orElse(null);
        String stored = repo.findPasswordHash(name).orElse(null);
        if (user == null || !PasswordHasher.verify(password == null ? "" : password, stored))
            throw new IllegalArgumentException("Wrong username or password");
        Session.getInstance().login(user);
        return user;
    }

    public void logout() {
        Session.getInstance().logout();
    }
}
