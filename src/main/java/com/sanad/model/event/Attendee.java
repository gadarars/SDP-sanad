package com.sanad.model.event;

import com.sanad.model.notification.NotificationSender;
import com.sanad.model.user.User;

import java.time.LocalDate;

/**
 * OBSERVER PATTERN (Concrete Observer): a user registered for an event, told about changes through their sender.
 * STRATEGY PATTERN (Context): sends through whichever NotificationSender it was given.
 */
public class Attendee implements Observer {
    private final User user;
    private final NotificationSender sender;

    public Attendee(Subject event, User user, NotificationSender sender) {
        this.user = user;
        this.sender = sender;
        event.registerObserver(this);
    }

    @Override
    public void update(String name, LocalDate date, String location, boolean cancelled) {
        String when = date == null ? "a date to be announced" : date.toString();
        sender.send(user, cancelled
                ? "Event " + name + " on " + when + " has been cancelled"
                : "Event " + name + " was updated: " + when + (location == null || location.isBlank() ? "" : " at " + location));
    }
}
