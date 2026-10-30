package com.sanad.controller;

import com.sanad.db.DatabaseConnection;
import com.sanad.model.volunteer.Assignment;
import com.sanad.model.volunteer.HourLog;
import com.sanad.model.volunteer.HourLogRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** CONTROLLER: validates input from the view and calls the repository. */
public class HourLogController {

    /** Most hours one log may hold. */
    private static final BigDecimal MAX_HOURS = new BigDecimal("24");

    public List<HourLog> getAll() { return repo().findAll(); }

    public void add(Assignment assignment, BigDecimal hours) {
        repo().add(validated(0, assignment, hours));
    }

    public void update(int id, Assignment assignment, BigDecimal hours) {
        repo().update(validated(id, assignment, hours));
    }

    public void delete(int id) { repo().delete(id); }

    private static HourLog validated(int id, Assignment a, BigDecimal hours) {
        if (a == null) throw new IllegalArgumentException("Assignment is required");
        if (a.getEventDate() == null || a.getEventDate().isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Hours can be logged only on or after the event date");
        if (hours.signum() <= 0) throw new IllegalArgumentException("Hours must be positive");
        if (hours.stripTrailingZeros().scale() > 2) throw new IllegalArgumentException("Hours can have at most 2 decimal places");
        if (hours.compareTo(MAX_HOURS) > 0) throw new IllegalArgumentException("Hours must be at most 24 per log");
        return new HourLog(id, a.getId(), a.getVolunteerName(), a.getEventName(), a.getEventDate(), hours);
    }

    private static HourLogRepository repo() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(HourLogRepository.class);
    }
}
