package com.sanad.model.volunteer;

/** MODEL: a volunteer with skills, availability and logged hours. */
public class Volunteer {
    private int id;
    private String name;
    private String skills;
    private String availability;
    private double hours;

    public Volunteer(int id, String name, String skills, String availability, double hours) {
        this.id = id; this.name = name; this.skills = skills;
        this.availability = availability; this.hours = hours;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getSkills() { return skills; }
    public String getAvailability() { return availability; }
    public double getHours() { return hours; }
}
