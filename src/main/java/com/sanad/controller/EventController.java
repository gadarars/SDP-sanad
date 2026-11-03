package com.sanad.controller;

import com.sanad.auth.Session;
import com.sanad.db.DatabaseConnection;
import com.sanad.model.event.AssignedVolunteer;
import com.sanad.model.event.Attendee;
import com.sanad.model.event.Event;
import com.sanad.model.event.EventRepository;
import com.sanad.model.event.RegistrationRepository;
import com.sanad.model.notification.EmailSender;
import com.sanad.model.notification.NotificationSender;
import com.sanad.model.notification.SmsSender;
import com.sanad.model.user.User;
import com.sanad.model.volunteer.AssignmentRepository;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** CONTROLLER: validates input from the view and calls the repository. */
public class EventController {

    private static final String CATEGORY_KIND = "EVENT_CATEGORY";

    private final LookupController lookups = new LookupController();

    public List<Event> getAll() { return repo().findAll(); }

    public List<String> getCategories() { return lookups.getNames(CATEGORY_KIND); }

    public boolean canEdit() { return Session.getInstance().managesAllRecords(); }

    public void add(String name, LocalDate date, String location, String category) {
        check(name, category);
        repo().add(new Event(0, name.strip(), date, location, category));
    }

    /** OBSERVER: the event's attendees and assigned volunteers register on it, then changing its details notifies them. */
    public void update(int id, String name, LocalDate date, String location, String category) {
        check(name, category);
        Event e = find(id);
        repo().update(new Event(id, name.strip(), date, location, category));
        attachObservers(e);
        e.setDetails(name.strip(), date, location, category);
    }

    /** Cancels the event, telling its attendees and assigned volunteers. */
    public void delete(int id) {
        Session.getInstance().requireManager();
        Event e = find(id);
        attachObservers(e);
        e.cancel();
        repo().delete(id);
    }

    private void check(String name, String category) {
        Session.getInstance().requireManager();
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Event name is required");
        if (category != null && !getCategories().contains(category))
            throw new IllegalArgumentException("'" + category + "' is not in the " + CATEGORY_KIND + " lookup list");
    }

    private static Event find(int id) {
        return repo().findById(id).orElseThrow(() -> new IllegalArgumentException("Event not found"));
    }

    private static void attachObservers(Event e) {
        Set<Integer> notified = new HashSet<>();
        for (User u : registrations().findAttendees(e.getId())) {
            NotificationSender sender = senderFor(u);
            if (sender != null && notified.add(u.getId())) new Attendee(e, u, sender);
        }
        for (User u : assignments().findAssignedUsers(e.getId())) {
            NotificationSender sender = senderFor(u);
            if (sender != null && notified.add(u.getId())) new AssignedVolunteer(e, u, sender);
        }
    }

    private static NotificationSender senderFor(User u) {
        return has(u.getEmail()) ? new EmailSender() : has(u.getPhone()) ? new SmsSender() : null;
    }

    private static boolean has(String value) {
        return value != null && !value.isBlank();
    }

    private static EventRepository repo() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(EventRepository.class);
    }

    private static RegistrationRepository registrations() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(RegistrationRepository.class);
    }

    private static AssignmentRepository assignments() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(AssignmentRepository.class);
    }
}
