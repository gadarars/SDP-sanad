package com.sanad.model.user;

import java.util.Map;
import java.util.Set;

/**
 * MODEL: a VOLUNTEER user. Created only through UserFactory.
 */
public class VolunteerUser extends User {
    private final String skills;

    VolunteerUser(int id, String username, String email, String skills) {
        super(id, username, email);
        this.skills = skills;
    }

    public String getSkills() {
        return skills;
    }

    @Override
    public UserType getType() {
        return UserType.VOLUNTEER;
    }

    @Override
    public Set<Feature> getFeatures() {
        return Set.of(Feature.VOLUNTEERS, Feature.EVENTS);
    }

    @Override
    public Map<String, String> getDetails() {
        return Map.of("skills", skills);
    }
}
