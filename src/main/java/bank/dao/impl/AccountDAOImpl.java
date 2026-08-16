package bank.dao.impl;

import bank.dao.AccountDAO;
import bank.model.Account;
import bank.config.DBConnection;
import bank.model.CheckingAccount;
import bank.model.SavingsAccount;


import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDAOImpl implements AccountDAO {

    @Override
    public Account createAccount(Account account) {

        String sql = """
                INSERT INTO accounts
                    (
                        customer_id,
                        account_number,
                        account_type,
                        balance,
                        interest_rate,
                        overdraft_limit
                    )
                VALUES
                    (?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            statement.setLong(1, account.getCustomerId());
            statement.setString(2, account.getAccountNumber());

            // Determine account type from the Java class
            if (account instanceof SavingsAccount) {
                statement.setString(3, "SAVINGS");
            } else if (account instanceof CheckingAccount) {
                statement.setString(3, "CHECKING");
            } else {
                throw new IllegalArgumentException(
                        "Unsupported account type: "
                                + account.getClass().getSimpleName()
                );
            }

            statement.setBigDecimal(4, account.getBalance());

            // Savings-specific value
            if (account instanceof SavingsAccount savingsAccount) {
                statement.setBigDecimal(
                        5,
                        savingsAccount.getInterestRate()
                );
            } else {
                statement.setNull(5, Types.DECIMAL);
            }

            // Checking-specific value
            if (account instanceof CheckingAccount checkingAccount) {
                statement.setBigDecimal(
                        6,
                        checkingAccount.getOverdraftLimit()
                );
            } else {
                statement.setNull(6, Types.DECIMAL);
            }

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException(
                        "Creating account failed. No rows affected."
                );
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    account.setAccountId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException(
                            "Creating account failed. No ID obtained."
                    );
                }
            }

            return account;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to createAccount account.",
                    e
            );
        }
    }

    @Override
    public Optional<Account> findById(long accountId) {

        String sql = """
                SELECT
                    account_id,
                    customer_id,
                    account_number,
                    account_type,
                    balance,
                    interest_rate,
                    overdraft_limit,
                    created_at
                FROM accounts
                WHERE account_id = ?
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setLong(1, accountId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to find account.",
                    e
            );
        }
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {

        String sql = """
                SELECT
                    account_id,
                    customer_id,
                    account_number,
                    account_type,
                    balance,
                    interest_rate,
                    overdraft_limit,
                    created_at
                FROM accounts
                WHERE account_number = ?
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, accountNumber);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to find account by account number.",
                    e
            );
        }
    }

    @Override
    public List<Account> findAll() {

        String sql = """
                SELECT
                    account_id,
                    customer_id,
                    account_number,
                    account_type,
                    balance,
                    interest_rate,
                    overdraft_limit,
                    created_at
                FROM accounts
                ORDER BY account_id
                """;

        List<Account> accounts = new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {
                accounts.add(mapRow(resultSet));
            }

            return accounts;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to retrieve accounts.",
                    e
            );
        }
    }

    @Override
    public List<Account> findByCustomerId(long customerId) {

        String sql = """
                SELECT
                    account_id,
                    customer_id,
                    account_number,
                    account_type,
                    balance,
                    interest_rate,
                    overdraft_limit,
                    created_at
                FROM accounts
                WHERE customer_id = ?
                ORDER BY account_id
                """;

        List<Account> accounts = new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setLong(1, customerId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    accounts.add(mapRow(resultSet));
                }
            }

            return accounts;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to find accounts for customer.",
                    e
            );
        }
    }

    @Override
    public boolean update(Account account) {

        String sql = """
                UPDATE accounts
                SET
                    customer_id = ?,
                    account_number = ?,
                    account_type = ?,
                    balance = ?,
                    interest_rate = ?,
                    overdraft_limit = ?
                WHERE account_id = ?
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setLong(1, account.getCustomerId());
            statement.setString(2, account.getAccountNumber());

            if (account instanceof SavingsAccount) {
                statement.setString(3, "SAVINGS");
            } else if (account instanceof CheckingAccount) {
                statement.setString(3, "CHECKING");
            } else {
                throw new IllegalArgumentException(
                        "Unsupported account type: "
                                + account.getClass().getSimpleName()
                );
            }

            statement.setBigDecimal(4, account.getBalance());

            if (account instanceof SavingsAccount savingsAccount) {
                statement.setBigDecimal(
                        5,
                        savingsAccount.getInterestRate()
                );
            } else {
                statement.setNull(5, Types.DECIMAL);
            }

            if (account instanceof CheckingAccount checkingAccount) {
                statement.setBigDecimal(
                        6,
                        checkingAccount.getOverdraftLimit()
                );
            } else {
                statement.setNull(6, Types.DECIMAL);
            }

            statement.setLong(7, account.getAccountId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to update account.",
                    e
            );
        }
    }

    @Override
    public boolean updateBalance(
            long accountId,
            BigDecimal newBalance
    ) {

        String sql = """
                UPDATE accounts
                SET balance = ?
                WHERE account_id = ?
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setBigDecimal(1, newBalance);
            statement.setLong(2, accountId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to update account balance.",
                    e
            );
        }
    }

    @Override
    public boolean deleteById(long accountId) {

        String sql = """
                DELETE FROM accounts
                WHERE account_id = ?
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setLong(1, accountId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to delete account.",
                    e
            );
        }
    }

    private Account mapRow(ResultSet resultSet)
            throws SQLException {

        long accountId =
                resultSet.getLong("account_id");

        long customerId =
                resultSet.getLong("customer_id");

        String accountNumber =
                resultSet.getString("account_number");

        String accountType =
                resultSet.getString("account_type");

        BigDecimal balance =
                resultSet.getBigDecimal("balance");

        BigDecimal interestRate =
                resultSet.getBigDecimal("interest_rate");

        BigDecimal overdraftLimit =
                resultSet.getBigDecimal("overdraft_limit");

        Timestamp timestamp =
                resultSet.getTimestamp("created_at");

        java.time.LocalDateTime createdAt =
                timestamp != null
                        ? timestamp.toLocalDateTime()
                        : null;

        if ("SAVINGS".equalsIgnoreCase(accountType)) {

            return new SavingsAccount(
                    accountId,
                    customerId,
                    accountNumber,
                    balance,
                    createdAt,
                    interestRate
            );

        } else if ("CHECKING".equalsIgnoreCase(accountType)) {

            return new CheckingAccount(
                    accountId,
                    customerId,
                    accountNumber,
                    balance,
                    createdAt,
                    overdraftLimit
            );
        }

        throw new SQLException(
                "Unknown account type: " + accountType
        );
    }
}
