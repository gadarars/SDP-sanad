package com.sanad.model.user;

import java.util.Map;

/**
 * FACTORY METHOD PATTERN (Concrete Creator): makes AdminUser objects.
 */
public class AdminFactory extends UserFactory {
    @Override
    protected User createUser(int id, String username, String email, Map<String, String> details) {
        return new AdminUser(id, username, email, details.get("job_title"));
    }
}
