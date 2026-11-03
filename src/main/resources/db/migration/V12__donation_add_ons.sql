-- Optional add-ons chosen for each donation.
ALTER TABLE donations
    ADD COLUMN gift_wrapping  BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN thank_you_card BOOLEAN NOT NULL DEFAULT FALSE;
