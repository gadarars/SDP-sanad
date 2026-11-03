package com.sanad.controller;

import com.sanad.db.DatabaseConnection;
import com.sanad.model.event.Attendee;
import com.sanad.model.event.Event;
import com.sanad.model.event.EventRepository;
import com.sanad.model.event.RegistrationRepository;
import com.sanad.model.notification.EmailSender;
import com.sanad.model.notification.NotificationSender;
import com.sanad.model.notification.SmsSender;
import com.sanad.model.user.User;
import java.time.LocalDate;
import java.util.List;

/** CONTROLLER: validates input from the view and calls the repository. */
public class EventController {

    public List<Event> getAll() { return repo().findAll(); }

    public void add(String name, LocalDate date, String location, String category) {
        check(name);
        repo().add(new Event(0, name, date, location, category));
    }

    public void update(int id, String name, LocalDate date, String location, String category) {
        check(name);
        Event e = new Event(id, name, date, location, category);
        repo().update(e);
        notifyAttendees(e, "Event " + name + " was updated: " + when(e) + (has(location) ? " at " + location : ""));
    }

    /** Cancels the event, telling its attendees before their registrations are removed with it. */
    public void delete(int id) {
        repo().findById(id).ifPresent(e -> notifyAttendees(e, "Event " + e.getName() + " on " + when(e) + " has been cancelled"));
        repo().delete(id);
    }

    private void check(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Event name is required");
    }

    /** OBSERVER: each registered user is attached to the event, then the event notifies them all. */
    private static void notifyAttendees(Event e, String message) {
        for (User u : registrations().findAttendees(e.getId())) {
            NotificationSender sender = has(u.getEmail()) ? new EmailSender() : has(u.getPhone()) ? new SmsSender() : null;
            if (sender != null) e.registerObserver(new Attendee(u, sender));
        }
        e.notifyObservers(message);
    }

    private static boolean has(String value) {
        return value != null && !value.isBlank();
    }

    private static String when(Event e) {
        return e.getDate() == null ? "a date to be announced" : e.getDate().toString();
    }

    private static EventRepository repo() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(EventRepository.class);
    }

    private static RegistrationRepository registrations() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(RegistrationRepository.class);
    }
}
