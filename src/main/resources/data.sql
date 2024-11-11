ALTER TABLE users SET REFERENTIAL_INTEGRITY FALSE;
ALTER TABLE roles SET REFERENTIAL_INTEGRITY FALSE;
ALTER TABLE transactions SET REFERENTIAL_INTEGRITY FALSE;
ALTER TABLE category SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE users;
TRUNCATE TABLE roles;
TRUNCATE TABLE transactions;
TRUNCATE TABLE category;

ALTER TABLE users SET REFERENTIAL_INTEGRITY TRUE;
ALTER TABLE roles SET REFERENTIAL_INTEGRITY TRUE;
ALTER TABLE transactions SET REFERENTIAL_INTEGRITY TRUE;
ALTER TABLE category SET REFERENTIAL_INTEGRITY TRUE;

INSERT INTO users (NAME, EMAIL, PASSWORD, CELLPHONE, PICTURE)
VALUES
('Admin', 'admin@admin.com', '$2a$12$E.GBokDDH4SeVF22dacPpOnzll0ZnbQEexdf3sTcogvw1EJYzH.EK', '999999998', 'admin_pic'),
('Alice', 'alice@test.com', '$2b$12$ieYk9tpZA45fd/tnosOeZ.t3zkVkNrXwm2ehNyr6QU0Afck73J9Sy', '999999995', 'alice_pic'),
('Bob', 'bob@test.com', '$2b$12$rkB7qfO63uWwJ9KDccT/puXNtQtrT62sXirzJNNkTi1TruRr0NaPW', '999999997', 'bob_pic'),
('Charlie', 'charlie@test.com', '$2b$12$WQozj3UXgRfkxX2yup2pBO4Qgl2Dx.IUm852r11H/JxmZuw9a64da', '999999996', 'charlie_pic');
-- admin_password, alice_password, bob_password, charlie_password

INSERT INTO roles (NAME) VALUES ('ROLE_USER');
INSERT INTO roles (NAME) VALUES ('ROLE_ADMIN');

INSERT INTO user_roles (USER_ID, ROLE_ID)
    VALUES (
        (SELECT id FROM users WHERE email = 'admin@admin.com'),
        (SELECT id FROM roles WHERE name = 'ROLE_ADMIN')
    );
