package com.sanad.auth;

import com.sanad.model.user.User;

/**
 * SINGLETON PATTERN (second use).
 * Remembers WHO is logged in. One Session for the whole program.
 */
public class Session {
    private static Session uniqueInstance;
    private User currentUser;

    private Session() { }

    public static synchronized Session getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Session();
        }
        return uniqueInstance;
    }

    public void login(User user) { this.currentUser = user; }
    public void logout() { this.currentUser = null; }
    public User getCurrentUser() { return currentUser; }

    /**
     * True when the logged-in user may see and change every record, not only their own.
     */
    public boolean managesAllRecords() { return currentUser.managesAllRecords(); }

    /**
     * Refuses the action unless the logged-in user may change every record.
     */
    public void requireManager() {
        if (!managesAllRecords()) throw new IllegalArgumentException("Only staff can do this");
    }

    /**
     * True when {@code ownerId} is the logged-in user or they may see every record.
     */
    public boolean canSee(Integer ownerId) {
        return managesAllRecords() || (ownerId != null && ownerId == currentUser.getId());
    }

    /**
     * Refuses the action unless {@code ownerId} is the logged-in user or they may change every record.
     */
    public void requireOwner(Integer ownerId) {
        if (!canSee(ownerId)) throw new IllegalArgumentException("You can only change your own records");
    }
}
