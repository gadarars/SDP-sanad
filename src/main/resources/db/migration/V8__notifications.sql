-- Every email or SMS the app has sent.
CREATE TABLE notifications
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    recipient VARCHAR(100) NOT NULL,
    channel   VARCHAR(10)  NOT NULL,
    address   VARCHAR(255) NOT NULL,
    message   VARCHAR(500) NOT NULL,
    sent_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
