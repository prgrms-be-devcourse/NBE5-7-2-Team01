CREATE TABLE places
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    address     VARCHAR(255) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    total_seats INT          NOT NULL
);

CREATE TABLE grades
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    place_id      BIGINT      NOT NULL,
    grade         VARCHAR(50) NOT NULL,
    seat_count    INT         NOT NULL,
    default_price INT         NOT NULL,
    created_at    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_grades_places FOREIGN KEY (place_id) REFERENCES places (id) ON DELETE CASCADE
);

CREATE TABLE files
(
    id                 BIGINT PRIMARY KEY AUTO_INCREMENT,
    original_file_name VARCHAR(255),
    encoded_file_name  VARCHAR(255),
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE performances
(
    id                     BIGINT PRIMARY KEY AUTO_INCREMENT,
    title                  VARCHAR(255)   NOT NULL,
    description            VARCHAR(65535) NOT NULL,
    place_id               BIGINT         NOT NULL,
    file_id                BIGINT         NOT NULL,
    start_time             TIMESTAMP      NOT NULL,
    end_time               TIMESTAMP      NOT NULL,
    category               VARCHAR(20)    NOT NULL,
    performance_status     BOOLEAN        NOT NULL DEFAULT TRUE,
    reservation_start_time TIMESTAMP      NOT NULL,
    created_at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_flag           BOOLEAN        NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_performances_places FOREIGN KEY (place_id) REFERENCES places (id) ON DELETE CASCADE,
    CONSTRAINT fk_performances_files FOREIGN KEY (file_id) REFERENCES files (id) ON DELETE CASCADE
);

CREATE TABLE like_count
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    performance_id BIGINT NOT NULL,
    like_count     BIGINT NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_like_count_performances FOREIGN KEY (performance_id) REFERENCES performances (id) ON DELETE CASCADE
);
