package com.sanad.controller;

import com.sanad.model.event.Event;
import com.sanad.model.event.EventRepository;
import java.util.List;

/** CONTROLLER: validates input from the view and calls the repository. */
public class EventController {
    private final EventRepository repo = new EventRepository();

    public List<Event> getAll() { return repo.findAll(); }

    public void add(String name, String date, String location, String category) {
        check(name);
        repo.add(new Event(0, name, date, location, category));
    }

    public void update(int id, String name, String date, String location, String category) {
        check(name);
        repo.update(new Event(id, name, date, location, category));
    }

    public void delete(int id) { repo.delete(id); }

    private void check(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Event name is required");
    }
}
