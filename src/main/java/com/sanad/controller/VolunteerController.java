package com.sanad.controller;

import com.sanad.db.DatabaseConnection;
import com.sanad.model.volunteer.Volunteer;
import com.sanad.model.volunteer.VolunteerRepository;
import java.math.BigDecimal;
import java.util.List;

/** CONTROLLER: validates input from the view and calls the repository. */
public class VolunteerController {

    /** Most hours the DECIMAL(8,2) column holds. */
    private static final BigDecimal MAX_HOURS = new BigDecimal("999999.99");

    public List<Volunteer> getAll() { return repo().findAll(); }

    public void add(String name, String skills, String availability, BigDecimal hours) {
        check(name, hours);
        repo().add(new Volunteer(0, name, skills, availability, hours));
    }

    public void update(int id, String name, String skills, String availability, BigDecimal hours) {
        check(name, hours);
        repo().update(new Volunteer(id, name, skills, availability, hours));
    }

    public void delete(int id) { repo().delete(id); }

    private void check(String name, BigDecimal hours) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");
        if (hours.signum() < 0) throw new IllegalArgumentException("Hours cannot be negative");
        if (hours.stripTrailingZeros().scale() > 2) throw new IllegalArgumentException("Hours can have at most 2 decimal places");
        if (hours.compareTo(MAX_HOURS) > 0) throw new IllegalArgumentException("Hours must be at most 999,999.99");
    }

    private static VolunteerRepository repo() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(VolunteerRepository.class);
    }
}
