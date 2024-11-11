CREATE TABLE users
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    username      VARCHAR(50) UNIQUE  NOT NULL,
    email         VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255)        NOT NULL, -- Passwords should be hashed
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE roles
(
    id     INT PRIMARY KEY AUTO_INCREMENT,
    role_name   VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE user_roles
(
    user_id INT,
    role_id INT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- Insert roles
INSERT INTO roles (role_name, description)
VALUES ('ROOT', 'Super user with full access'),
       ('ADMIN', 'Administrator with full access'),
       ('USER', 'User with limited access');

-- Insert roles
INSERT INTO users (username, email, password_hash, created_at, updated_at)
VALUES ('root', 'root@delodev.com','$2a$10$EvEzfXezPqPERX5WiMWPRuI.Hsi3RUzSmP07zZ2pzi7qPmfDbr7rW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('admin', 'admin@delodev.com', '$2a$10$qjGW3hOcwbylAKtRVnu7AeucH/GyAFeUId9d2CL1paTqbdASGz74S', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Assign roles to admin
INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1), -- User 1 is an root
       (1, 2),
       (1, 3),
       (2, 2),-- User 2 is an admin
       (2, 3);