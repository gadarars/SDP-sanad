-- Clear dates that are not a valid yyyy-MM-dd.
UPDATE IGNORE events
SET date = NULL
WHERE NOT (DATE_FORMAT(STR_TO_DATE(date, '%Y-%m-%d'), '%Y-%m-%d') <=> date);

ALTER TABLE events
    MODIFY date DATE;
