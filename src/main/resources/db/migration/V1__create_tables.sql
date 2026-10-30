CREATE TABLE donations
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    donor_name VARCHAR(255)   NOT NULL,
    amount     DECIMAL(10, 2) NOT NULL,
    type       VARCHAR(50)    NOT NULL
);

CREATE TABLE volunteers
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    skills       VARCHAR(255),
    availability VARCHAR(255),
    hours        DECIMAL(8, 2) DEFAULT 0
);

CREATE TABLE events
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    date     VARCHAR(20),
    location VARCHAR(255),
    category VARCHAR(100)
);

CREATE TABLE beneficiaries
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    need    VARCHAR(255),
    contact VARCHAR(255)
);

CREATE TABLE lookups
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    kind VARCHAR(50)  NOT NULL,
    name VARCHAR(255) NOT NULL,
    UNIQUE (kind, name)
);

CREATE TABLE users
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email    VARCHAR(255),
    role     VARCHAR(20)  NOT NULL
);
