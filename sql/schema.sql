CREATE DATABASE IF NOT EXISTS banking_db

CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE banking_db;

CREATE TABLE IF NOT EXISTS customers
(
    customer_id           BIGINT AUTO_INCREMENT PRIMARY KEY,

    first_name   VARCHAR(50) NOT NULL,
    last_name    VARCHAR(50) NOT NULL,
    address      VARCHAR(255),
    phone_number VARCHAR(20),
    email        VARCHAR(100),

    created_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS accounts(
    account_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,

    account_number VARCHAR(20) NOT NULL UNIQUE,
    account_type VARCHAR(20) NOT NULL,
    balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,

    interest_rate DECIMAL(5, 2),
    overdraft_limit DECIMAL(15, 2),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_accounts_customer
    FOREIGN KEY (customer_id)
    REFERENCES customers(customer_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);

CREATE TABLE transactions (
  transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,

  reference_number VARCHAR(50) NOT NULL UNIQUE,

  transfer_reference VARCHAR(50),

  account_id BIGINT NOT NULL,

  transaction_type VARCHAR(30) NOT NULL,

  amount DECIMAL(15, 2) NOT NULL,

  balance_after DECIMAL(15, 2) NOT NULL,

  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT fk_transactions_account
      FOREIGN KEY (account_id)
          REFERENCES accounts(account_id)
          ON DELETE RESTRICT
          ON UPDATE CASCADE
);