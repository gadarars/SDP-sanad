package com.sanad.model.notification;

import com.sanad.model.user.User;

/**
 * STRATEGY PATTERN (Strategy interface): the common way to send a notification; the channel (email, SMS...) can be
 * swapped without changing the caller.
 */
public interface NotificationSender {
    void send(User to, String message);
}
