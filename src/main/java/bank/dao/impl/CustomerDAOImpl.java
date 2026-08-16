package bank.dao.impl;

import bank.dao.CustomerDAO;
import bank.model.Customer;
import bank.config.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public Customer createCustomer(Customer customer) {
        String sql = """
                INSERT INTO customers
                    (first_name, last_name, address, phone_number, email)
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

            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getAddress());
            statement.setString(4, customer.getPhoneNumber());
            statement.setString(5, customer.getEmail());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating customer failed.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    customer.setCustomerId(
                            generatedKeys.getLong(1)
                    );
                } else {
                    throw new SQLException(
                            "Creating customer failed. No ID obtained."
                    );
                }
            }

            return customer;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to createAccount customer.",
                    e
            );
        }
    }

    @Override
    public Optional<Customer> findById(Long customerId) {
        String sql = """
                SELECT
                    customer_id,
                    first_name,
                    last_name,
                    address,
                    phone_number,
                    email
                FROM customers
                WHERE customer_id = ?
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setLong(1, customerId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to find customer.",
                    e
            );
        }
    }

    @Override
    public List<Customer> findAll() {
        String sql = """
                SELECT
                    customer_id,
                    first_name,
                    last_name,
                    address,
                    phone_number,
                    email
                FROM customers
                ORDER BY customer_id
                """;

        List<Customer> customers = new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {
                customers.add(mapRow(resultSet));
            }

            return customers;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to retrieve customers.",
                    e
            );
        }
    }

    @Override
    public boolean update(Customer customer) {
        String query = """
                UPDATE customers
                SET
                    first_name = ?,
                    last_name = ?,
                    address = ?,
                    phone_number = ?,
                    email = ?
                WHERE customer_id = ?
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {

            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getAddress());
            statement.setString(4, customer.getPhoneNumber());
            statement.setString(5, customer.getEmail());
            statement.setLong(6, customer.getCustomerId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to update customer.",
                    e
            );
        }
    }

    @Override
    public boolean deleteById(Long id) {
        String query = "DELETE FROM customers WHERE customer_id = ?";
        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(query)
        ) {

            statement.setLong(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to delete customer.",
                    e
            );
        }
    }
    private Customer mapRow(ResultSet resultSet) throws SQLException {

        Customer customer = new Customer();

        customer.setCustomerId(
                resultSet.getLong("customer_id")
        );

        customer.setFirstName(
                resultSet.getString("first_name")
        );

        customer.setLastName(
                resultSet.getString("last_name")
        );

        customer.setAddress(
                resultSet.getString("address")
        );

        customer.setPhoneNumber(
                resultSet.getString("phone_number")
        );

        customer.setEmail(
                resultSet.getString("email")
        );

        return customer;
    }
}
