CREATE TABLE rateapp_users (
  user_id    INTEGER,
  age        SMALLINT,
  gender     CHAR(1),
  occupation VARCHAR(50),
  zip_code   VARCHAR(6));

CREATE TABLE rateapp_movies (
  movie_id       INTEGER,
  title          VARCHAR(100) NOT NULL,
  released       TIMESTAMP WITH TIME ZONE,
  video_released TIMESTAMP WITH TIME ZONE,
  url            VARCHAR(200),
  genres         VARCHAR(50));

CREATE TABLE rateapp_ratings (
  user_id   INTEGER,
  movie_id  INTEGER,
  score     SMALLINT,
  timestamp BIGINT);
