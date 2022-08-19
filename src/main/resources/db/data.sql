INSERT INTO seller(name)
VALUES ('애플'),
       ('기프티콘'),
       ('하이닉스'),
       ('삼성');

INSERT INTO PRODUCT(name, price, quantity, status, starts_at, ends_at, seller_id)
VALUES ('맥북프로', 3400000, 100, 'SALE', '2022-08-20 00:00:00', '2022-12-31 23:59:59', 1),
       ('아이패드', 800000, 10, 'SALE', '2022-08-20 00:00:00', '2022-12-31 23:59:59', 1),
       ('에어팟', 320000, 50, 'SALE', '2022-08-20 00:00:00', '2022-12-31 23:59:59', 1),
       ('애플팬슬', 110000, 100, 'SALE', '2022-08-20 00:00:00', '2022-12-31 23:59:59', 1),
       ('문화상품권', 50000, 1000, 'SALE', '2022-08-20 00:00:00', '2022-12-31 23:59:59', 2),
       ('(아마존)Corsai 벤전스LPX DDR4 데스크톱 메모리 키트 16GB (2x8GB) 블랙(CMK16GX4M2B3200C16)', 84270, 30, 'SALE', '2022-08-20 00:00:00', '2022-12-31 23:59:59', 3),
       ('갤럭시S22', 1200000, 30, 'SALE', '2022-08-20 00:00:00', '2022-12-31 23:59:59', 4),
       ('갤럭시 워치 4', 220000, 60, 'SALE', '2022-08-20 00:00:00', '2022-12-31 23:59:59', 4),
       ('갤럭시S10', 1000000, 100, 'SUSPENDED', '2022-08-20 00:00:00', '2022-12-31 23:59:59', 4),
       ('갤럭시 버즈 프로', 330000, 100, 'SALE', '2022-08-20 00:00:00', '2022-12-31 23:59:59', 4);
