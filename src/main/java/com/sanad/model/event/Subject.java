package com.sanad.model.event;

/**
 * OBSERVER PATTERN (Subject): keeps a list of observers and tells them when it changes.
 */
public interface Subject {
    void registerObserver(Observer o);

    void removeObserver(Observer o);

    void notifyObservers();
}
