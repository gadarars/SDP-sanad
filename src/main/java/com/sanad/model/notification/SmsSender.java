package com.sanad.model.notification;

import com.sanad.db.DatabaseConnection;
import com.sanad.model.user.User;

/**
 * MODEL: sends a notification by SMS. Phase 1 has no real SMS service, so the message is only recorded.
 */
public class SmsSender implements NotificationSender {
    @Override
    public void send(User to, String message) {
        if (to.getPhone() == null || to.getPhone().isBlank()) throw new IllegalArgumentException(to.getUsername() + " has no phone number");
        DatabaseConnection.getInstance().getJdbi().onDemand(NotificationRepository.class)
                .add(new Notification(0, to.getUsername(), "SMS", to.getPhone(), message, null));
    }
}
