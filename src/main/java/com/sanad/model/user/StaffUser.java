package com.sanad.model.user;

import java.util.Map;
import java.util.Set;

import static com.sanad.model.user.Feature.*;

/**
 * MODEL: a STAFF user. Created only through UserFactory.
 */
public class StaffUser extends User {
    private final String jobTitle;

    StaffUser(int id, String username, String email, String jobTitle) {
        super(id, username, email);
        this.jobTitle = jobTitle;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    @Override
    public UserType getType() {
        return UserType.STAFF;
    }

    @Override
    public Set<Feature> getFeatures() {
        return Set.of(DONATIONS, VOLUNTEERS, EVENTS, BENEFICIARIES, LOOKUPS);
    }

    @Override
    public Map<String, String> getDetails() {
        return Map.of("job_title", jobTitle);
    }
}
