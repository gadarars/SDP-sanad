package com.sanad.model.user;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * MODEL: an ADMIN user, a staff member who can also manage users. Created only through UserFactory.
 */
public class AdminUser extends User {
    private final String jobTitle;

    AdminUser(int id, String username, String email, String phone, String jobTitle) {
        super(id, username, email, phone);
        this.jobTitle = jobTitle;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    @Override
    public UserType getType() {
        return UserType.ADMIN;
    }

    @Override
    public Set<Feature> getFeatures() {
        return EnumSet.allOf(Feature.class);
    }

    @Override
    public Map<String, String> getDetails() {
        return Map.of("job_title", jobTitle);
    }

    @Override
    public boolean managesAllRecords() {
        return true;
    }
}
