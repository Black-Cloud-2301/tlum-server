INSERT INTO users(id, created_at, created_by, is_active, last_updated_at, last_updated_by, address, avatar, code,
                  date_of_birth, email, firstname, gender, lastname, password, phone_number)
VALUES
    (1, Date(now()), 1, 1, Date(now()), 1, 'Hà Nội', NULL, 'admin', Date(now()), 'admin@gmail.com', 'Super', 0,
        'Admin', '$2a$10$xbw4hB8t4FtM0n5EqyES9ODzkSqMbNZ.YE62g7Thg9m0.l3/00FTu', '0123456789'),
    (2, Date(now()), 1, 1, Date(now()), 1, 'Hà Nội', NULL, 'A20687', '1994-01-23', 'tuank2301@gmail.com', 'Nguyễn', 0,
        'Tú Anh', '$2a$10$xbw4hB8t4FtM0n5EqyES9ODzkSqMbNZ.YE62g7Thg9m0.l3/00FTu', '0123456789');

INSERT INTO roles(id, created_at, created_by, is_active, last_updated_at, last_updated_by, name)
VALUES (1, Date(now()), 1, 1, Date(now()), 1, 'SYS_ADMIN'),
       (2, Date(now()), 1, 1, Date(now()), 1, 'STUDENT');

INSERT INTO permissions(id, created_at, created_by, is_active, last_updated_at, last_updated_by,
                        module, function, action)
VALUES (1, Date(now()), 1, 1, Date(now()), 1, 'INDIVIDUAL', 'STUDENT', 'VIEW'),
       (2, Date(now()), 1, 1, Date(now()), 1, 'INDIVIDUAL', 'STUDENT', 'CREATE'),
       (3, Date(now()), 1, 1, Date(now()), 1, 'INDIVIDUAL', 'STUDENT', 'UPDATE'),
       (4, Date(now()), 1, 1, Date(now()), 1, 'INDIVIDUAL', 'STUDENT', 'DELETE'),
       (5, Date(now()), 1, 1, Date(now()), 1, 'INDIVIDUAL', 'STUDENT', 'LOCK'),
       (6, Date(now()), 1, 1, Date(now()), 1, 'INDIVIDUAL', 'STUDENT', 'IMPORT');

INSERT INTO role_permissions(role_id, permission_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (2, 1);

INSERT INTO user_roles(user_id, role_id)
VALUES
    (1, 1),
    (2, 2);

INSERT INTO major(id, created_at, created_by, is_active, last_updated_at, last_updated_by, name)
VALUES (1, Date(now()), 1, 1, Date(now()), 1, 'Công nghệ thông tin'),
       (2, Date(now()), 1, 1, Date(now()), 1, 'Kinh tế'),
       (3, Date(now()), 1, 1, Date(now()), 1, 'Ngoại ngữ');

INSERT INTO student(created_at, created_by, is_active, last_updated_at, last_updated_by, id, major_id, status)
VALUES (Date(now()), 1, 1, Date(now()), 1, 2, 1, 1);