package com.sanad.model.user;

import java.util.Arrays;
import java.util.List;

/**
 * MODEL: every kind of user, with the factory that makes it and the columns only that kind fills in.
 */
public enum UserType {
    ADMIN(new AdminFactory(), "job_title"),
    STAFF(new StaffFactory(), "job_title"),
    DONOR(new DonorFactory(), "phone"),
    VOLUNTEER(new VolunteerFactory(), "skills"),
    BENEFICIARY(new BeneficiaryFactory(), "address");

    private final UserFactory factory;
    private final List<String> fields;

    UserType(UserFactory factory, String... fields) {
        this.factory = factory;
        this.fields = List.of(fields);
    }

    public UserFactory getFactory() {
        return factory;
    }

    public List<String> getFields() {
        return fields;
    }

    /**
     * The type-specific columns of all types, each listed once.
     */
    public static List<String> allFields() {
        return Arrays.stream(values()).flatMap(t -> t.fields.stream()).distinct().toList();
    }
}
