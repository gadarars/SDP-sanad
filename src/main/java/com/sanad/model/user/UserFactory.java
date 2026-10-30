package com.sanad.model.user;

import java.util.List;

/**
 * FACTORY PATTERN.
 * The rest of the program never writes "new AdminUser(...)" or "new DonorUser(...)".
 * It asks the factory: UserFactory.create("DONOR", ...) and gets the right subclass back.
 *
 * Why Factory here: we must CREATE one of several related classes depending on a value
 * (the role) that is only known at runtime (from the form or from the database).
 * If this "which class do I create?" decision lived in many places, adding a new role
 * would mean editing all of them. With the factory it is edited in one place only.
 */
public class UserFactory {

    /** Single list of valid roles - used by the form's drop-down. */
    public static final List<String> ROLES =
            List.of("ADMIN", "STAFF", "DONOR", "VOLUNTEER", "BENEFICIARY");

    private UserFactory() { }   // only static use, no instances needed

    public static User create(int id, String role, String username, String email) {
        if (role == null) throw new IllegalArgumentException("Role is required");
        return switch (role.toUpperCase()) {
            case "ADMIN"       -> new AdminUser(id, username, email);
            case "STAFF"       -> new StaffUser(id, username, email);
            case "DONOR"       -> new DonorUser(id, username, email);
            case "VOLUNTEER"   -> new VolunteerUser(id, username, email);
            case "BENEFICIARY" -> new BeneficiaryUser(id, username, email);
            default -> throw new IllegalArgumentException("Unknown role: " + role);
        };
    }
}
