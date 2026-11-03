package com.sanad.controller;

import com.sanad.auth.PasswordHasher;
import com.sanad.auth.Session;
import com.sanad.db.DatabaseConnection;
import com.sanad.model.user.*;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Map;

/**
 * CONTROLLER: validates input from the view and calls the repository.
 */
public class UserController {

    private static final int MIN_PASSWORD = 4;

    /**
     * Admins see every user; anyone else sees only their own account.
     */
    public List<User> getAll() {
        User me = Session.getInstance().getCurrentUser();
        return isAdmin() ? repo().findAll() : List.of(repo().findById(me.getId()).orElse(me));
    }

    public boolean isAdmin() {
        return Session.getInstance().getCurrentUser().getType() == UserType.ADMIN;
    }

    /**
     * {@code details} holds the type-specific fields by column name, e.g. {"skills": "..."}.
     */
    public void add(String username, String email, String phone, UserType type, String password, Map<String, String> details) {
        requireAdmin();
        User user = validated(0, username, email, phone, type, details);
        String hash = hashed(password);
        unique(user, () -> repo().add(user, hash));
    }

    /**
     * Leave the password empty to keep the current one. A non-admin may only edit their own account and not its role.
     */
    public void update(int id, String username, String email, String phone, UserType type, String password, Map<String, String> details) {
        User me = Session.getInstance().getCurrentUser();
        if (!isAdmin() && (id != me.getId() || type != me.getType()))
            throw new IllegalArgumentException("You can only change your own account, not its role");
        User user = validated(id, username, email, phone, type, details);
        String hash = password == null || password.isEmpty() ? null : hashed(password);
        if (type != UserType.ADMIN) keepAnAdmin(id);
        unique(user, () -> repo().update(user, hash));
    }

    public void delete(int id) {
        requireAdmin();
        User me = Session.getInstance().getCurrentUser();
        if (me.getId() == id)
            throw new IllegalArgumentException("You can't delete the account you are logged in as");
        keepAnAdmin(id);
        repo().delete(id);
    }

    private static User validated(int id, String username, String email, String phone, UserType type, Map<String, String> details) {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username is required");
        if (type == null) throw new IllegalArgumentException("Role is required");
        return type.getFactory().buildUser(id, username, email, phone, details);   // FACTORY METHOD
    }

    private void requireAdmin() {
        if (!isAdmin()) throw new IllegalArgumentException("Only an admin can do this");
    }

    private static String hashed(String password) {
        if (password == null || password.length() < MIN_PASSWORD)
            throw new IllegalArgumentException("Password must be at least " + MIN_PASSWORD + " characters");
        return PasswordHasher.hash(password);
    }

    /**
     * Refuses to delete or demote the last admin who can log in.
     */
    private static void keepAnAdmin(int id) {
        boolean isAdmin = repo().findById(id).map(u -> u.getType() == UserType.ADMIN).orElse(false);
        if (isAdmin && repo().countOtherAdmins(id) == 0)
            throw new IllegalArgumentException("At least one admin must remain");
    }

    /**
     * Runs the write, reporting a username that is already taken as a validation error.
     */
    private static void unique(User u, Runnable write) {
        try {
            write.run();
        } catch (UnableToExecuteStatementException e) {
            if (!(e.getCause() instanceof SQLIntegrityConstraintViolationException)) throw e;
            throw new IllegalArgumentException("Username '" + u.getUsername() + "' is already taken");
        }
    }

    private static UserRepository repo() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(UserRepository.class);
    }
}
