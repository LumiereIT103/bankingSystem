package bank.menu;

import bank.model.Customer;
import bank.service.CustomerService;
import bank.util.InputValidator;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CustomerMenu {

    private final CustomerService customerService;
    private final Scanner scanner;

    public CustomerMenu(
            CustomerService customerService,
            Scanner scanner
    ) {
        this.customerService = customerService;
        this.scanner = scanner;
    }

    public void show() {

        while (true) {

            System.out.println();
            System.out.println("================================");
            System.out.println("       CUSTOMER MANAGEMENT");
            System.out.println("================================");
            System.out.println("1. Register Customer");
            System.out.println("2. Find Customer");
            System.out.println("3. List Customers");
            System.out.println("4. Update Customer");
            System.out.println("5. Delete Customer");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            try {

                switch (choice) {

                    case "1" -> createCustomer();

                    case "2" -> findCustomer();

                    case "3" -> listCustomers();

                    case "4" -> updateCustomer();

                    case "5" -> deleteCustomer();

                    case "0" -> {
                        return;
                    }

                    default ->
                            System.out.println(
                                    "Invalid choice."
                            );
                }

            } catch (IllegalArgumentException e) {

                System.out.println(
                        "Error: " + e.getMessage()
                );

            } catch (Exception e) {

                System.out.println(
                        "An unexpected error occurred."
                );
            }
        }
    }

    private void createCustomer() {

        System.out.println();
        System.out.println("===== REGISTER CUSTOMER =====");

        System.out.print("First name: ");
        String firstName =
                InputValidator.requireText(
                        scanner.nextLine(),
                        "First name"
                );

        System.out.print("Last name: ");
        String lastName =
                InputValidator.requireText(
                        scanner.nextLine(),
                        "Last name"
                );

        System.out.print("Address: ");
        String address =
                InputValidator.requireText(
                        scanner.nextLine(),
                        "Address"
                );

        System.out.print("Phone number: ");
        String phoneNumber =
                InputValidator.requireText(
                        scanner.nextLine(),
                        "Phone number"
                );

        System.out.print("Email: ");
        String email =
                InputValidator.requireText(
                        scanner.nextLine(),
                        "Email"
                );

        Customer customer =
                new Customer(
                        firstName,
                        lastName,
                        address,
                        phoneNumber,
                        email
                );

        Customer saved =
                customerService.createCustomer(customer);

        System.out.println();
        System.out.println(
                "Customer successfully created."
        );

        System.out.println(
                "Customer ID: " +
                        saved.getCustomerId()
        );
    }

    private void findCustomer() {

        System.out.println();
        System.out.println("===== FIND CUSTOMER =====");

        System.out.print("Customer ID: ");

        long customerId =
                InputValidator.requirePositiveLong(
                        scanner.nextLine(),
                        "Customer ID"
                );

        Optional<Customer> result =
                customerService.getCustomerById(
                        customerId
                );

        if (result.isEmpty()) {

            System.out.println(
                    "Customer not found."
            );

            return;
        }

        printCustomer(result.get());
    }

    private void listCustomers() {

        System.out.println();
        System.out.println("===== CUSTOMERS =====");

        List<Customer> customers =
                customerService.getAllCustomers();

        if (customers.isEmpty()) {

            System.out.println(
                    "No customers found."
            );

            return;
        }

        for (Customer customer : customers) {

            System.out.println(
                    customer.getCustomerId()
                            + " | "
                            + customer.getFirstName()
                            + " "
                            + customer.getLastName()
                            + " | "
                            + customer.getPhoneNumber()
            );
        }
    }

    private void updateCustomer() {

        System.out.println();
        System.out.println("===== UPDATE CUSTOMER =====");

        System.out.print("Customer ID: ");

        long customerId =
                InputValidator.requirePositiveLong(
                        scanner.nextLine(),
                        "Customer ID"
                );

        Optional<Customer> result =
                customerService.getCustomerById(
                        customerId
                );

        if (result.isEmpty()) {

            System.out.println(
                    "Customer not found."
            );

            return;
        }

        Customer customer = result.get();

        System.out.print(
                "First name [" +
                        customer.getFirstName() +
                        "]: "
        );

        String firstName = scanner.nextLine();

        if (!firstName.isBlank()) {
            customer.setFirstName(firstName.trim());
        }

        System.out.print(
                "Last name [" +
                        customer.getLastName() +
                        "]: "
        );

        String lastName = scanner.nextLine();

        if (!lastName.isBlank()) {
            customer.setLastName(lastName.trim());
        }

        System.out.print(
                "Address [" +
                        customer.getAddress() +
                        "]: "
        );

        String address = scanner.nextLine();

        if (!address.isBlank()) {
            customer.setAddress(address.trim());
        }

        System.out.print(
                "Phone number [" +
                        customer.getPhoneNumber() +
                        "]: "
        );

        String phoneNumber = scanner.nextLine();

        if (!phoneNumber.isBlank()) {
            customer.setPhoneNumber(
                    phoneNumber.trim()
            );
        }

        System.out.print(
                "Email [" +
                        customer.getEmail() +
                        "]: "
        );

        String email = scanner.nextLine();

        if (!email.isBlank()) {
            customer.setEmail(email.trim());
        }

        customerService.updateCustomer(customer);

        System.out.println(
                "Customer successfully updated."
        );
    }

    private void deleteCustomer() {

        System.out.println();
        System.out.println("===== DELETE CUSTOMER =====");

        System.out.print("Customer ID: ");

        long customerId =
                InputValidator.requirePositiveLong(
                        scanner.nextLine(),
                        "Customer ID"
                );

        System.out.print(
                "Are you sure? (yes/no): "
        );

        String confirmation =
                scanner.nextLine();

        if (!confirmation.equalsIgnoreCase("yes")) {

            System.out.println(
                    "Deletion cancelled."
            );

            return;
        }

        customerService.deleteCustomer(
                customerId
        );

        System.out.println(
                "Customer successfully deleted."
        );
    }

    private void printCustomer(Customer customer) {

        System.out.println();
        System.out.println("===== CUSTOMER =====");

        System.out.println(
                "ID: " + customer.getCustomerId()
        );

        System.out.println(
                "Name: " +
                        customer.getFirstName() +
                        " " +
                        customer.getLastName()
        );

        System.out.println(
                "Address: " +
                        customer.getAddress()
        );

        System.out.println(
                "Phone: " +
                        customer.getPhoneNumber()
        );

        System.out.println(
                "Email: " +
                        customer.getEmail()
        );
    }
}