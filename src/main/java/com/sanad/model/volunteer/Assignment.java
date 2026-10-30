package com.sanad.model.volunteer;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * MODEL: a volunteer assigned to an event.
 */
public class Assignment {
    private int id;
    private int volunteerId;
    private String volunteerName;
    private int eventId;
    private String eventName;
    private LocalDate eventDate;
    private String skill;
    private BigDecimal hours;

    public Assignment(int id, int volunteerId, String volunteerName, int eventId, String eventName, LocalDate eventDate,
                      String skill, BigDecimal hours) {
        this.id = id;
        this.volunteerId = volunteerId;
        this.volunteerName = volunteerName;
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.skill = skill;
        this.hours = hours;
    }

    public int getId() {
        return id;
    }

    public int getVolunteerId() {
        return volunteerId;
    }

    public String getVolunteerName() {
        return volunteerName;
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

    public String getSkill() {
        return skill;
    }

    public BigDecimal getHours() {
        return hours;
    }
}
