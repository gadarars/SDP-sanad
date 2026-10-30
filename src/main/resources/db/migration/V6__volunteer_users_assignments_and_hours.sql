-- Link volunteers to users, matching existing rows by username.
ALTER TABLE volunteers
    ADD COLUMN user_id INT NULL AFTER id,
    ADD CONSTRAINT uq_volunteers_user UNIQUE (user_id),
    ADD CONSTRAINT fk_volunteers_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL;

UPDATE volunteers v
    JOIN (SELECT MIN(id) AS id, TRIM(name) AS name FROM volunteers GROUP BY TRIM(name)) first_of_name ON first_of_name.id = v.id
    JOIN users u ON u.username = first_of_name.name AND u.role = 'VOLUNTEER'
SET v.user_id = u.id;

-- Skills become VOLUNTEER_SKILL lookup names.
UPDATE volunteers v
SET v.skills = (SELECT GROUP_CONCAT(l.name ORDER BY l.id)
                FROM lookups l
                WHERE l.kind = 'VOLUNTEER_SKILL'
                  AND REPLACE(REPLACE(v.skills, '_', ' '), '-', ' ') LIKE CONCAT('%', REPLACE(l.name, '_', ' '), '%'));

-- Availability becomes days of the week (weekend = Friday and Saturday; ranges like "Mon-Fri" become NULL).
ALTER TABLE volunteers
    ADD COLUMN available_days SET ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY') AFTER skills;

UPDATE volunteers
SET available_days = IF(
        availability REGEXP '\\b(mon|tue|wed|thu|fri|sat|sun)[a-z]*\\s*(-|to|till|until|through)\\s*(mon|tue|wed|thu|fri|sat|sun)',
        NULL,
        NULLIF(CONCAT_WS(',',
            IF(availability REGEXP '\\b(mon(day)?s?|weekdays?|daily|every ?day|any ?time|flexible)\\b', 'MONDAY', NULL),
            IF(availability REGEXP '\\b(tue(s(day)?)?s?|weekdays?|daily|every ?day|any ?time|flexible)\\b', 'TUESDAY', NULL),
            IF(availability REGEXP '\\b(wed(nesday)?s?|weekdays?|daily|every ?day|any ?time|flexible)\\b', 'WEDNESDAY', NULL),
            IF(availability REGEXP '\\b(thu(r(s(day)?)?)?s?|weekdays?|daily|every ?day|any ?time|flexible)\\b', 'THURSDAY', NULL),
            IF(availability REGEXP '\\b(fri(day)?s?|weekends?|daily|every ?day|any ?time|flexible)\\b', 'FRIDAY', NULL),
            IF(availability REGEXP '\\b(sat(urday)?s?|weekends?|daily|every ?day|any ?time|flexible)\\b', 'SATURDAY', NULL),
            IF(availability REGEXP '\\b(sun(day)?s?|weekdays?|daily|every ?day|any ?time|flexible)\\b', 'SUNDAY', NULL)), ''));

-- Total hours are now summed from volunteer_hours.
ALTER TABLE volunteers
    DROP COLUMN availability,
    DROP COLUMN hours;

CREATE TABLE volunteer_assignments
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    volunteer_id INT          NOT NULL,
    event_id     INT          NOT NULL,
    skill        VARCHAR(255) NOT NULL,
    CONSTRAINT uq_assignment_volunteer_event UNIQUE (volunteer_id, event_id),
    FOREIGN KEY (volunteer_id) REFERENCES volunteers (id) ON DELETE CASCADE,
    FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE
);

CREATE TABLE volunteer_hours
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    assignment_id INT           NOT NULL,
    hours         DECIMAL(4, 2) NOT NULL CHECK (hours > 0),
    FOREIGN KEY (assignment_id) REFERENCES volunteer_assignments (id) ON DELETE CASCADE
);
