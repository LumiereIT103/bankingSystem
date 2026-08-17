package bank.service;

import bank.dao.AccountDAO;
import bank.dao.CustomerDAO;
import bank.model.Account;
import bank.model.CheckingAccount;
import bank.model.SavingsAccount;
import bank.model.Customer;

import bank.exception.AccountNotFoundException;
import bank.exception.CustomerNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class AccountService {

    private final AccountDAO accountDAO;
    private final CustomerDAO customerDAO;

    public AccountService(
            AccountDAO accountDAO,
            CustomerDAO customerDAO
    ) {
        this.accountDAO = accountDAO;
        this.customerDAO = customerDAO;
    }

    public Account createAccount(Account account) {

        validateAccount(account);

        if (account.getCustomerId() <= 0) {
            throw new IllegalArgumentException(
                    "Customer ID must be greater than zero."
            );
        }

        Optional<Customer> customer =
                customerDAO.findById(account.getCustomerId());

        if (customer.isEmpty()) {
            throw new IllegalArgumentException(
                    "Customer does not exist."
            );
        }

        Optional<Account> existingAccount =
                accountDAO.findByAccountNumber(
                        account.getAccountNumber()
                );

        if (existingAccount.isPresent()) {
            throw new IllegalArgumentException(
                    "Account number already exists: "
                            + account.getAccountNumber()
            );
        }

        return accountDAO.createAccount(account);
    }

    public Optional<Account> getAccountById(long accountId) {

        validateId(accountId);

        return accountDAO.findById(accountId);
    }

    public Optional<Account> getAccountByNumber(
            String accountNumber
    ) {

        validateAccountNumber(accountNumber);

        return accountDAO.findByAccountNumber(
                accountNumber
        );
    }

    public List<Account> getAllAccounts() {

        return accountDAO.findAll();
    }

    public List<Account> getAccountsByCustomerId(
            long customerId
    ) {

        validateId(customerId);

        return accountDAO.findByCustomerId(customerId);
    }

    public void updateAccount(Account account) {

        validateAccount(account);
        validateId(account.getAccountId());

        boolean updated =
                accountDAO.update(account);

        if (!updated) {
            throw new AccountNotFoundException(
                    "Account not found: "
                            + account.getAccountId()
            );
        }
    }

    public void closeAccount(long accountId) {

        validateId(accountId);

        Optional<Account> account =
                accountDAO.findById(accountId);

        if (account.isEmpty()) {
            throw new AccountNotFoundException(
                    "Account not found: " + accountId
            );
        }

        Account existingAccount = account.get();

        if (existingAccount.getBalance()
                .compareTo(BigDecimal.ZERO) != 0) {

            throw new IllegalArgumentException(
                    "Account balance must be zero before closing."
            );
        }

        boolean deleted =
                accountDAO.deleteById(accountId);

        if (!deleted) {
            throw new AccountNotFoundException(
                    "Account not found: " + accountId
            );
        }
    }

    private void validateAccount(Account account) {

        if (account == null) {
            throw new IllegalArgumentException(
                    "Account cannot be null."
            );
        }

        if (isBlank(account.getAccountNumber())) {
            throw new IllegalArgumentException(
                    "Account number is required."
            );
        }

        if (account.getBalance() == null) {
            throw new IllegalArgumentException(
                    "Balance cannot be null."
            );
        }

        if (account.getBalance()
                .compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Balance cannot be negative."
            );
        }

        if (account instanceof SavingsAccount savingsAccount) {

            if (savingsAccount.getInterestRate() == null) {
                throw new IllegalArgumentException(
                        "Interest rate is required for savings accounts."
                );
            }

            if (savingsAccount.getInterestRate()
                    .compareTo(BigDecimal.ZERO) < 0) {

                throw new IllegalArgumentException(
                        "Interest rate cannot be negative."
                );
            }

        } else if (account instanceof CheckingAccount checkingAccount) {

            if (checkingAccount.getOverdraftLimit() == null) {
                throw new IllegalArgumentException(
                        "Overdraft limit is required for checking accounts."
                );
            }

            if (checkingAccount.getOverdraftLimit()
                    .compareTo(BigDecimal.ZERO) < 0) {

                throw new IllegalArgumentException(
                        "Overdraft limit cannot be negative."
                );
            }

        } else {

            throw new IllegalArgumentException(
                    "Unsupported account type."
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

    private void validateAccountNumber(
            String accountNumber
    ) {

        if (isBlank(accountNumber)) {
            throw new IllegalArgumentException(
                    "Account number is required."
            );
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}