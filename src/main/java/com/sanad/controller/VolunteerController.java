package com.sanad.controller;

import com.sanad.model.volunteer.Volunteer;
import com.sanad.model.volunteer.VolunteerRepository;
import java.util.List;

/** CONTROLLER: validates input from the view and calls the repository. */
public class VolunteerController {
    private final VolunteerRepository repo = new VolunteerRepository();

    public List<Volunteer> getAll() { return repo.findAll(); }

    public void add(String name, String skills, String availability, double hours) {
        check(name, hours);
        repo.add(new Volunteer(0, name, skills, availability, hours));
    }

    public void update(int id, String name, String skills, String availability, double hours) {
        check(name, hours);
        repo.update(new Volunteer(id, name, skills, availability, hours));
    }

    public void delete(int id) { repo.delete(id); }

    private void check(String name, double hours) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");
        if (hours < 0) throw new IllegalArgumentException("Hours cannot be negative");
    }
}
