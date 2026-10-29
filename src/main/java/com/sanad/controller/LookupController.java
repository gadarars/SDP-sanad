package com.sanad.controller;

import com.sanad.model.lookup.Lookup;
import com.sanad.model.lookup.LookupRepository;
import java.util.List;

/** CONTROLLER: sits between the view and the model. Put validation / business rules here. */
public class LookupController {

    private final LookupRepository repo = new LookupRepository();

    public List<Lookup> getAll() { return repo.findAll(); }

    public void add(String kind, String name) {
        check(kind);
        repo.add(new Lookup(0, kind, name));
    }

    public void update(int id, String kind, String name) {
        check(kind);
        repo.update(new Lookup(id, kind, name));
    }

    public void delete(int id) { repo.delete(id); }

    private void check(String kind) {
        if (kind == null || kind.isBlank()) throw new IllegalArgumentException("Kind (e.g. DONATION_TYPE) is required");
    }
}
