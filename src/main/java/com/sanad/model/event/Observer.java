package com.sanad.model.event;

import java.time.LocalDate;

/**
 * OBSERVER PATTERN (Observer): is told by its Subject when the subject changes.
 */
public interface Observer {
    void update(String name, LocalDate date, String location, boolean cancelled);
}
