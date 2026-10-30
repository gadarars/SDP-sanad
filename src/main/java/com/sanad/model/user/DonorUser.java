package com.sanad.model.user;

import java.util.Map;
import java.util.Set;

/**
 * MODEL: a DONOR user. Created only through UserFactory.
 */
public class DonorUser extends User {
    private final String phone;

    DonorUser(int id, String username, String email, String phone) {
        super(id, username, email);
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public UserType getType() {
        return UserType.DONOR;
    }

    @Override
    public Set<Feature> getFeatures() {
        return Set.of(Feature.DONATIONS);
    }

    @Override
    public Map<String, String> getDetails() {
        return Map.of("phone", phone);
    }
}
