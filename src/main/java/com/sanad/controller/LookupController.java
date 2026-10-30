package com.sanad.controller;

import com.sanad.db.DatabaseConnection;
import com.sanad.model.lookup.Lookup;
import com.sanad.model.lookup.LookupRepository;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Locale;

/**
 * CONTROLLER: sits between the view and the model. Put validation / business rules here.
 */
public class LookupController {

    public List<Lookup> getAll() {
        return repo().findAll();
    }

    /**
     * Values of one kind in the order they were added, e.g. the options for a dropdown.
     */
    public List<String> getNames(String kind) {
        return repo().findByKind(normalizeKind(kind)).stream().map(Lookup::getName).toList();
    }

    public void add(String kind, String name) {
        Lookup x = validated(0, kind, name);
        unique(x, () -> repo().add(x));
    }

    public void update(int id, String kind, String name) {
        Lookup x = validated(id, kind, name);
        unique(x, () -> repo().update(x));
    }

    public void delete(int id) {
        repo().delete(id);
    }

    /**
     * Kind as stored: upper case with underscores, so "event category" becomes EVENT_CATEGORY.
     */
    private static String normalizeKind(String kind) {
        return kind.strip().toUpperCase(Locale.ROOT).replaceAll("[\\s-]+", "_");
    }

    private static Lookup validated(int id, String kind, String name) {
        if (kind == null || kind.isBlank())
            throw new IllegalArgumentException("Kind is required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Value is required");
        String k = normalizeKind(kind);
        String n = name.strip();
        if (!k.matches("[A-Z][A-Z0-9_]*"))
            throw new IllegalArgumentException("Kind may only use letters, digits and underscores");
        if (k.length() > 50) throw new IllegalArgumentException("Kind must be at most 50 characters");
        if (n.length() > 255) throw new IllegalArgumentException("Value must be at most 255 characters");
        return new Lookup(id, k, n);
    }

    /**
     * Runs the write, reporting a kind and value that already exist as a validation error.
     */
    private static void unique(Lookup x, Runnable write) {
        try {
            write.run();
        } catch (UnableToExecuteStatementException e) {
            if (!(e.getCause() instanceof SQLIntegrityConstraintViolationException)) throw e;
            throw new IllegalArgumentException("'" + x.getName() + "' already exists for " + x.getKind());
        }
    }

    private static LookupRepository repo() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(LookupRepository.class);
    }
}
