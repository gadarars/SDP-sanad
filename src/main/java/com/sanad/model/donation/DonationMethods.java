package com.sanad.model.donation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * STRATEGY PATTERN: registry of all available donation methods.
 * Every choice in the dropdown has a class behind it.
 */
public final class DonationMethods {

    private static final Map<String, DonationMethod> METHODS = new LinkedHashMap<>();

    static {
        register(new OnlineMethod());
        register(new CheckMethod());
        register(new InKindMethod());
    }

    private DonationMethods() {
    }

    private static void register(DonationMethod m) {
        METHODS.put(m.getName(), m);
    }

    /** All methods in registration order (for the dropdown). */
    public static List<DonationMethod> all() {
        return List.copyOf(METHODS.values());
    }

    /** Looks up a method by its stored name, e.g. "ONLINE". */
    public static DonationMethod byName(String name) {
        if (name == null) {
            return METHODS.values().iterator().next();
        }
        DonationMethod m = METHODS.get(name);
        if (m == null) {
            throw new IllegalArgumentException("Unknown method: " + name);
        }
        return m;
    }
}

