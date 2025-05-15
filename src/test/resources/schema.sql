CREATE TABLE places
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    address     VARCHAR(255) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    total_seats INT          NOT NULL
);

CREATE TABLE grades
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    place_id      BIGINT      NOT NULL,
    grade         VARCHAR(50) NOT NULL,
    seat_count    INT         NOT NULL,
    default_price INT         NOT NULL,
    created_at    TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP   NOT NULL DEFAULT NOW(),
    FOREIGN KEY (place_id) REFERENCES places (id) ON DELETE CASCADE
);

CREATE TABLE files
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    original_file_name VARCHAR(255),
    encoded_file_name  VARCHAR(255),
    created_at         TIMESTAMP,
    updated_at         TIMESTAMP
);

CREATE TABLE performances
(
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    title                  VARCHAR(255) NOT NULL,
    description            TEXT         NOT NULL,
    place_id               BIGINT       NOT NULL,
    file_id                BIGINT       NOT NULL,
    start_time             TIMESTAMP    NOT NULL,
    end_time               TIMESTAMP    NOT NULL,
    category               VARCHAR(20)  NOT NULL,
    performance_status     BOOLEAN      NOT NULL DEFAULT TRUE,
    reservation_start_time TIMESTAMP    NOT NULL,
    created_at             TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at             TIMESTAMP    NOT NULL DEFAULT NOW(),
    deleted_flag           BOOLEAN      NOT NULL DEFAULT FALSE,
    FOREIGN KEY (place_id) REFERENCES places (id) ON DELETE CASCADE,
    FOREIGN KEY (file_id) REFERENCES files (id) ON DELETE CASCADE
);

CREATE TABLE like_count
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    performance_id BIGINT NOT NULL,
    like_count     BIGINT NOT NULL,
    created_at     TIMESTAMP,
    updated_at     TIMESTAMP,
    FOREIGN KEY (performance_id) REFERENCES performances (id) ON DELETE CASCADE
);