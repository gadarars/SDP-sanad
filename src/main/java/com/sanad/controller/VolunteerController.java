package com.sanad.controller;

import com.sanad.db.DatabaseConnection;
import com.sanad.model.user.User;
import com.sanad.model.user.UserRepository;
import com.sanad.model.user.UserType;
import com.sanad.model.volunteer.Assignment;
import com.sanad.model.volunteer.AssignmentRepository;
import com.sanad.model.volunteer.Volunteer;
import com.sanad.model.volunteer.VolunteerRepository;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/** CONTROLLER: validates input from the view and calls the repository. */
public class VolunteerController {

    private static final String SKILL_KIND = "VOLUNTEER_SKILL";
    private static final int DUPLICATE_KEY = 1062;

    private final LookupController lookups = new LookupController();

    public List<Volunteer> getAll() { return repo().findAll(); }

    public List<User> getVolunteerUsers() {
        return users().findAll().stream().filter(u -> u.getType() == UserType.VOLUNTEER).toList();
    }

    public List<String> getSkills() { return lookups.getNames(SKILL_KIND); }

    public void add(String name, User user, List<String> skills, Collection<DayOfWeek> days) {
        Volunteer v = validated(0, name, user, skills, days);
        uniqueUser(user, () -> repo().add(v));
    }

    public void update(int id, String name, User user, List<String> skills, Collection<DayOfWeek> days) {
        Volunteer v = validated(id, name, user, skills, days);
        uniqueUser(user, () -> repo().update(v));
    }

    public void delete(int id) { repo().delete(id); }

    public String certificate(int volunteerId) {
        Volunteer v = repo().findById(volunteerId).orElseThrow(() -> new IllegalArgumentException("Volunteer not found"));
        if (v.getTotalHours().signum() == 0) throw new IllegalArgumentException(v.getName() + " has no logged hours yet");
        StringBuilder text = new StringBuilder()
                .append("CERTIFICATE OF VOLUNTEER SERVICE\n\n")
                .append("Sanad gratefully certifies that\n\n")
                .append("    ").append(v.getName()).append("\n\n")
                .append("has given ").append(plain(v.getTotalHours())).append(" hours of volunteer service:\n\n");
        for (Assignment a : assignments().findByVolunteer(volunteerId)) {
            if (a.getHours().signum() > 0)
                text.append(String.format("    %-10s  %-30s  %6s h%n", Objects.toString(a.getEventDate(), ""), a.getEventName(),
                        plain(a.getHours())));
        }
        return text.append("\nIssued on ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("d MMMM yyyy"))).append('\n')
                .toString();
    }

    private Volunteer validated(int id, String name, User user, List<String> skills, Collection<DayOfWeek> days) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");
        if (name.strip().length() > 255) throw new IllegalArgumentException("Name must be at most 255 characters");
        if (user == null) throw new IllegalArgumentException("Pick the user this volunteer logs in as");
        boolean isVolunteer = users().findById(user.getId()).map(u -> u.getType() == UserType.VOLUNTEER).orElse(false);
        if (!isVolunteer) throw new IllegalArgumentException("User " + user.getUsername() + " is not a volunteer user");
        List<String> known = getSkills();
        for (String skill : skills) {
            if (!known.contains(skill)) throw new IllegalArgumentException("Unknown skill " + skill);
        }
        String skillText = skills.isEmpty() ? null : known.stream().filter(skills::contains).collect(Collectors.joining(","));
        if (skillText != null && skillText.length() > 255) throw new IllegalArgumentException("Too many skills");
        String dayText = days.isEmpty() ? null
                : EnumSet.copyOf(days).stream().map(DayOfWeek::name).collect(Collectors.joining(","));
        return new Volunteer(id, user.getId(), user.getUsername(), name.strip(), skillText, dayText, null);
    }

    private static void uniqueUser(User user, Runnable write) {
        try {
            write.run();
        } catch (UnableToExecuteStatementException e) {
            if (!(e.getCause() instanceof SQLException s && s.getErrorCode() == DUPLICATE_KEY)) throw e;
            throw new IllegalArgumentException("User " + user.getUsername() + " is already linked to another volunteer");
        }
    }

    private static String plain(BigDecimal hours) { return hours.stripTrailingZeros().toPlainString(); }

    private static VolunteerRepository repo() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(VolunteerRepository.class);
    }

    private static AssignmentRepository assignments() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(AssignmentRepository.class);
    }

    private static UserRepository users() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(UserRepository.class);
    }
}
