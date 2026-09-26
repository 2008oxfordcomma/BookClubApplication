drop database `BookDB`;

CREATE database `BookDB`;
USE `BookDB`;


CREATE TABLE `User`(
    `user_id` INT AUTO_INCREMENT PRIMARY KEY,
    `username` varchar(50) UNIQUE NOT NULL,
    `first_name` varchar(255),
    `last_name` varchar(255)
);

CREATE TABLE `Moderator`(
    `admin_id` INT PRIMARY KEY,
    FOREIGN KEY(admin_id) REFERENCES User(user_id)
);

CREATE TABLE `Book`(
    `book_id` INT AUTO_INCREMENT PRIMARY KEY,
    `isbn` varchar(1000),
    `title` varchar(255),
    `author_name` varchar(255)
);

CREATE TABLE `Room`(
    `room_id` INT AUTO_INCREMENT PRIMARY KEY,
    `start_time` TIMESTAMP NOT NULL,
    `end_time` TIMESTAMP,
    `book_id` INT NOT NULL,
    FOREIGN KEY(book_id) REFERENCES Book(book_id)
);

CREATE TABLE `Meeting`(
    `room_id` INT NOT NULL,
    `user_id` INT NOT NULL,
    PRIMARY KEY(room_id, user_id),
    FOREIGN KEY(room_id) REFERENCES Room(room_id),
    FOREIGN KEY(user_id) REFERENCES User(user_id)
);

CREATE TABLE `Comment`(
    `comment_id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `text` varchar(2000) NOT NULL,
    `time` TIMESTAMP NOT NULL,
    `room_id` INT NOT NULL,
    FOREIGN KEY(user_id) REFERENCES User(user_id),
    FOREIGN KEY(room_id) REFERENCES Room(room_id)
);

select @@version;
