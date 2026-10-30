package com.sanad.model.lookup;

/** MODEL: data object for one lookup. */
public class Lookup {
    // kinds of the data-only lists that dropdowns load
    public static final String EVENT_CATEGORY = "EVENT_CATEGORY";
    public static final String NEED_TYPE = "NEED_TYPE";
    public static final String VOLUNTEER_SKILL = "VOLUNTEER_SKILL";

    private int id;
    private String kind;
    private String name;

    public Lookup(int id, String kind, String name) {
        this.id = id;
        this.kind = kind;
        this.name = name;
    }

    public int getId() { return id; }
    public String getKind() { return kind; }
    public void setKind(String kind) { this.kind = kind; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
