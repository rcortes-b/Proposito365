-- What will i seek for in the database?
--	- Get groups by user id
--	- Get resolutions by user id
--	- Well i just realized that this database desing is overkill :)
--
USE propositos_db;

CREATE TABLE users (
    `id` int AUTO_INCREMENT PRIMARY KEY,
	`username` VARCHAR(50) UNIQUE NOT NULL,
    `email` VARCHAR(255) UNIQUE NOT NULL,
    `password` VARCHAR(60) NOT NULL, -- This is for bcrypt
	`is_verified` BOOLEAN,
	`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (`id`, `email`, `username`, `password`) VALUES 
(1, 'raulcortes6@gmail.com', 'Raul', '$2a$10$LI6Say13zFZaURamVvvkzeUZG8QXwLgQ5c4KAAxthTYOXpVXPv77W'), 
(2, 'csraulcb@gmail.com', 'Raul2', '$2a$10$LI6Say13zFZaURamVvvkzeUZG8QXwLgQ5c4KAAxthTYOXpVXPv77W'), 
(3, 'raulcortes.dev@gmail.com', 'Raul3', '$2a$10$LI6Say13zFZaURamVvvkzeUZG8QXwLgQ5c4KAAxthTYOXpVXPv77W');  

CREATE TABLE email_verification (
	`id` int AUTO_INCREMENT PRIMARY KEY,
	`email` VARCHAR(255) UNIQUE NOT NULL,
	`token` VARCHAR(6) NOT NULL,
	`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	`expires_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP + INTERVAL 15 MINUTE)
);

CREATE TABLE groups_table (
	`id` int AUTO_INCREMENT PRIMARY KEY,
	`name` VARCHAR(50) NOT NULL, -- Should this be UNIQUE? Or should we allow two groups with the same name?
	`description` VARCHAR(255), -- A group description must not be more than 255 characters
	`capacity` int NOT NULL CHECK (capacity > 0 AND capacity <= 100), -- A group capacity must be maximum 20 users
	`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO groups_table (`id`, `name`, `description`, `capacity`) VALUES 
(1, 'Chakras', 'Este grupo es la prueba 1', 3), 
(2, 'Mue', 'Este grupo es la prueba 2', 3);

CREATE TABLE resolution_status (
	`id` int AUTO_INCREMENT PRIMARY KEY,
	`value` VARCHAR(20) UNIQUE NOT NULL
);

INSERT INTO resolution_status (`value`) VALUES ('In progress'), ('Completed'), ('Failed');  

CREATE TABLE resolutions (
	`id` int AUTO_INCREMENT PRIMARY KEY,
	`user_id` int NOT NULL,
	`resolution` VARCHAR(255) NOT NULL,
	`details` VARCHAR(255),
	`status_id` int NOT NULL,
	`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
	FOREIGN KEY (status_id) REFERENCES resolution_status(id)
);

CREATE TABLE roles (
	`id` int AUTO_INCREMENT PRIMARY KEY,
	`role` VARCHAR(20) UNIQUE NOT NULL
);

INSERT INTO roles (`role`) VALUES ('admin'), ('member');

CREATE TABLE user_group (
	`user_id` int NOT NULL,
	`group_id` int NOT NULL,
	`role_id` int NOT NULL,
	`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(`user_id`, `group_id`),
	FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
	FOREIGN KEY (group_id) REFERENCES groups_table(id) ON DELETE CASCADE,
	FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE INDEX idx_resolutions_user_id ON resolutions(user_id);
CREATE INDEX idx_resolutions_status_id ON resolutions(status_id);

CREATE INDEX idx_user_group_group_id ON user_group(group_id);
