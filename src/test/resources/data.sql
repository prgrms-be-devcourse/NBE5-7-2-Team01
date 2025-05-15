-- 1. 장소 정보
INSERT INTO places (address, name, total_seats)
VALUES ('서울특별시 서초구 서초동 1307', '강남아트홀', 100),
       ('서울특별시 마포구 와우산로21', '홍대소극장', 100),
       ('서울시 종로구 대학로 8', '대학로극장', 100),
       ('부산광역시 해운대구 중동2', '해운대문화회관', 100);

-- 2. 좌석 등급 정보 (각 장소별로 4개 grade)
-- 강남아트홀 (place_id = 1)
INSERT INTO grades (place_id, grade, seat_count, default_price, created_at, updated_at)
VALUES (1, 'S', 20, 120000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (1, 'A', 30, 90000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (1, 'B', 30, 60000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (1, 'C', 20, 40000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 홍대소극장 (place_id = 2)
INSERT INTO grades (place_id, grade, seat_count, default_price, created_at, updated_at)
VALUES (2, 'S', 20, 110000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 'A', 30, 80000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 'B', 30, 50000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 'C', 20, 30000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 대학로극장 (place_id = 3)
INSERT INTO grades (place_id, grade, seat_count, default_price, created_at, updated_at)
VALUES (3, 'S', 20, 100000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 'A', 30, 75000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 'B', 30, 50000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 'C', 20, 25000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 해운대문화회관 (place_id = 4)
INSERT INTO grades (place_id, grade, seat_count, default_price, created_at, updated_at)
VALUES (4, 'S', 20, 90000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (4, 'A', 30, 70000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (4, 'B', 30, 50000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (4, 'C', 20, 20000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 3. 파일 정보
INSERT INTO files (original_file_name, encoded_file_name, created_at, updated_at)
VALUES ('001.png', 'a1f2-001.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('001.png', 'b3c4-001.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('001.png', 'c5d6-001.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('001.png', 'd7e8-001.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 4. 공연 정보
INSERT INTO performances (title, description, place_id, file_id, start_time, end_time, category,
                          performance_status, reservation_start_time, deleted_flag,
                          created_at, updated_at)
VALUES ('Champions League Final', 'Europe’s top football competition final match.', 1, 1,
        '2025-06-01 20:00:00', '2025-06-01 22:30:00', 'SPORTS', TRUE, '2025-05-01 10:00:00', FALSE,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

       ('BTS World Tour', 'Live concert featuring BTS in Seoul.', 2, 2,
        '2025-07-15 19:00:00', '2025-07-15 22:00:00', 'CONCERT', TRUE, '2025-06-10 09:00:00', FALSE,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

       ('Avengers: Endgame Screening', 'Special screening of Avengers: Endgame.', 3, 3,
        '2025-05-20 18:00:00', '2025-05-20 21:00:00', 'MOVIE', TRUE, '2025-05-01 08:00:00', FALSE,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

       ('Phantom of the Opera', 'Classic musical performance at the grand theater.', 1, 4,
        '2025-08-10 18:30:00', '2025-08-10 21:30:00', 'CONCERT', TRUE, '2025-07-01 10:00:00', FALSE,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 5. 좋아요 수
INSERT INTO like_count (performance_id, like_count, created_at, updated_at)
VALUES (1, 1240, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 3421, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 879, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (4, 2567, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
