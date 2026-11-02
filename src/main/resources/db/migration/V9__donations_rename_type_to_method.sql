-- Rename the "type" column to "method" for consistency with the Strategy pattern.
ALTER TABLE donations CHANGE COLUMN type method VARCHAR(50) NOT NULL;

