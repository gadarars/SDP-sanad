package com.sanad.model.event;

/**
 * OBSERVER PATTERN (Observer): is told by its Subject when the subject changes.
 */
public interface Observer {
    void update(String message);
}
