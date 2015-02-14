CREATE SEQUENCE uid_seq;

CREATE TABLE rateapp_users (
  user_id    SERIAL PRIMARY KEY DEFAULT nextval('uid_seq'),
  username   VARCHAR(30),
  age        SMALLINT,
  gender     CHAR(1),
  occupation VARCHAR(50),
  zip_code   VARCHAR(6));

ALTER SEQUENCE uid_seq OWNED BY rateapp_users.user_id;

CREATE TABLE rateapp_movies (
  movie_id       SERIAL PRIMARY KEY,
  title          VARCHAR(100) NOT NULL,
  released       TIMESTAMP WITH TIME ZONE,
  video_released TIMESTAMP WITH TIME ZONE,
  url            VARCHAR(200),
  genres         VARCHAR(50));

CREATE TABLE rateapp_ratings (
  user_id   INTEGER NOT NULL,
  movie_id  INTEGER NOT NULL,
  score     SMALLINT,
  timestamp BIGINT,
  PRIMARY KEY(user_id, movie_id));
