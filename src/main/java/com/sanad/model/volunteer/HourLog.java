package com.sanad.model.volunteer;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * MODEL: hours a volunteer worked on one assignment.
 */
public class HourLog {
    private int id;
    private int assignmentId;
    private String volunteerName;
    private String eventName;
    private LocalDate eventDate;
    private BigDecimal hours;

    public HourLog(int id, int assignmentId, String volunteerName, String eventName, LocalDate eventDate, BigDecimal hours) {
        this.id = id;
        this.assignmentId = assignmentId;
        this.volunteerName = volunteerName;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.hours = hours;
    }

    public int getId() {
        return id;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public String getVolunteerName() {
        return volunteerName;
    }

    public String getEventName() {
        return eventName;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public BigDecimal getHours() {
        return hours;
    }
}
