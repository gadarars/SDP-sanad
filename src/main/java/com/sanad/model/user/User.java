package com.sanad.model.user;

import java.util.List;

/**
 * MODEL: the common parent of every user type.
 * Each role (Admin, Staff, Donor, Volunteer, Beneficiary) is a subclass that
 * answers three questions differently: getRole(), getPermissions() and getAllowedTabs().
 * Objects are created only through UserFactory.
 */
public abstract class User {
    private final int id;
    private final String username;
    private final String email;

    protected User(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    /**
     * The role name stored in the database, e.g. "ADMIN".
     */
    public abstract String getRole();

    /**
     * What this kind of user is allowed to do (shown in the table).
     */
    public abstract String getPermissions();

    /**
     * Which sidebar sections this kind of user may open after login.
     */
    public abstract List<String> getAllowedTabs();
}
