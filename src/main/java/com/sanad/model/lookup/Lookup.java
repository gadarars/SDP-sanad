package com.sanad.model.lookup;

/**
 * MODEL: data object for one lookup.
 */
public class Lookup {
    private final int id;
    private final String kind;
    private final String name;

    public Lookup(int id, String kind, String name) {
        this.id = id;
        this.kind = kind;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getKind() {
        return kind;
    }

    public String getName() {
        return name;
    }
}
