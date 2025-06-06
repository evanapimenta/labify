CREATE TABLE IF NOT EXISTS roles(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO roles(name) VALUES('SYSTEM');
INSERT INTO roles(name) VALUES('SUPER_ADMIN');
INSERT INTO roles(name) VALUES('ADMIN');
INSERT INTO roles(name) VALUES('PATIENT');

INSERT INTO users(id, email, password, role, name, active, verified)
VALUES ('d290f1ee-6c54-4b01-90e6-d701748f0851', 'system@agendaexame.com', '$2y$10$7Tj9iBoEXMsEow23TSwDGOZ82/fA/QmsJ6uULSL68JH8U7YhI7s0q', 'SYSTEM', 'SYSTEM', true, true);