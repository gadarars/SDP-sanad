package com.sanad.model.event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * MODEL: an event (fundraiser, workshop, outreach...).
 * OBSERVER PATTERN (Concrete Subject): its registered attendees are its observers.
 */
public class Event implements Subject {
    private int id;
    private String name;
    private LocalDate date;
    private String location;
    private String category;
    private final List<Observer> observers = new ArrayList<>();

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

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer o : observers) o.update(message);
    }
}
