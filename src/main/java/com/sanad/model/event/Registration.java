package com.sanad.model.event;

import java.time.LocalDate;

/**
 * MODEL: a user registered for an event.
 */
public class Registration {
    private int id;
    private int eventId;
    private String eventName;
    private LocalDate eventDate;
    private int userId;
    private String username;
    private boolean attended;

    public Registration(int id, int eventId, String eventName, LocalDate eventDate, int userId, String username,
                        boolean attended) {
        this.id = id;
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.userId = userId;
        this.username = username;
        this.attended = attended;
    }

    public int getId() {
        return id;
    }

    public int getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAttended() {
        return attended;
    }
}
