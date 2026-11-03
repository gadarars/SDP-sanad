package com.sanad.controller;

import com.sanad.db.DatabaseConnection;
import com.sanad.model.notification.Notification;
import com.sanad.model.notification.NotificationRepository;
import java.util.List;

/** CONTROLLER: gives the view the notifications that were sent. */
public class NotificationController {

    public List<Notification> getAll() { return repo().findAll(); }

    private static NotificationRepository repo() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(NotificationRepository.class);
    }
}
