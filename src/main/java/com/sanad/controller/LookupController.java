package com.sanad.controller;

import com.sanad.model.lookup.Lookup;
import com.sanad.model.lookup.LookupRepository;

import java.util.List;
import java.util.Locale;

/**
 * CONTROLLER: sits between the view and the model. Put validation / business rules here.
 */
public class LookupController {

    private final LookupRepository repo = new LookupRepository();

    public List<Lookup> getAll() {
        return repo.findAll();
    }

    /**
     * Values of one kind in the order they were added, e.g. the options for a dropdown.
     */
    public List<String> getNames(String kind) {
        return repo.findByKind(normalizeKind(kind)).stream().map(Lookup::getName).toList();
    }

    public void add(String kind, String name) {
        repo.add(validated(0, kind, name));
    }

    public void update(int id, String kind, String name) {
        repo.update(validated(id, kind, name));
    }

    public void delete(int id) {
        repo.delete(id);
    }

    /**
     * Kind as stored: upper case with underscores, so "event category" becomes EVENT_CATEGORY.
     */
    private static String normalizeKind(String kind) {
        return kind.strip().toUpperCase(Locale.ROOT).replaceAll("[\\s-]+", "_");
    }

    private static Lookup validated(int id, String kind, String name) {
        if (kind == null || kind.isBlank())
            throw new IllegalArgumentException("Kind (e.g. EVENT_CATEGORY) is required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Value is required");
        String k = normalizeKind(kind), n = name.strip();
        if (!k.matches("[A-Z][A-Z0-9_]*"))
            throw new IllegalArgumentException("Kind may only use letters, digits and underscores");
        if (k.length() > 50) throw new IllegalArgumentException("Kind must be at most 50 characters");
        if (n.length() > 255) throw new IllegalArgumentException("Value must be at most 255 characters");
        return new Lookup(id, k, n);
    }
}
