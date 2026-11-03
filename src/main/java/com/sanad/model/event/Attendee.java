package com.sanad.model.event;

import com.sanad.model.notification.NotificationSender;
import com.sanad.model.user.User;

/**
 * OBSERVER PATTERN (Concrete Observer): a user registered for an event, told about changes through their sender.
 */
public class Attendee implements Observer {
    private final User user;
    private final NotificationSender sender;

    public Attendee(User user, NotificationSender sender) {
        this.user = user;
        this.sender = sender;
    }

    @Override
    public void update(String message) {
        sender.send(user, message);
    }
}
