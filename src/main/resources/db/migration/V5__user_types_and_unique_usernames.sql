-- Fields that only some user types fill in.
ALTER TABLE users
    ADD COLUMN job_title VARCHAR(100),
    ADD COLUMN phone     VARCHAR(30),
    ADD COLUMN skills    VARCHAR(255),
    ADD COLUMN address   VARCHAR(255);

-- Trim usernames, store roles as UserType names, and give duplicate usernames an _<id> suffix, keeping the oldest as is.
UPDATE users
SET username = TRIM(username),
    role     = UPPER(TRIM(role));

UPDATE users u
    JOIN users older ON older.username = u.username AND older.id < u.id
SET u.username = CONCAT(u.username, '_', u.id);

ALTER TABLE users
    ADD CONSTRAINT uq_users_username UNIQUE (username);
