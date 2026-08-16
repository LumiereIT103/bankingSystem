package bank.dao.impl;

import bank.dao.TransactionDAO;
import bank.config.DBConnection;
import bank.model.Transaction;
import bank.model.TransactionType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionDAOImpl implements TransactionDAO {

    @Override
    public Transaction createTransaction(Transaction transaction) {

        String sql = """
                INSERT INTO transactions
                    (
                        reference_number,
                        account_id,
                        transaction_type,
                        amount,
                        balance_after
                    )
                VALUES
                    (?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            statement.setString(
                    1,
                    transaction.getReferenceNumber()
            );

            statement.setLong(
                    2,
                    transaction.getAccountId()
            );

            statement.setString(
                    3,
                    transaction.getType().name()
            );

            statement.setBigDecimal(
                    4,
                    transaction.getAmount()
            );

            statement.setBigDecimal(
                    5,
                    transaction.getBalanceAfter()
            );

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException(
                        "Creating transaction failed. No rows affected."
                );
            }

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    transaction.setTransactionId(
                            generatedKeys.getLong(1)
                    );

                } else {

                    throw new SQLException(
                            "Creating transaction failed. No ID obtained."
                    );
                }
            }

            return transaction;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to createTransaction transaction.",
                    e
            );
        }
    }

    @Override
    public Optional<Transaction> findById(long transactionId) {

        String sql = """
                SELECT
                    transaction_id,
                    reference_number,
                    account_id,
                    transaction_type,
                    amount,
                    balance_after,
                    created_at
                FROM transactions
                WHERE transaction_id = ?
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setLong(1, transactionId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(
                            mapRow(resultSet)
                    );
                }

                return Optional.empty();
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to find transaction.",
                    e
            );
        }
    }

    @Override
    public Optional<Transaction> findByReferenceNumber(
            String referenceNumber
    ) {

        String sql = """
                SELECT
                    transaction_id,
                    reference_number,
                    account_id,
                    transaction_type,
                    amount,
                    balance_after,
                    created_at
                FROM transactions
                WHERE reference_number = ?
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, referenceNumber);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(
                            mapRow(resultSet)
                    );
                }

                return Optional.empty();
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to find transaction by reference number.",
                    e
            );
        }
    }

    @Override
    public List<Transaction> findByAccountId(long accountId) {

        String sql = """
                SELECT
                    transaction_id,
                    reference_number,
                    account_id,
                    transaction_type,
                    amount,
                    balance_after,
                    created_at
                FROM transactions
                WHERE account_id = ?
                ORDER BY created_at DESC
                """;

        List<Transaction> transactions = new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setLong(1, accountId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    transactions.add(
                            mapRow(resultSet)
                    );
                }
            }

            return transactions;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to retrieve account transactions.",
                    e
            );
        }
    }

    @Override
    public List<Transaction> findByAccountIdOrderByDateDesc(
            long accountId
    ) {

        String sql = """
                SELECT
                    transaction_id,
                    reference_number,
                    account_id,
                    transaction_type,
                    amount,
                    balance_after,
                    created_at
                FROM transactions
                WHERE account_id = ?
                ORDER BY created_at DESC, transaction_id DESC
                """;

        List<Transaction> transactions = new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setLong(1, accountId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    transactions.add(
                            mapRow(resultSet)
                    );
                }
            }

            return transactions;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to retrieve transaction history.",
                    e
            );
        }
    }

    @Override
    public List<Transaction> findRecentByAccountId(
            long accountId,
            int limit
    ) {

        if (limit <= 0) {
            throw new IllegalArgumentException(
                    "Limit must be greater than zero."
            );
        }

        String sql = """
                SELECT
                    transaction_id,
                    reference_number,
                    account_id,
                    transaction_type,
                    amount,
                    balance_after,
                    created_at
                FROM transactions
                WHERE account_id = ?
                ORDER BY created_at DESC, transaction_id DESC
                LIMIT ?
                """;

        List<Transaction> transactions = new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setLong(1, accountId);
            statement.setInt(2, limit);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    transactions.add(
                            mapRow(resultSet)
                    );
                }
            }

            return transactions;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to retrieve recent transactions.",
                    e
            );
        }
    }

    private Transaction mapRow(ResultSet resultSet)
            throws SQLException {

        Transaction transaction = new Transaction();

        transaction.setTransactionId(
                resultSet.getLong("transaction_id")
        );

        transaction.setReferenceNumber(
                resultSet.getString("reference_number")
        );

        transaction.setAccountId(
                resultSet.getLong("account_id")
        );

        transaction.setType(
                TransactionType.valueOf(
                        resultSet.getString("transaction_type")
                )
        );

        transaction.setAmount(
                resultSet.getBigDecimal("amount")
        );

        transaction.setBalanceAfter(
                resultSet.getBigDecimal("balance_after")
        );

        Timestamp timestamp =
                resultSet.getTimestamp("created_at");

        if (timestamp != null) {
            transaction.setCreatedAt(
                    timestamp.toLocalDateTime()
            );
        }

        return transaction;
    }
}
