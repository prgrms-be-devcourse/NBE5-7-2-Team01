INSERT INTO places (address, name, total_seats)
VALUES ('서울특별시 서초구 서초동 1307', '강남아트홀', 100),
       ('서울특별시 마포구 와우산로21', '홍대소극장', 100),
       ('서울시 종로구 대학로 8', '대학로극장', 100),
       ('부산광역시 해운대구 중동2', '해운대문화회관', 100);

-- grades 삽입 예시 (place_id 1부터 자동증가 가정)
INSERT INTO grades (place_id, grade, seat_count, default_price, created_at, updated_at)
VALUES (1, 'S', 20, 120000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (1, 'A', 30, 90000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (1, 'B', 30, 60000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (1, 'C', 20, 40000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
-- 다른 place_id도 동일하게 추가

INSERT INTO files (original_file_name, encoded_file_name, created_at, updated_at)
VALUES ('001.png', '001.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('001.png', '001.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('001.png', '001.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('001.png', '001.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- performances 삽입시 file_id, place_id는 실제 존재하는 값을 넣어야 하므로 주의!
INSERT INTO performances (title, description, place_id, file_id, start_time, end_time, category,
                          reservation_start_time, created_at, updated_at)
VALUES ('Champions League Final', 'Europe’s top football competition final match.', 1, 1,
        '2025-06-01 20:00:00', '2025-06-01 22:30:00', 'SPORTS', '2025-05-01 10:00:00',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('BTS World Tour', 'Live concert featuring BTS in Seoul.', 2, 2,
        '2025-07-15 19:00:00', '2025-07-15 22:00:00', 'CONCERT', '2025-06-10 09:00:00',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Avengers: Endgame Screening', 'Special screening of Avengers: Endgame.', 3, 3,
        '2025-05-20 18:00:00', '2025-05-20 21:00:00', 'MOVIE', '2025-05-01 08:00:00',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Phantom of the Opera', 'Classic musical performance at the grand theater.', 1, 4,
        '2025-08-10 18:30:00', '2025-08-10 21:30:00', 'CONCERT', '2025-07-01 10:00:00',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO like_count (performance_id, like_count, created_at, updated_at)
VALUES (1, 1240, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 3421, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 879, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (4, 2567, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
