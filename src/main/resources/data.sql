ALTER TABLE users SET REFERENTIAL_INTEGRITY FALSE;
ALTER TABLE roles SET REFERENTIAL_INTEGRITY FALSE;
ALTER TABLE transactions SET REFERENTIAL_INTEGRITY FALSE;
ALTER TABLE category SET REFERENTIAL_INTEGRITY FALSE;
ALTER TABLE account SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE users;
TRUNCATE TABLE roles;
TRUNCATE TABLE transactions;
TRUNCATE TABLE category;
TRUNCATE TABLE account;

ALTER TABLE users SET REFERENTIAL_INTEGRITY TRUE;
ALTER TABLE roles SET REFERENTIAL_INTEGRITY TRUE;
ALTER TABLE transactions SET REFERENTIAL_INTEGRITY TRUE;
ALTER TABLE category SET REFERENTIAL_INTEGRITY TRUE;
ALTER TABLE account SET REFERENTIAL_INTEGRITY TRUE;

-- Users
INSERT INTO users (NAME, EMAIL, PASSWORD, CELLPHONE, PICTURE)
VALUES
('Admin', 'admin@admin.com', '$2a$12$E.GBokDDH4SeVF22dacPpOnzll0ZnbQEexdf3sTcogvw1EJYzH.EK', '999999998', 'admin_pic'),
('Test', 'a@a', '$2a$12$hECHkvkdwCElCi0DUOZz4Ofgpke2E6ssE8o3YlWYug5vBmuksfxvW', '999999994', 'admin_pic'),
('Alice', 'alice@test.com', '$2b$12$ieYk9tpZA45fd/tnosOeZ.t3zkVkNrXwm2ehNyr6QU0Afck73J9Sy', '999999995', 'alice_pic'),
('Bob', 'bob@test.com', '$2b$12$rkB7qfO63uWwJ9KDccT/puXNtQtrT62sXirzJNNkTi1TruRr0NaPW', '999999997', 'bob_pic'),
('Charlie', 'charlie@test.com', '$2b$12$WQozj3UXgRfkxX2yup2pBO4Qgl2Dx.IUm852r11H/JxmZuw9a64da', '999999996', 'charlie_pic');
-- admin_password, 1, alice_password, bob_password, charlie_password

-- Roles
INSERT INTO roles (NAME) VALUES ('ROLE_USER');
INSERT INTO roles (NAME) VALUES ('ROLE_ADMIN');

INSERT INTO user_roles (USER_ID, ROLE_ID)
    VALUES (
        (SELECT id FROM users WHERE email = 'admin@admin.com'),
        (SELECT id FROM roles WHERE name = 'ROLE_ADMIN')
    ),
    (
        (SELECT id FROM users WHERE email = 'a@a'),
        (SELECT id FROM roles WHERE name = 'ROLE_ADMIN')
    );

INSERT INTO user_roles (USER_ID, ROLE_ID)
    VALUES (
        (SELECT id FROM users WHERE email = 'admin@admin.com'),
        (SELECT id FROM roles WHERE name = 'ROLE_USER')
    ),
    (
        (SELECT id FROM users WHERE email = 'a@a'),
        (SELECT id FROM roles WHERE name = 'ROLE_USER')
    );

-- Categories
INSERT INTO category (NAME, USER_FOREING_KEY)
VALUES
('Groceries', (SELECT id FROM users WHERE email = 'admin@admin.com')),
('Entertainment', (SELECT id FROM users WHERE email = 'admin@admin.com')),
('Utilities', (SELECT id FROM users WHERE email = 'admin@admin.com')),
('Groceries', (SELECT id FROM users WHERE email = 'a@a')),
('Entertainment', (SELECT id FROM users WHERE email = 'a@a')),
('Utilities', (SELECT id FROM users WHERE email = 'a@a')),
('Groceries', (SELECT id FROM users WHERE email = 'alice@test.com')),
('Entertainment', (SELECT id FROM users WHERE email = 'alice@test.com')),
('Utilities', (SELECT id FROM users WHERE email = 'alice@test.com')),
('Groceries', (SELECT id FROM users WHERE email = 'bob@test.com')),
('Entertainment', (SELECT id FROM users WHERE email = 'bob@test.com')),
('Utilities', (SELECT id FROM users WHERE email = 'bob@test.com')),
('Groceries', (SELECT id FROM users WHERE email = 'charlie@test.com')),
('Entertainment', (SELECT id FROM users WHERE email = 'charlie@test.com')),
('Utilities', (SELECT id FROM users WHERE email = 'charlie@test.com'));

