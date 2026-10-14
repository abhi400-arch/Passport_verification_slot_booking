CREATE DATABASE IF NOT EXISTS passport_booking;
USE passport_booking;

DROP TABLE IF EXISTS gate_checkins;
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS waitlist_requests;
DROP TABLE IF EXISTS document_proofs;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS slots;
DROP TABLE IF EXISTS passport_applications;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL,
    password_hash VARCHAR(300) NOT NULL,
    role ENUM('USER','ADMIN') NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE passport_applications (
    application_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL UNIQUE,
    passport_number VARCHAR(20) NOT NULL UNIQUE,
    date_of_birth DATE NOT NULL,
    nationality VARCHAR(50) NOT NULL DEFAULT 'Indian',
    address VARCHAR(255) NOT NULL,
    application_type ENUM('NEW','RENEWAL') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_passport_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE slots (
    slot_id INT PRIMARY KEY AUTO_INCREMENT,
    slot_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    capacity INT NOT NULL DEFAULT 1,
    booked_count INT NOT NULL DEFAULT 0,
    status ENUM('AVAILABLE','CLOSED') NOT NULL DEFAULT 'AVAILABLE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_slot(slot_date, start_time, end_time)
);

CREATE TABLE bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    application_id INT NOT NULL,
    slot_id INT NOT NULL,
    booking_reference VARCHAR(30) NOT NULL UNIQUE,
    booking_status VARCHAR(30) NOT NULL DEFAULT 'CONFIRMED',
    booked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_booking_application FOREIGN KEY (application_id) REFERENCES passport_applications(application_id) ON DELETE CASCADE,
    CONSTRAINT fk_booking_slot FOREIGN KEY (slot_id) REFERENCES slots(slot_id) ON DELETE RESTRICT,
    UNIQUE KEY uq_active_user_slot(user_id, slot_id)
);

CREATE TABLE document_proofs (
    doc_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    doc_type VARCHAR(50) NOT NULL,
    doc_name VARCHAR(100) NOT NULL,
    doc_identifier VARCHAR(100),
    verification_status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    remarks VARCHAR(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_doc_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE KEY uq_user_doctype (user_id, doc_type)
);

CREATE TABLE waitlist_requests (
    waitlist_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    preferred_date_from DATE NOT NULL,
    preferred_date_to DATE NOT NULL,
    urgency_category VARCHAR(50) NOT NULL DEFAULT 'TATKAL_URGENT',
    reason VARCHAR(255),
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_waitlist_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE notifications (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(30) NOT NULL DEFAULT 'INFO',
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notify_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE gate_checkins (
    checkin_id INT PRIMARY KEY AUTO_INCREMENT,
    booking_id INT NOT NULL,
    stage VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL,
    officer_name VARCHAR(100) NOT NULL,
    notes VARCHAR(255),
    logged_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_checkin_booking FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE CASCADE
);

CREATE INDEX idx_slots_date ON slots(slot_date);
CREATE INDEX idx_bookings_user ON bookings(user_id);
CREATE INDEX idx_bookings_status ON bookings(booking_status);
CREATE INDEX idx_notifications_user ON notifications(user_id);
CREATE INDEX idx_waitlist_status ON waitlist_requests(status);
CREATE INDEX idx_checkins_booking ON gate_checkins(booking_id);
