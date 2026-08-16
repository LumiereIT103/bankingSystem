USE banking_db;

-- =========================================================
-- RESET EXISTING TEST DATA
-- =========================================================

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE transactions;
TRUNCATE TABLE accounts;
TRUNCATE TABLE customers;

SET FOREIGN_KEY_CHECKS = 1;


-- =========================================================
-- SEED CUSTOMERS
-- =========================================================

INSERT INTO customers (
    first_name,
    last_name,
    address,
    phone_number,
    email
)
VALUES
    (
        'Juan',
        'Dela Cruz',
        'Caloocan City',
        '09171234567',
        'juan.delacruz@example.com'
    ),
    (
        'Maria',
        'Santos',
        'Quezon City',
        '09181234567',
        'maria.santos@example.com'
    ),
    (
        'Carlos',
        'Reyes',
        'Manila',
        '09191234567',
        'carlos.reyes@example.com'
    ),
    (
        'Anna',
        'Garcia',
        'Valenzuela City',
        '09201234567',
        'anna.garcia@example.com'
    );


-- =========================================================
-- SEED ACCOUNTS
-- =========================================================

-- Juan Dela Cruz
INSERT INTO accounts (
    customer_id,
    account_number,
    account_type,
    balance,
    interest_rate,
    overdraft_limit
)
VALUES
    (
        1,
        '1000000001',
        'SAVINGS',
        13000.00,
        2.50,
        NULL
    ),
    (
        1,
        '2000000001',
        'CHECKING',
        20000.00,
        NULL,
        5000.00
    );

-- Maria Santos
INSERT INTO accounts (
    customer_id,
    account_number,
    account_type,
    balance,
    interest_rate,
    overdraft_limit
)
VALUES
    (
        2,
        '1000000002',
        'SAVINGS',
        35000.00,
        2.75,
        NULL
    );

-- Carlos Reyes
INSERT INTO accounts (
    customer_id,
    account_number,
    account_type,
    balance,
    interest_rate,
    overdraft_limit
)
VALUES
    (
        3,
        '1000000003',
        'SAVINGS',
        10000.00,
        2.50,
        NULL
    ),
    (
        3,
        '2000000002',
        'CHECKING',
        18000.00,
        NULL,
        3000.00
    );

-- Anna Garcia
INSERT INTO accounts (
    customer_id,
    account_number,
    account_type,
    balance,
    interest_rate,
    overdraft_limit
)
VALUES
    (
        4,
        '1000000004',
        'SAVINGS',
        45000.00,
        3.00,
        NULL
    );


-- =========================================================
-- SEED TRANSACTIONS
-- =========================================================

-- Juan Savings
-- Initial deposit: 15,000
INSERT INTO transactions (
    reference_number,
    transfer_reference,
    account_id,
    transaction_type,
    amount,
    balance_after
)
VALUES
    (
        'TXN-000008',
        'TRF-000001',
        2,
        'TRANSFER_OUT',
        5000.00,
        20000.00
    );

INSERT INTO transactions (
    reference_number,
    transfer_reference,
    account_id,
    transaction_type,
    amount,
    balance_after
)
VALUES
    (
        'TXN-000009',
        'TRF-000001',
        3,
        'TRANSFER_IN',
        5000.00,
        35000.00
    );