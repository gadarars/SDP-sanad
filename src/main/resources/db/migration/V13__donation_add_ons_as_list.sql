-- Add-ons become an ordered list of codes, so one can repeat and a new add-on needs no new column.
ALTER TABLE donations
    ADD COLUMN add_ons VARCHAR(255) NOT NULL DEFAULT '';

UPDATE donations
SET add_ons = CONCAT_WS(',', IF(gift_wrapping, 'GIFT_WRAPPING', NULL), IF(thank_you_card, 'THANK_YOU_CARD', NULL));

ALTER TABLE donations
    DROP COLUMN gift_wrapping,
    DROP COLUMN thank_you_card;
