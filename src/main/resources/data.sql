INSERT INTO tag (tag_name) VALUES ('Science');
INSERT INTO tag (tag_name) VALUES ('Cloud');
INSERT INTO tag (tag_name) VALUES ('Science');
INSERT INTO tag (tag_name) VALUES ('Sports');
INSERT INTO tag (tag_name) VALUES ('Literature');

INSERT INTO gift_certificate (create_date, certificate_description, duration, last_update_date, certificate_name, price)
 VALUES (CURRENT_TIMESTAMP(), 'AWS Masters', 10, CURRENT_TIMESTAMP(), 'AWS', 8.99);
INSERT INTO gift_certificate (create_date, certificate_description, duration, last_update_date, certificate_name, price)
 VALUES (CURRENT_TIMESTAMP(), 'Azure Masters', 10, CURRENT_TIMESTAMP(), 'Azure', 8.99);
INSERT INTO gift_certificate (create_date, certificate_description, duration, last_update_date, certificate_name, price)
 VALUES (CURRENT_TIMESTAMP(), 'Chef certificate', 10, CURRENT_TIMESTAMP(), 'Chef', 8.99);
INSERT INTO gift_certificate (create_date, certificate_description, duration, last_update_date, certificate_name, price)
 VALUES (CURRENT_TIMESTAMP(), 'Sports certificate', 10, CURRENT_TIMESTAMP(), 'Sportsman', 8.99);
