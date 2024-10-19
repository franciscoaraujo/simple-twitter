-- src/main/resources/data.sql

-- Insert roles

INSERT INTO tb_roles (role_id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO tb_roles (role_id, name) VALUES (2, 'ROLE_USER');



-- Insert users
/*INSERT INTO user (user_id, username, email) VALUES (1, 'user1', 'user1@example.com');
INSERT INTO user (user_id, username, email) VALUES (2, 'admin', 'admin@example.com');

-- Insert user roles
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1); -- user1 has ROLE_USER
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2); -- admin has ROLE_ADMIN*/