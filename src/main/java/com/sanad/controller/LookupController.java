package com.sanad.controller;

import com.sanad.model.lookup.Lookup;
import com.sanad.model.lookup.LookupRepository;

import java.util.List;

/**
 * CONTROLLER: sits between the view and the model. Put validation / business rules here.
 */
public class LookupController {

    private final LookupRepository repo = new LookupRepository();

    public List<Lookup> getAll() {
        return repo.findAll();
    }

    public void add(String kind, String name) {
        check(kind, name);
        repo.add(new Lookup(0, kind.trim(), name.trim()));
    }

    public void update(int id, String kind, String name) {
        check(kind, name);
        repo.update(new Lookup(id, kind.trim(), name.trim()));
    }

    public void delete(int id) {
        repo.delete(id);
    }

    private void check(String kind, String name) {
        if (kind == null || kind.isBlank()) throw new IllegalArgumentException("Kind (e.g. EVENT_CATEGORY) is required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Value is required");
    }
}
