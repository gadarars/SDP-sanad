package com.sanad.model.notification;

import java.time.LocalDateTime;

/**
 * MODEL: one sent notification.
 */
public class Notification {
    private int id;
    private String recipient;
    private String channel;
    private String address;
    private String message;
    private LocalDateTime sentAt;

    public Notification(int id, String recipient, String channel, String address, String message, LocalDateTime sentAt) {
        this.id = id;
        this.recipient = recipient;
        this.channel = channel;
        this.address = address;
        this.message = message;
        this.sentAt = sentAt;
    }

    public int getId() {
        return id;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getChannel() {
        return channel;
    }

    public String getAddress() {
        return address;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }
}
