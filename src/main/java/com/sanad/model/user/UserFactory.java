package com.sanad.model.user;

import java.util.Map;

/**
 * FACTORY PATTERN.
 * Turns a UserType into the matching User subclass; nothing else calls the subclass constructors.
 */
public class UserFactory {

    private UserFactory() {
    }

    /**
     * {@code details} holds the type-specific fields by column name; columns of other types are ignored.
     */
    public static User create(int id, UserType type, String username, String email, Map<String, String> details) {
        if (type == null) throw new IllegalArgumentException("Role is required");
        return switch (type) {
            case ADMIN -> new AdminUser(id, username, email, field(details, "job_title"));
            case STAFF -> new StaffUser(id, username, email, field(details, "job_title"));
            case DONOR -> new DonorUser(id, username, email, field(details, "phone"));
            case VOLUNTEER -> new VolunteerUser(id, username, email, field(details, "skills"));
            case BENEFICIARY -> new BeneficiaryUser(id, username, email, field(details, "address"));
        };
    }

    private static String field(Map<String, String> details, String column) {
        String value = details.get(column);
        return value == null ? "" : value.strip();
    }
}
