package com.sanad.controller;

import com.sanad.auth.Session;
import com.sanad.db.DatabaseConnection;
import com.sanad.model.event.Event;
import com.sanad.model.event.Registration;
import com.sanad.model.event.RegistrationRepository;
import com.sanad.model.user.User;
import com.sanad.model.user.UserRepository;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import java.sql.SQLException;
import java.util.List;

/** CONTROLLER: validates input from the view and calls the repository. */
public class RegistrationController {

    private static final int DUPLICATE_KEY = 1062;

    /** Staff see every registration; anyone else sees only their own. */
    public List<Registration> getAll() {
        return repo().findAll().stream().filter(r -> Session.getInstance().canSee(r.getUserId())).toList();
    }

    /** Users who can be registered; anyone but staff can only register themselves. */
    public List<User> getUsers() {
        User me = Session.getInstance().getCurrentUser();
        return me.managesAllRecords() ? userRepo().findAll() : List.of(me);
    }

    public boolean canMarkAttended() { return Session.getInstance().managesAllRecords(); }

    /** Staff see everyone registered for the event; anyone else sees only themselves. */
    public List<String> registeredNames(Event event) {
        if (event == null) return List.of();
        return repo().findByEvent(event.getId()).stream().filter(r -> Session.getInstance().canSee(r.getUserId()))
                .map(Registration::getUsername).toList();
    }

    public void add(User user, Event event) {
        Registration r = validated(0, user, event);
        unique(user, event, () -> repo().add(r));
    }

    public void update(int id, User user, Event event) {
        find(id);
        Registration r = validated(id, user, event);
        unique(user, event, () -> repo().update(r));
    }

    /** Cancels the registration. */
    public void delete(int id) {
        find(id);
        repo().delete(id);
    }

    public void markAttended(Registration r) {
        Session.getInstance().requireManager();
        repo().markAttended(r.getId());
    }

    private Registration find(int id) {
        return getAll().stream().filter(r -> r.getId() == id).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("You can only change your own registrations"));
    }

    private static Registration validated(int id, User user, Event event) {
        if (user == null) throw new IllegalArgumentException("User is required");
        if (event == null) throw new IllegalArgumentException("Event is required");
        Session.getInstance().requireOwner(user.getId());
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

    private static UserRepository userRepo() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(UserRepository.class);
    }
}
