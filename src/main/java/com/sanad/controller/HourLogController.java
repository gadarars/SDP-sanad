package com.sanad.controller;

import com.sanad.db.DatabaseConnection;
import com.sanad.model.volunteer.Assignment;
import com.sanad.model.volunteer.HourLog;
import com.sanad.model.volunteer.HourLogRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** CONTROLLER: validates input from the view and calls the repository. */
public class HourLogController {

    /** Most hours one log may hold. */
    private static final BigDecimal MAX_HOURS = new BigDecimal("24");

    private final AssignmentController assignments = new AssignmentController();

    /** Staff see every log; a volunteer sees only the hours on their own assignments. */
    public List<HourLog> getAll() {
        Set<Integer> mine = ownAssignmentIds();
        return repo().findAll().stream().filter(h -> mine.contains(h.getAssignmentId())).toList();
    }

    public void add(Assignment assignment, BigDecimal hours) {
        repo().add(validated(0, assignment, hours));
    }

    public void update(int id, Assignment assignment, BigDecimal hours) {
        find(id);
        repo().update(validated(id, assignment, hours));
    }

    public void delete(int id) {
        find(id);
        repo().delete(id);
    }

    private HourLog find(int id) {
        return getAll().stream().filter(h -> h.getId() == id).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("You can only change your own hours"));
    }

    private Set<Integer> ownAssignmentIds() {
        return assignments.getAll().stream().map(Assignment::getId).collect(Collectors.toSet());
    }

    private HourLog validated(int id, Assignment a, BigDecimal hours) {
        if (a == null) throw new IllegalArgumentException("Assignment is required");
        if (!ownAssignmentIds().contains(a.getId())) throw new IllegalArgumentException("You can only log hours on your own assignments");
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
