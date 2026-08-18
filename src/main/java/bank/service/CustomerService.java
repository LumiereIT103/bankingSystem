package bank.service;

import bank.dao.CustomerDAO;
import bank.exception.CustomerNotFoundException;
import bank.model.Customer;

import java.util.List;
import java.util.Optional;

public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public Customer createCustomer(Customer customer) {

        validateCustomer(customer);

        return customerDAO.createCustomer(customer);
    }

    public Optional<Customer> getCustomerById(long customerId) {

        validateId(customerId);

        return customerDAO.findById(customerId);
    }

    public List<Customer> getAllCustomers() {

        return customerDAO.findAll();
    }

    public void updateCustomer(Customer customer) {

        validateCustomer(customer);

        validateId(customer.getCustomerId());

        boolean updated = customerDAO.update(customer);

        if (!updated) {
            throw new CustomerNotFoundException(
                    "Customer not found: " + customer.getCustomerId()
            );
        }
    }

    public void deleteCustomer(long customerId) {

        validateId(customerId);

        boolean deleted = customerDAO.deleteById(customerId);

        if (!deleted) {
            throw new CustomerNotFoundException(
                    "Customer not found: " + customerId
            );
        }
    }

    private void validateCustomer(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer cannot be null."
            );
        }

        if (isBlank(customer.getFirstName())) {
            throw new IllegalArgumentException(
                    "First name is required."
            );
        }

        if (isBlank(customer.getLastName())) {
            throw new IllegalArgumentException(
                    "Last name is required."
            );
        }

        if (isBlank(customer.getAddress())) {
            throw new IllegalArgumentException(
                    "Address is required."
            );
        }

        if (isBlank(customer.getPhoneNumber())) {
            throw new IllegalArgumentException(
                    "Phone number is required."
            );
        }

        if (isBlank(customer.getEmail())) {
            throw new IllegalArgumentException(
                    "Email is required."
            );
        }
    }

    private void validateId(long id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "ID must be greater than zero."
            );
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}