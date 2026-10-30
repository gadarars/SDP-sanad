package com.sanad.model.user;

/**
 * SINGLETON PATTERN (second use).
 * Remembers WHO is logged in. One Session for the whole program.
 */
public class Session {
    private static Session instance;
    private User currentUser;

    private Session() { }

    public static synchronized Session getInstance() {
        if (instance == null) instance = new Session();
        return instance;
    }

    public void login(User user) { this.currentUser = user; }
    public void logout() { this.currentUser = null; }
    public User getCurrentUser() { return currentUser; }
    public boolean isLoggedIn() { return currentUser != null; }
}
