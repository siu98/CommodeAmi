DROP TABLE IF EXISTS `TBL_CUSTOM_TICKET`;
DROP TABLE IF EXISTS `TBL_MOVIE_ACTOR`;
DROP TABLE IF EXISTS `TBL_ACTOR`;
DROP TABLE IF EXISTS `TBL_AVERAGE_SCOPE`;
DROP TABLE IF EXISTS `TBL_REVIEW`;
DROP TABLE IF EXISTS `TBL_SCOPE`;
DROP TABLE IF EXISTS `TBL_MOVIE`;
DROP TABLE IF EXISTS `TBL_USER`;


CREATE TABLE TBL_USER (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    profile TEXT,
    nickname VARCHAR(100),
    birth_date DATE,
    gender VARCHAR(10),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE TBL_MOVIE (
    movie_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(1023) NOT NULL,
    plot TEXT,
    released_at DATE,
    poster_url TEXT,
    genre VARCHAR(255),
    original_title VARCHAR(255),
    original_country VARCHAR(255),
    stills TEXT,
    running_time INT,
    youtube_url TEXT,
    cumulative_audience BIGINT,
    box_office_rank INT,
    api_id BIGINT UNIQUE
) ENGINE=InnoDB;

CREATE TABLE TBL_ACTOR (
                           actor_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(255),
                           gender VARCHAR(50),
                           profile_image TEXT,
                           original_name VARCHAR(255),
                           known_for_department VARCHAR(255)
) ENGINE=InnoDB;

CREATE TABLE TBL_AVERAGE_SCOPE (
    average_scope_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    average_scope DOUBLE,
    number_of_people BIGINT,
    movie_id BIGINT,
    CONSTRAINT fk_average_scope_movie FOREIGN KEY (movie_id) REFERENCES TBL_MOVIE(movie_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE TBL_CUSTOM_TICKET (
    custom_ticket_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_image LONGTEXT,
    hologram_color1 VARCHAR(255),
    hologram_color2 VARCHAR(255),
    comment VARCHAR(500),
    user_id BIGINT,
    CONSTRAINT fk_custom_ticket_user FOREIGN KEY (user_id) REFERENCES TBL_USER(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE TBL_MOVIE_ACTOR (
    movie_actor_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role VARCHAR(255),
    actor_id BIGINT,
    movie_id BIGINT,
    casting_order INT(11),
    CONSTRAINT fk_movie_actor_actor FOREIGN KEY (actor_id) REFERENCES TBL_ACTOR(actor_id) ON DELETE CASCADE,
    CONSTRAINT fk_movie_actor_movie FOREIGN KEY (movie_id) REFERENCES TBL_MOVIE(movie_id) ON DELETE CASCADE,
    CONSTRAINT unique_movie_actor UNIQUE (movie_id, actor_id) -- 복합 고유 키
) ENGINE=InnoDB;

CREATE TABLE TBL_SCOPE (
                           scope_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           scope DOUBLE,
                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                           watched_at DATE,
                           movie_id BIGINT,
                           user_id BIGINT,
                           review_id BIGINT,
                           CONSTRAINT fk_scope_movie FOREIGN KEY (movie_id) REFERENCES TBL_MOVIE(movie_id) ON DELETE CASCADE,
                           CONSTRAINT fk_scope_user FOREIGN KEY (user_id) REFERENCES TBL_USER(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE TBL_REVIEW (
                            review_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                            review TEXT,
                            movie_id BIGINT,
                            user_id BIGINT,
                            scope_id BIGINT,
                            CONSTRAINT fk_review_movie FOREIGN KEY (movie_id) REFERENCES TBL_MOVIE(movie_id) ON DELETE CASCADE,
                            CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES TBL_USER(user_id) ON DELETE CASCADE,
                            CONSTRAINT fk_review_scope FOREIGN KEY (scope_id) REFERENCES  TBL_SCOPE(scope_id) ON DELETE CASCADE
) ENGINE=InnoDB;

