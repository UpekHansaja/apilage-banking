-- ApilageBanking Database Schema (DDL)
-- Suitable for MySQL 8+

CREATE SCHEMA IF NOT EXISTS `apilage-banking`;
USE `apilage-banking`;

CREATE TABLE role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE', 'LOCKED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    verification_token VARCHAR(64),
    verified BOOLEAN DEFAULT FALSE,
    verification_token_created_at TIMESTAMP NULL DEFAULT NULL
);

CREATE TABLE user_role (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE
);

CREATE TABLE account (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL UNIQUE,
    user_id INT NOT NULL,
    balance DECIMAL(18,2) NOT NULL DEFAULT 0.00,
    type ENUM('SAVINGS', 'CHECKING', 'LOAN') NOT NULL,
    status ENUM('ACTIVE', 'CLOSED', 'FROZEN') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id INT NOT NULL,
    type ENUM('DEPOSIT', 'WITHDRAWAL', 'TRANSFER', 'PAYMENT') NOT NULL,
    amount DECIMAL(18,2) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES account(id)
);

-- Optional: Table for JMS message demo
CREATE TABLE jms_message (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(1024) NOT NULL,
    processed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance
CREATE INDEX idx_user_username ON user(username);
CREATE INDEX idx_account_user_id ON account(user_id);
CREATE INDEX idx_transaction_account_id ON transaction(account_id); 