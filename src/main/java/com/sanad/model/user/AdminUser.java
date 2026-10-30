package com.sanad.model.user;

import java.util.EnumSet;
import java.util.Set;

/**
 * MODEL: an ADMIN user, a staff member who can also manage users. Created only through UserFactory.
 */
public class AdminUser extends StaffUser {
    AdminUser(int id, String username, String email, String jobTitle) {
        super(id, username, email, jobTitle);
    }

    @Override
    public UserType getType() {
        return UserType.ADMIN;
    }

    @Override
    public Set<Feature> getFeatures() {
        return EnumSet.allOf(Feature.class);
    }
}
