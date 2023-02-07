CREATE TABLE IF NOT EXISTS genres (
genre_id integer PRIMARY KEY,
name varchar(100)
);

CREATE TABLE IF NOT EXISTS group_films (
    group_films_id integer PRIMARY KEY,
    film_id integer,
    genre_id integer
);

CREATE TABLE IF NOT EXISTS  films (
    film_id integer PRIMARY KEY,
    name varchar(100) NOT NULL,
    description varchar(200) NOT NULL,
    releaseDate date NOT NULL,
    duration integer,
    countLike integer,
    restriction_id integer
    );

CREATE TABLE IF NOT EXISTS mpa (
restriction_id integer PRIMARY KEY,
name varchar(100)
);

CREATE TABLE IF NOT EXISTS users (
user_id integer PRIMARY KEY,
email varchar(100) NOT null,
login varchar(100) NOT null,
name varchar(100),
birthday date NOT null
);

CREATE TABLE IF NOT EXISTS friendship (
user_id integer REFERENCES users (user_id),
friend_id integer,
status boolean
);


CREATE TABLE IF NOT EXISTS usersLike (
like_id integer PRIMARY KEY,
film_id integer REFERENCES films (film_id),
user_id integer REFERENCES users (user_id)
);