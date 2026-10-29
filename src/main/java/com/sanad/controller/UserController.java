package com.sanad.controller;

import com.sanad.model.user.User;
import com.sanad.model.user.UserRepository;
import java.util.List;

/** CONTROLLER: validates input from the view and calls the repository. */
public class UserController {
    private final UserRepository repo = new UserRepository();

    public List<User> getAll() { return repo.findAll(); }

    public void add(String username, String email, String role) {
        check(username);
        repo.add(new User(0, username, email, role));
    }

    public void update(int id, String username, String email, String role) {
        check(username);
        repo.update(new User(id, username, email, role));
    }

    public void delete(int id) { repo.delete(id); }

    private void check(String username) {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username is required");
    }
}
