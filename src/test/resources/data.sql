DELETE FROM seats;
DELETE FROM grades;
DELETE FROM performances;
DELETE FROM places;

-- 데이터 삽입 외래키 제약 조건 위반(Foreign Key Constraint Violation) 때문에 id 명시적 선언
INSERT INTO places (id, address, name, total_seats) VALUES
(1, '서울특별시 서초구 서초동 1307', '강남아트홀', 100),
(2, '서울특별시 마포구 와우산로21', '홍대소극장', 100),
(3, '서울시 종로구 대학로 8', '대학로극장', 100),
(4, '부산광역시 해운대구 중동2', '해운대문화회관', 100),
(5, '대구광역시 수성구 무학로 180', '수성아트센터', 100);

INSERT INTO grades (place_id, grade, seat_count, default_price, created_at, updated_at) VALUES
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
(4, 'C', 20, 38000, NOW(), NOW()),

-- 수성아트센터
(5, 'S', 20, 118000, NOW(), NOW()),
(5, 'A', 30, 89000, NOW(), NOW()),
(5, 'B', 30, 59000, NOW(), NOW()),
(5, 'C', 20, 39000, NOW(), NOW());