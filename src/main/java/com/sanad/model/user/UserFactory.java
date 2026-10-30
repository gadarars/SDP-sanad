package com.sanad.model.user;

import java.util.HashMap;
import java.util.Map;

/**
 * FACTORY METHOD PATTERN (Creator).
 * buildUser() is the same for every type; each subclass decides which User class createUser() makes.
 */
public abstract class UserFactory {

    /**
     * {@code details} holds the type-specific fields by column name; columns of other types are ignored.
     */
    public User buildUser(int id, String username, String email, Map<String, String> details) {
        Map<String, String> fields = new HashMap<>();
        for (String column : UserType.allFields()) {
            String value = details.get(column);
            fields.put(column, value == null ? "" : value.strip());
        }
        return createUser(id, username.strip(), email == null ? null : email.strip(), fields);
    }

    /**
     * The factory method: {@code details} has every type-specific column, never null.
     */
    protected abstract User createUser(int id, String username, String email, Map<String, String> details);
}
