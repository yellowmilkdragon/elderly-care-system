USE elderly_care;

INSERT INTO role (id, create_time, is_deleted, name, code) VALUES
(1, NOW(), 0, '系统管理员', 'ADMIN'),
(2, NOW(), 0, '健康管家', 'CAREGIVER');

INSERT INTO user (id, create_time, is_deleted, nickname, username, password, phone_number, role_id) VALUES
(1, NOW(), 0, '系统管理员', 'admin', '123456', '13800000001', 1),
(2, NOW(), 0, '运营管理员', 'admin1', '123456', '13800000002', 1),
(3, NOW(), 0, '张护工', 'care01', '123456', '13800000003', 2),
(4, NOW(), 0, '李护工', 'care02', '123456', '13800000004', 2);
