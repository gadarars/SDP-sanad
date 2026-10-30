package com.sanad.model.volunteer;

import java.math.BigDecimal;

/**
 * MODEL: a volunteer with skills, availability and logged hours.
 */
public class Volunteer {
    private int id;
    private String name;
    private String skills;
    private String availability;
    private BigDecimal hours;

    public Volunteer(int id, String name, String skills, String availability, BigDecimal hours) {
        this.id = id;
        this.name = name;
        this.skills = skills;
        this.availability = availability;
        this.hours = hours;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSkills() {
        return skills;
    }

    public String getAvailability() {
        return availability;
    }

    public BigDecimal getHours() {
        return hours;
    }
}
