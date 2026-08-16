USE banking_db;

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
        15000.00,
        2.50,
        NULL
    ),
    (
        1,
        '2000000001',
        'CHECKING',
        25000.00,
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
        30000.00,
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

-- Juan's Savings Account: Initial deposit
INSERT INTO transactions (
    reference_number,
    account_id,
    related_account_id,
    transaction_type,
    amount,
    balance_after
)
VALUES
    (
        'TXN-000001',
        1,
        NULL,
        'DEPOSIT',
        15000.00,
        15000.00
    );

-- Juan's Checking Account: Initial deposit
INSERT INTO transactions (
    reference_number,
    account_id,
    related_account_id,
    transaction_type,
    amount,
    balance_after
)
VALUES
    (
        'TXN-000002',
        2,
        NULL,
        'DEPOSIT',
        25000.00,
        25000.00
    );

-- Maria's Savings Account: Initial deposit
INSERT INTO transactions (
    reference_number,
    account_id,
    related_account_id,
    transaction_type,
    amount,
    balance_after
)
VALUES
    (
        'TXN-000003',
        3,
        NULL,
        'DEPOSIT',
        30000.00,
        30000.00
    );

-- Carlos's Savings Account: Initial deposit
INSERT INTO transactions (
    reference_number,
    account_id,
    related_account_id,
    transaction_type,
    amount,
    balance_after
)
VALUES
    (
        'TXN-000004',
        4,
        NULL,
        'DEPOSIT',
        10000.00,
        10000.00
    );

-- Carlos's Checking Account: Initial deposit
INSERT INTO transactions (
    reference_number,
    account_id,
    related_account_id,
    transaction_type,
    amount,
    balance_after
)
VALUES
    (
        'TXN-000005',
        5,
        NULL,
        'DEPOSIT',
        18000.00,
        18000.00
    );

-- Anna's Savings Account: Initial deposit
INSERT INTO transactions (
    reference_number,
    account_id,
    related_account_id,
    transaction_type,
    amount,
    balance_after
)
VALUES
    (
        'TXN-000006',
        6,
        NULL,
        'DEPOSIT',
        45000.00,
        45000.00
    );

-- =========================================================
-- SAMPLE WITHDRAWAL
-- Juan withdraws 2,000 from Savings
-- =========================================================

INSERT INTO transactions (
    reference_number,
    account_id,
    related_account_id,
    transaction_type,
    amount,
    balance_after
)
VALUES
    (
        'TXN-000007',
        1,
        NULL,
        'WITHDRAWAL',
        2000.00,
        13000.00
    );

-- =========================================================
-- SAMPLE TRANSFER
-- Juan's Checking -> Maria's Savings
-- =========================================================

INSERT INTO transactions (
    reference_number,
    account_id,
    related_account_id,
    transaction_type,
    amount,
    balance_after
)
VALUES
    (
        'TXN-000008',
        2,
        3,
        'TRANSFER_OUT',
        5000.00,
        20000.00
    );

INSERT INTO transactions (
    reference_number,
    account_id,
    related_account_id,
    transaction_type,
    amount,
    balance_after
)
VALUES
    (
        'TXN-000009',
        3,
        2,
        'TRANSFER_IN',
        5000.00,
        35000.00
    );