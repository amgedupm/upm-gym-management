CREATE DATABASE IF NOT EXISTS upm_gym;

USE upm_gym;

CREATE TABLE users (
    user_id VARCHAR(20) PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE memberships (
    membership_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(20) NOT NULL,
    membership_type VARCHAR(20) NOT NULL,
    start_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    auto_renew BOOLEAN DEFAULT FALSE,

    FOREIGN KEY (user_id)
        REFERENCES users(user_id)
);

CREATE TABLE bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(20) NOT NULL,
    facility_name VARCHAR(50) NOT NULL,
    booking_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status VARCHAR(20) NOT NULL,

    FOREIGN KEY (user_id)
        REFERENCES users(user_id)
);