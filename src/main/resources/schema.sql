CREATE TABLE IF NOT EXISTS `films` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `name` varchar(255),
  `description` varchar(255),
  `release_date` DATE,
  `duration` BIGINT,
  `rating_id` BIGINT
);

CREATE TABLE IF NOT EXISTS `rating` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `name` varchar(255)
);

CREATE TABLE IF NOT EXISTS `genre` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `name` varchar(255)
);

CREATE TABLE IF NOT EXISTS `film_genres` (
  `film_id` BIGINT,
  `genre_id` BIGINT,
  PRIMARY KEY (`film_id`, `genre_id`)
);

CREATE TABLE IF NOT EXISTS `likes` (
  `user_id` BIGINT,
  `film_id` BIGINT,
  PRIMARY KEY (`user_id`, `film_id`)
);

CREATE TABLE IF NOT EXISTS `users` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `email` varchar(255),
  `login` varchar(255),
  `name` varchar(255),
  `birthday` DATE
);

CREATE TABLE IF NOT EXISTS `user_friends` (
  `user_id` BIGINT,
  `friend_id` BIGINT,
  PRIMARY KEY (`user_id`, `friend_id`)
);

ALTER TABLE `likes` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `likes` ADD FOREIGN KEY (`film_id`) REFERENCES `films` (`id`);

ALTER TABLE `user_friends` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `user_friends` ADD FOREIGN KEY (`friend_id`) REFERENCES `users` (`id`);

ALTER TABLE `films` ADD FOREIGN KEY (`rating_id`) REFERENCES `rating` (`id`);

ALTER TABLE `film_genres` ADD FOREIGN KEY (`genre_id`) REFERENCES `genre` (`id`);

ALTER TABLE `film_genres` ADD FOREIGN KEY (`film_id`) REFERENCES `films` (`id`);

