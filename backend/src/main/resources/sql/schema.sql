CREATE DATABASE IF NOT EXISTS elderly_care DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE elderly_care;

CREATE TABLE IF NOT EXISTS role (
    id BIGINT PRIMARY KEY,
    create_time DATETIME,
    update_time DATETIME,
    update_by BIGINT,
    is_deleted INT DEFAULT 0,
    name VARCHAR(50) NOT NULL,
    code VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY,
    create_time DATETIME,
    create_by BIGINT,
    update_time DATETIME,
    update_by BIGINT,
    is_deleted INT DEFAULT 0,
    nickname VARCHAR(20) NOT NULL,
    username VARCHAR(20) NOT NULL,
    password VARCHAR(100) NOT NULL,
    sex INT,
    email VARCHAR(254),
    phone_number VARCHAR(20),
    role_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS room (
    id BIGINT PRIMARY KEY,
    room_floor VARCHAR(10) NOT NULL,
    room_no INT NOT NULL
);

CREATE TABLE IF NOT EXISTS bed (
    id BIGINT PRIMARY KEY,
    room_no INT NOT NULL,
    bed_status INT NOT NULL,
    remarks VARCHAR(255),
    bed_no VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS customer (
    id BIGINT PRIMARY KEY,
    create_time DATETIME,
    create_by BIGINT,
    update_time DATETIME,
    update_by BIGINT,
    is_deleted INT DEFAULT 0,
    customer_name VARCHAR(20) NOT NULL,
    customer_age INT NOT NULL,
    customer_sex INT NOT NULL,
    idcard VARCHAR(20) NOT NULL,
    room_no VARCHAR(20) NOT NULL,
    building_no VARCHAR(11) NOT NULL,
    checkin_date DATE NOT NULL,
    expiration_date DATE NOT NULL,
    contact_tel VARCHAR(20) NOT NULL,
    bed_id BIGINT NOT NULL,
    psychosomatic_state VARCHAR(255),
    attention VARCHAR(255),
    birthday DATE,
    height VARCHAR(20),
    weight VARCHAR(20),
    blood_type VARCHAR(20) NOT NULL,
    filepath VARCHAR(100),
    user_id BIGINT,
    level_id BIGINT,
    family_member VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS nursecontent (
    id BIGINT PRIMARY KEY,
    create_time DATETIME,
    create_by BIGINT,
    update_time DATETIME,
    update_by BIGINT,
    is_deleted INT DEFAULT 0,
    serial_number VARCHAR(20) NOT NULL,
    nursing_name VARCHAR(55) NOT NULL,
    service_price DECIMAL(10, 2) NOT NULL,
    message VARCHAR(100),
    status INT NOT NULL,
    execution_cycle VARCHAR(20),
    execution_times VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS nurselevel (
    id BIGINT PRIMARY KEY,
    create_time DATETIME,
    create_by BIGINT,
    update_time DATETIME,
    update_by BIGINT,
    is_deleted INT DEFAULT 0,
    level_name VARCHAR(20) NOT NULL,
    level_status INT NOT NULL
);

CREATE TABLE IF NOT EXISTS outward (
    id BIGINT PRIMARY KEY,
    create_time DATETIME,
    create_by BIGINT,
    update_time DATETIME,
    update_by BIGINT,
    is_deleted INT DEFAULT 0,
    remarks VARCHAR(100),
    customer_id BIGINT NOT NULL,
    outgoing_reason VARCHAR(100) NOT NULL,
    outgoing_time DATE NOT NULL,
    expected_return_time DATE NOT NULL,
    actual_return_time DATE,
    escorted VARCHAR(50),
    relation VARCHAR(50),
    escorted_tel VARCHAR(50),
    audit_status INT NOT NULL,
    audit_person VARCHAR(50),
    audit_time DATE
);

CREATE TABLE IF NOT EXISTS backdown (
    id BIGINT PRIMARY KEY,
    create_time DATETIME,
    create_by BIGINT,
    update_time DATETIME,
    update_by BIGINT,
    is_deleted INT DEFAULT 0,
    remarks VARCHAR(100),
    customer_id BIGINT NOT NULL,
    retreat_time DATE NOT NULL,
    retreat_type INT NOT NULL,
    retreat_reason VARCHAR(100),
    audit_status INT NOT NULL,
    audit_person VARCHAR(100),
    audit_time DATE
);
