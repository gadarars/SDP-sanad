-- Users registered for an event, and whether they attended.
CREATE TABLE event_registrations
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT     NOT NULL,
    user_id  INT     NOT NULL,
    attended BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uq_registration_event_user UNIQUE (event_id, user_id),
    FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
