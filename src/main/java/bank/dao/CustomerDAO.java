package bank.dao;
import bank.model.Customer;

import java.util.List;
import java.util.Optional;
public interface CustomerDAO {

    Customer createCustomer(Customer customer);
    //make a new customer

    Optional<Customer> findById(Long customerId);
    //find a customer

    List<Customer> findAll();
    //find all customer

    boolean update(Customer customer);
    //update customer info

    boolean deleteById(Long customerId);
    //destroy customer data
}
