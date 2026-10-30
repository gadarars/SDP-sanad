package com.sanad.model.user;

import java.util.Arrays;
import java.util.List;

/**
 * MODEL: every kind of user, with the columns only that kind fills in.
 */
public enum UserType {
    ADMIN("job_title"), STAFF("job_title"), DONOR("phone"), VOLUNTEER("skills"), BENEFICIARY("address");

    private final List<String> fields;

    UserType(String... fields) {
        this.fields = List.of(fields);
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
