package com.sanad.model.user;

import java.util.Map;
import java.util.Set;

/**
 * MODEL: a DONOR user. Created only through UserFactory.
 */
public class DonorUser extends User {

    DonorUser(int id, String username, String email, String phone) {
        super(id, username, email, phone);
    }

    @Override
    public UserType getType() {
        return UserType.DONOR;
    }

    @Override
    public Set<Feature> getFeatures() {
        return Set.of(Feature.DONATIONS, Feature.USERS);
    }

    @Override
    public Map<String, String> getDetails() {
        return Map.of();
    }

    @Override
    public boolean managesAllRecords() {
        return false;
    }
}
