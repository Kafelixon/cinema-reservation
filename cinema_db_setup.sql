DROP TABLE IF EXISTS cinema_halls CASCADE;

CREATE TABLE cinema_halls (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50),
  capacity INT
);

INSERT INTO cinema_halls (name, capacity) VALUES ('Hall 1', 100);
INSERT INTO cinema_halls (name, capacity) VALUES ('Hall 2', 120);
INSERT INTO cinema_halls (name, capacity) VALUES ('Hall 3', 90);

DROP TABLE IF EXISTS movies CASCADE;

CREATE TABLE movies (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50),
  duration INT
);

INSERT INTO movies (name, duration) VALUES ('Movie 1', 120);
INSERT INTO movies (name, duration) VALUES ('Movie 2', 140);
INSERT INTO movies (name, duration) VALUES ('Movie 3', 130);

DROP TABLE IF EXISTS showtimes CASCADE;

CREATE TABLE showtimes (
  id SERIAL PRIMARY KEY,
  hall_id INT REFERENCES cinema_halls(id),
  movie_id INT REFERENCES movies(id),
  show_time TIMESTAMP
);

INSERT INTO showtimes (hall_id, movie_id, show_time) VALUES (1, 1, '2023-09-10 15:00:00');
INSERT INTO showtimes (hall_id, movie_id, show_time) VALUES (2, 2, '2023-09-10 18:00:00');
INSERT INTO showtimes (hall_id, movie_id, show_time) VALUES (3, 3, '2023-09-10 20:00:00');

DROP TABLE IF EXISTS reservations CASCADE;

CREATE TABLE reservations (
  id SERIAL PRIMARY KEY,
  showtime_id INT REFERENCES showtimes(id),
  seat_number INT
);

INSERT INTO reservations (showtime_id, seat_number) VALUES (1, 1);
INSERT INTO reservations (showtime_id, seat_number) VALUES (1, 2);
INSERT INTO reservations (showtime_id, seat_number) VALUES (2, 3);
