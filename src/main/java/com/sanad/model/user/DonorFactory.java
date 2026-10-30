package com.sanad.model.user;

import java.util.Map;

/**
 * FACTORY METHOD PATTERN (Concrete Creator): makes DonorUser objects.
 */
public class DonorFactory extends UserFactory {
    @Override
    protected User createUser(int id, String username, String email, Map<String, String> details) {
        return new DonorUser(id, username, email, details.get("phone"));
    }
}
