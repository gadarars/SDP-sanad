-- Link each donation to a donor user.
ALTER TABLE donations
    ADD COLUMN user_id INT NULL AFTER id,
    ADD CONSTRAINT fk_donations_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL;

-- Best-effort: link existing rows whose donor_name matches a DONOR user's username.
UPDATE donations d
    JOIN users u ON u.username = TRIM(d.donor_name) AND u.role = 'DONOR'
SET d.user_id = u.id;

