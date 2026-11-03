package com.sanad.model.user;

import java.util.Map;

/**
 * FACTORY METHOD PATTERN (Concrete Creator): makes StaffUser objects.
 */
public class StaffFactory extends UserFactory {
    @Override
    protected User createUser(int id, String username, String email, String phone, Map<String, String> details) {
        return new StaffUser(id, username, email, phone, details.get("job_title"));
    }
}
