package com.sanad.model.event;

import java.time.LocalDate;

/**
 * MODEL: an event (fundraiser, workshop, outreach...).
 */
public class Event {
    private int id;
    private String name;
    private LocalDate date;
    private String location;
    private String category;

    public Event(int id, String name, LocalDate date, String location, String category) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.location = location;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getCategory() {
        return category;
    }
}
