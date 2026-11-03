package com.sanad.model.user;

import java.util.Map;

/**
 * FACTORY METHOD PATTERN (Concrete Creator): makes BeneficiaryUser objects.
 */
public class BeneficiaryFactory extends UserFactory {
    @Override
    protected User createUser(int id, String username, String email, String phone, Map<String, String> details) {
        return new BeneficiaryUser(id, username, email, phone, details.get("address"));
    }
}
