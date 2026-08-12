USE banking_db;

-- Disable foreign key checks temporarily
SET FOREIGN_KEY_CHECKS = 0;

-- Delete all existing data
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS customers;
-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

SELECT 'All tables dropped. Run schema.sql then seed.sql to rebuild.' AS status;
