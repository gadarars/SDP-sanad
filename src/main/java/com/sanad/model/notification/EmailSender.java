package com.sanad.model.notification;

import com.sanad.db.DatabaseConnection;
import com.sanad.model.user.User;

/**
 * MODEL: sends a notification by email. Phase 1 has no real mail service, so the message is only recorded.
 */
public class EmailSender implements NotificationSender {
    @Override
    public void send(User to, String message) {
        if (to.getEmail() == null || to.getEmail().isBlank()) throw new IllegalArgumentException(to.getUsername() + " has no email");
        DatabaseConnection.getInstance().getJdbi().onDemand(NotificationRepository.class)
                .add(new Notification(0, to.getUsername(), "EMAIL", to.getEmail(), message, null));
    }
}
