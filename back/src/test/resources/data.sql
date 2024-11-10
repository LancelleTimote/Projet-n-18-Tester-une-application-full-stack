INSERT INTO TEACHERS (first_name, last_name)
VALUES ('Margot', 'DELAHAYE'),
       ('Hélène', 'THIERCELIN');

INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES ('Yoga', 'Studio', true, 'yoga@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq'),
       ('toto', 'toto', false, 'toto3@toto.com', '$2a$10$D4bHs/ZxUEW9P9JihHeO.PTmxd/lO9epORkdd7xYFg8QmZ/YmB3Iq');

INSERT INTO SESSIONS (name, description, date, teacher_id)
VALUES ('Session 1', 'My description', '2024-12-01 10:00:00', 1),
       ('Session 2', 'My description', '2024-12-02 18:00:00', 2);


INSERT INTO PARTICIPATE (user_id, session_id) VALUES (1, 1);
INSERT INTO PARTICIPATE (user_id, session_id) VALUES (2, 1);