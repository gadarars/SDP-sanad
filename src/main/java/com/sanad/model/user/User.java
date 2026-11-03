package com.sanad.model.user;

import java.util.Map;
import java.util.Set;

/**
 * MODEL: the common parent of every user type.
 * Each subclass adds its own fields and decides which features it may open.
 * Objects are created only through UserFactory.
 */
public abstract class User {
    private final int id;
    private final String username;
    private final String email;
    private final String phone;

    protected User(int id, String username, String email, String phone) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public abstract UserType getType();

    /**
     * The role name stored in the database, e.g. "ADMIN".
     */
    public String getRole() {
        return getType().name();
    }

    /**
     * Which features this kind of user may open after login.
     */
    public abstract Set<Feature> getFeatures();

    /**
     * This type's own fields keyed by column name, e.g. {"skills": "TEACHING"}.
     */
    public abstract Map<String, String> getDetails();

    /**
     * True when this user may see and change every record, not only their own.
     */
    public abstract boolean managesAllRecords();
}
