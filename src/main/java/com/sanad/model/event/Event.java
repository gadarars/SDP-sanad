package com.sanad.model.event;

/** MODEL: an event (fundraiser, workshop, outreach...). */
public class Event {
    private int id;
    private String name;
    private String date;      // simple text like 2026-12-01
    private String location;
    private String category;

    public Event(int id, String name, String date, String location, String category) {
        this.id = id; this.name = name; this.date = date;
        this.location = location; this.category = category;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDate() { return date; }
    public String getLocation() { return location; }
    public String getCategory() { return category; }
}
