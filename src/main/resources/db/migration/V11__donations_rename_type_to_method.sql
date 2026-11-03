-- Rename the "type" column to "method" and allow only the codes that have a DonationMethod class.
ALTER TABLE donations
    CHANGE COLUMN type method VARCHAR(50) NOT NULL,
    ADD CONSTRAINT chk_donations_method CHECK (method IN ('ONLINE', 'CHECK', 'IN_KIND'));
