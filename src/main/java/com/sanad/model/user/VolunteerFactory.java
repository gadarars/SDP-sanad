package com.sanad.model.user;

import java.util.Map;

/**
 * FACTORY METHOD PATTERN (Concrete Creator): makes VolunteerUser objects.
 */
public class VolunteerFactory extends UserFactory {
    @Override
    protected User createUser(int id, String username, String email, Map<String, String> details) {
        return new VolunteerUser(id, username, email, details.get("skills"));
    }
}
