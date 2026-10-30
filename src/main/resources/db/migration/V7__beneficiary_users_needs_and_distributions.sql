-- Link beneficiaries to users.
ALTER TABLE beneficiaries
    ADD COLUMN user_id INT NULL,
    ADD CONSTRAINT uq_beneficiaries_user UNIQUE (user_id),
    ADD CONSTRAINT fk_beneficiaries_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL;

-- Needs become NEED_TYPE lookup values, adding any missing ones to the list.
UPDATE beneficiaries
SET need = NULLIF(TRIM(need), '');

INSERT INTO lookups(kind, name)
SELECT 'NEED_TYPE', MIN(b.need)
FROM beneficiaries b
WHERE b.need IS NOT NULL
  AND NOT EXISTS (SELECT 1
                  FROM lookups l
                  WHERE l.kind = 'NEED_TYPE'
                    AND REPLACE(l.name, '_', ' ') = REPLACE(b.need, '_', ' '))
GROUP BY REPLACE(b.need, '_', ' ');

UPDATE beneficiaries b
    JOIN lookups l ON l.kind = 'NEED_TYPE' AND REPLACE(l.name, '_', ' ') = REPLACE(b.need, '_', ' ')
SET b.need = l.name;

-- Each resource or service given to a beneficiary.
CREATE TABLE distributions
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    beneficiary_id INT            NOT NULL,
    item           VARCHAR(255)   NOT NULL,
    quantity       DECIMAL(10, 2) NOT NULL,
    given_on       DATE           NOT NULL,
    CONSTRAINT fk_distributions_beneficiary FOREIGN KEY (beneficiary_id) REFERENCES beneficiaries (id) ON DELETE CASCADE
);
