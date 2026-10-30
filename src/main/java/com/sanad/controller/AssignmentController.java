package com.sanad.controller;

import com.sanad.db.DatabaseConnection;
import com.sanad.model.event.Event;
import com.sanad.model.volunteer.Assignment;
import com.sanad.model.volunteer.AssignmentRepository;
import com.sanad.model.volunteer.Volunteer;
import com.sanad.model.volunteer.VolunteerRepository;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import java.sql.SQLException;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

/** CONTROLLER: validates input from the view and calls the repository. */
public class AssignmentController {

    private static final int DUPLICATE_KEY = 1062;

    public List<Assignment> getAll() { return repo().findAll(); }

    public List<Volunteer> suitableVolunteers(Event event, String skill) {
        if (event == null || event.getDate() == null || skill == null) return List.of();
        return volunteers().findAll().stream()
                .filter(v -> v.hasSkill(skill) && v.isAvailableOn(event.getDate())).toList();
    }

    public void add(Volunteer volunteer, Event event, String skill) {
        Assignment a = validated(0, volunteer, event, skill);
        unique(volunteer, event, () -> repo().add(a));
    }

    public void update(int id, Volunteer volunteer, Event event, String skill) {
        Assignment a = validated(id, volunteer, event, skill);
        Assignment stored = repo().findById(id).orElseThrow(() -> new IllegalArgumentException("Assignment not found"));
        boolean moved = stored.getVolunteerId() != a.getVolunteerId() || stored.getEventId() != a.getEventId();
        if (moved && stored.getHours().signum() > 0)
            throw new IllegalArgumentException("Delete the logged hours before moving this assignment");
        unique(volunteer, event, () -> repo().update(a));
    }

    public void delete(int id) { repo().delete(id); }

    private static Assignment validated(int id, Volunteer volunteer, Event event, String skill) {
        if (volunteer == null) throw new IllegalArgumentException("Volunteer is required");
        if (event == null) throw new IllegalArgumentException("Event is required");
        if (skill == null) throw new IllegalArgumentException("Skill is required");
        if (event.getDate() == null)
            throw new IllegalArgumentException("Give event " + event.getName() + " a date before assigning volunteers");
        Volunteer v = volunteers().findById(volunteer.getId())
                .orElseThrow(() -> new IllegalArgumentException("Volunteer not found"));
        if (!v.hasSkill(skill))
            throw new IllegalArgumentException(v.getName() + " does not have the skill " + skill);
        if (!v.isAvailableOn(event.getDate()))
            throw new IllegalArgumentException(v.getName() + " is not available on "
                    + event.getDate().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + "s");
        return new Assignment(id, v.getId(), v.getName(), event.getId(), event.getName(), event.getDate(), skill, null);
    }

    private static void unique(Volunteer volunteer, Event event, Runnable write) {
        try {
            write.run();
        } catch (UnableToExecuteStatementException e) {
            if (!(e.getCause() instanceof SQLException s && s.getErrorCode() == DUPLICATE_KEY)) throw e;
            throw new IllegalArgumentException(volunteer.getName() + " is already assigned to " + event.getName());
        }
    }

    private static AssignmentRepository repo() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(AssignmentRepository.class);
    }

    private static VolunteerRepository volunteers() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(VolunteerRepository.class);
    }
}
