INSERT INTO places (address, name, total_seats)
VALUES ('서울특별시 서초구 서초동 1307', '강남아트홀', 100),
       ('서울특별시 마포구 와우산로21', '홍대소극장', 100),
       ('서울시 종로구 대학로 8', '대학로극장', 100),
       ('부산광역시 해운대구 중동2', '해운대문화회관', 100);

INSERT INTO grades (place_id, grade, seat_count, default_price, created_at, updated_at)
VALUES
-- 강남아트홀
(1, 'S', 20, 120000, NOW(), NOW()),
(1, 'A', 30, 90000, NOW(), NOW()),
(1, 'B', 30, 60000, NOW(), NOW()),
(1, 'C', 20, 40000, NOW(), NOW()),

-- 홍대소극장
(2, 'S', 20, 100000, NOW(), NOW()),
(2, 'A', 30, 80000, NOW(), NOW()),
(2, 'B', 30, 50000, NOW(), NOW()),
(2, 'C', 20, 30000, NOW(), NOW()),

-- 대학로극장
(3, 'S', 20, 110000, NOW(), NOW()),
(3, 'A', 30, 85000, NOW(), NOW()),
(3, 'B', 30, 55000, NOW(), NOW()),
(3, 'C', 20, 35000, NOW(), NOW()),

-- 해운대문화회관
(4, 'S', 20, 115000, NOW(), NOW()),
(4, 'A', 30, 88000, NOW(), NOW()),
(4, 'B', 30, 58000, NOW(), NOW()),
(4, 'C', 20, 38000, NOW(), NOW());

INSERT INTO files (original_file_name, encoded_file_name, created_at, updated_at)
VALUES ('001.png', '001.png', NOW(), NOW()),
       ('001.png', '001.png', NOW(), NOW()),
       ('001.png', '001.png', NOW(), NOW()),
       ('001.png', '001.png', NOW(), NOW());

INSERT INTO performances (title, description, place_id, file_id, start_time, end_time, category,
                          reservation_start_time, created_at, updated_at)
VALUES ('Champions League Final', 'Europe’s top football competition final match.', 1, 1,
        '2025-06-01 20:00:00', '2025-06-01 22:30:00', 'SPORTS', '2025-05-01 10:00:00', NOW(),
        NOW()),

       ('BTS World Tour', 'Live concert featuring BTS in Seoul.', 2, 2, '2025-07-15 19:00:00',
        '2025-07-15 22:00:00', 'CONCERT', '2025-06-10 09:00:00', NOW(), NOW()),

       ('Avengers: Endgame Screening', 'Special screening of Avengers: Endgame.', 3, 3,
        '2025-05-20 18:00:00', '2025-05-20 21:00:00', 'MOVIE', '2025-05-01 08:00:00', NOW(),
        NOW()),
       ('Phantom of the Opera', 'Classic musical performance at the grand theater.', 1, 4,
        '2025-08-10 18:30:00', '2025-08-10 21:30:00', 'CONCERT', '2025-07-01 10:00:00', NOW(),
        NOW());

INSERT INTO like_count (performance_id, like_count, created_at, updated_at)
VALUES (1, 1240, NOW(), NOW()),
       (2, 3421, NOW(), NOW()),
       (3, 879, NOW(), NOW()),
       (4, 2567, NOW(), NOW());