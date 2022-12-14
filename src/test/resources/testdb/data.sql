INSERT INTO seller(name)
VALUES ('애플'),
       ('기프티콘'),
       ('하이닉스'),
       ('삼성');

INSERT INTO PRODUCT(name, price, quantity, status, starts_at, ends_at, seller_id)
VALUES ('맥북프로', 3400000, 100, 'SALE', '2022-08-30 00:00:00', '2022-12-31 23:59:59', 1),
       ('아이패드', 800000, 10, 'SALE', '2022-08-20 00:00:00', '2022-12-31 23:59:59', 1),
       ('에어팟', 320000, 50, 'SALE', '2022-08-30 00:00:00', '2022-12-31 23:59:59', 1),
       ('애플팬슬', 110000, 100, 'SALE', '2022-08-20 00:00:00', '2022-12-31 23:59:59', 1),
       ('문화상품권', 50000, 1000, 'SALE', '2022-08-20 00:00:00', '2022-12-31 23:59:59', 2),
       ('(아마존)Corsai 벤전스LPX DDR4 데스크톱 메모리 키트 16GB (2x8GB) 블랙(CMK16GX4M2B3200C16)', 84270, 30, 'SALE', '2022-08-20 00:00:00', '2022-12-31 23:59:59', 3),
       ('갤럭시S22', 1200000, 30, 'SALE', '2022-08-20 00:00:00', '2022-12-31 23:59:59', 4),
       ('갤럭시 워치 4', 220000, 60, 'SALE', '2022-08-20 00:00:00', '2022-12-31 23:59:59', 4),
       ('갤럭시S10', 1000000, 100, 'SUSPENDED', '2022-08-20 00:00:00', '2022-10-31 23:59:59', 4),
       ('갤럭시 버즈 프로', 330000, 100, 'SALE', '2022-08-20 00:00:00', '2022-10-31 23:59:59', 4),
       ('상품11', 10000, 100, 'SALE', '2022-08-20 00:00:00', '2022-10-31 23:59:59', 1);

INSERT INTO orders(city, street, zip_code, user_id, status, created_at, modified_at)
VALUES ('서울시 송파구', '송파대로 567', '05503', 'greatpeople', 'IN_PROGRESS', '2022-05-20 20:00:00', '2022-05-20 20:00:00'),
       ('서울시 송파구', '송파대로 567', '05503', 'greatpeople', 'IN_PROGRESS', '2022-06-20 20:00:00', '2022-06-20 20:00:00'),
       ('서울시 송파구', '송파대로 567', '05503', 'greatpeople', 'IN_PROGRESS', '2022-07-20 20:00:00', '2022-07-20 20:00:00'),
       ('서울시 송파구', '송파대로 567', '05503', 'greatpeople', 'IN_PROGRESS', '2022-08-20 20:00:00', '2022-08-20 20:00:00'),
       ('서울시 송파구', '송파대로 567', '05503', 'greatpeople', 'CANCELED', '2022-08-20 20:00:00', '2022-08-20 20:00:00'),
       ('서울시 송파구', '송파대로 567', '05503', 'greatpeople', 'COMPLETED', '2022-08-20 20:00:00', '2022-08-20 20:00:00');

INSERT INTO order_product(order_id, product_id, quantity, total_price)
VALUES (1, 1, 1, 3400000),
       (1, 3, 2, 640000),
       (2, 5, 10, 500000),
       (3, 6, 1, 84270),
       (4, 2, 1, 800000),
       (4, 4, 1, 110000),
       (5, 10, 1, 330000),
       (6, 1, 1, 3400000);
