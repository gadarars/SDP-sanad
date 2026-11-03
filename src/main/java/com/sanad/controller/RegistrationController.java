package com.sanad.controller;

import com.sanad.db.DatabaseConnection;
import com.sanad.model.event.Event;
import com.sanad.model.event.Registration;
import com.sanad.model.event.RegistrationRepository;
import com.sanad.model.user.User;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import java.sql.SQLException;
import java.util.List;

/** CONTROLLER: validates input from the view and calls the repository. */
public class RegistrationController {

    private static final int DUPLICATE_KEY = 1062;

    public List<Registration> getAll() { return repo().findAll(); }

    public List<String> registeredNames(Event event) {
        if (event == null) return List.of();
        return repo().findByEvent(event.getId()).stream().map(Registration::getUsername).toList();
    }

    public void add(User user, Event event) {
        Registration r = validated(0, user, event);
        unique(user, event, () -> repo().add(r));
    }

    public void update(int id, User user, Event event) {
        Registration r = validated(id, user, event);
        unique(user, event, () -> repo().update(r));
    }

    /** Cancels the registration. */
    public void delete(int id) { repo().delete(id); }

    public void markAttended(Registration r) { repo().markAttended(r.getId()); }

    private static Registration validated(int id, User user, Event event) {
        if (user == null) throw new IllegalArgumentException("User is required");
        if (event == null) throw new IllegalArgumentException("Event is required");
        return new Registration(id, event.getId(), event.getName(), event.getDate(), user.getId(), user.getUsername(), false);
    }

    private static void unique(User user, Event event, Runnable write) {
        try {
            write.run();
        } catch (UnableToExecuteStatementException e) {
            if (!(e.getCause() instanceof SQLException s && s.getErrorCode() == DUPLICATE_KEY)) throw e;
            throw new IllegalArgumentException(user.getUsername() + " is already registered for " + event.getName());
        }
    }

    private static RegistrationRepository repo() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(RegistrationRepository.class);
    }
}
