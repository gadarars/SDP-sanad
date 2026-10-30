package com.sanad.model.user;

import java.util.Map;
import java.util.Set;

/**
 * MODEL: a BENEFICIARY user. Created only through UserFactory.
 */
public class BeneficiaryUser extends User {
    private final String address;

    BeneficiaryUser(int id, String username, String email, String address) {
        super(id, username, email);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public UserType getType() {
        return UserType.BENEFICIARY;
    }

    @Override
    public Set<Feature> getFeatures() {
        return Set.of(Feature.BENEFICIARIES);
    }

    @Override
    public Map<String, String> getDetails() {
        return Map.of("address", address);
    }
}