-- Accounts
INSERT INTO account (NAME, USER_FOREING_KEY)
VALUES
('Admin Checking', (SELECT id FROM users WHERE email = 'admin@admin.com')),
('Test Checking', (SELECT id FROM users WHERE email = 'a@a')),
('Alice Checking', (SELECT id FROM users WHERE email = 'alice@test.com')),
('Bob Checking', (SELECT id FROM users WHERE email = 'bob@test.com')),
('Charlie Checking', (SELECT id FROM users WHERE email = 'charlie@test.com'));

-- Transactions for admin@admin.com
INSERT INTO transactions (AMOUNT, HAPPENED_ON, CREATED_ON, USER_ID, CATEGORY_ID, ACCOUNT_ID)
VALUES
(10.00, '2024-11-25T08:00', CURRENT_TIMESTAMP,
 (SELECT id FROM users WHERE email = 'admin@admin.com'),
 (SELECT id FROM category WHERE NAME = 'Groceries' AND USER_FOREING_KEY = (SELECT id FROM users WHERE email = 'admin@admin.com')),
 (SELECT id FROM account WHERE NAME = 'Admin Checking')),
(15.00, '2024-11-24T14:00', CURRENT_TIMESTAMP,
 (SELECT id FROM users WHERE email = 'admin@admin.com'),
 (SELECT id FROM category WHERE NAME = 'Entertainment' AND USER_FOREING_KEY = (SELECT id FROM users WHERE email = 'admin@admin.com')),
 (SELECT id FROM account WHERE NAME = 'Admin Checking')),
(20.00, '2024-11-23T12:00', CURRENT_TIMESTAMP,
 (SELECT id FROM users WHERE email = 'admin@admin.com'),
 (SELECT id FROM category WHERE NAME = 'Utilities' AND USER_FOREING_KEY = (SELECT id FROM users WHERE email = 'admin@admin.com')),
 (SELECT id FROM account WHERE NAME = 'Admin Checking')),
(25.00, '2024-11-22T10:00', CURRENT_TIMESTAMP,
 (SELECT id FROM users WHERE email = 'admin@admin.com'),
 (SELECT id FROM category WHERE NAME = 'Groceries' AND USER_FOREING_KEY = (SELECT id FROM users WHERE email = 'admin@admin.com')),
 (SELECT id FROM account WHERE NAME = 'Admin Checking'));

-- Transactions for a@a
INSERT INTO transactions (AMOUNT, HAPPENED_ON, CREATED_ON, USER_ID, CATEGORY_ID, ACCOUNT_ID)
VALUES
(5.00, '2024-11-25T08:30', CURRENT_TIMESTAMP,
 (SELECT id FROM users WHERE email = 'a@a'),
 (SELECT id FROM category WHERE NAME = 'Groceries' AND USER_FOREING_KEY = (SELECT id FROM users WHERE email = 'a@a')),
 (SELECT id FROM account WHERE NAME = 'Test Checking')),
(7.50, '2024-11-24T15:00', CURRENT_TIMESTAMP,
 (SELECT id FROM users WHERE email = 'a@a'),
 (SELECT id FROM category WHERE NAME = 'Entertainment' AND USER_FOREING_KEY = (SELECT id FROM users WHERE email = 'a@a')),
 (SELECT id FROM account WHERE NAME = 'Test Checking')),
(12.00, '2024-11-23T10:00', CURRENT_TIMESTAMP,
 (SELECT id FROM users WHERE email = 'a@a'),
 (SELECT id FROM category WHERE NAME = 'Utilities' AND USER_FOREING_KEY = (SELECT id FROM users WHERE email = 'a@a')),
 (SELECT id FROM account WHERE NAME = 'Test Checking')),
(9.00, '2024-11-22T11:00', CURRENT_TIMESTAMP,
 (SELECT id FROM users WHERE email = 'a@a'),
 (SELECT id FROM category WHERE NAME = 'Groceries' AND USER_FOREING_KEY = (SELECT id FROM users WHERE email = 'a@a')),
 (SELECT id FROM account WHERE NAME = 'Test Checking'));
