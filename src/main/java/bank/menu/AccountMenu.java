package bank.menu;

import bank.exception.AccountNotFoundException;
import bank.exception.CustomerNotFoundException;
import bank.model.Account;
import bank.model.CheckingAccount;
import bank.model.SavingsAccount;
import bank.service.AccountService;
import bank.util.AccountNumberGenerator;
import bank.util.InputValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class AccountMenu {
    private static long nextAccountNumber = 9000000001L;
    private final AccountService accountService;
    private final Scanner scanner;

    public AccountMenu(
            AccountService accountService,
            Scanner scanner
    ) {
        this.accountService = accountService;
        this.scanner = scanner;
    }

    public void show() {

        while (true) {

            System.out.println();
            System.out.println("================================");
            System.out.println("       ACCOUNT MANAGEMENT");
            System.out.println("================================");
            System.out.println("1. Open Savings Account");
            System.out.println("2. Open Checking Account");
            System.out.println("3. Find Account by ID");
            System.out.println("4. Find Account by Number");
            System.out.println("5. List All Accounts");
            System.out.println("6. View Customer Accounts");
            System.out.println("7. Update Account");
            System.out.println("8. Close Account");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            try {

                switch (choice) {

                    case "1" -> createSavingsAccount();

                    case "2" -> createCheckingAccount();

                    case "3" -> findAccountById();

                    case "4" -> findAccountByNumber();

                    case "5" -> listAllAccounts();

                    case "6" -> listCustomerAccounts();

                    case "7" -> updateAccount();

                    case "8" -> closeAccount();

                    case "0" -> {
                        return;
                    }

                    default ->
                            System.out.println(
                                    "Invalid choice."
                            );
                }

            } catch (CustomerNotFoundException |
                     AccountNotFoundException e) {

                System.out.println(
                        "Error: " + e.getMessage()
                );

            } catch (IllegalArgumentException e) {

                System.out.println(
                        "Error: " + e.getMessage()
                );

            } catch (Exception e) {

                System.out.println(
                        "An unexpected error occurred."
                );

                e.printStackTrace();
            }
        }
    }

    private void createSavingsAccount() {

        System.out.println();
        System.out.println("===== OPEN SAVINGS ACCOUNT =====");

        long customerId =
                InputValidator.requirePositiveLong(
                        prompt("Customer ID: "),
                        "Customer ID"
                );

        BigDecimal initialBalance =
                InputValidator.requirePositiveAmount(
                        prompt("Initial deposit: ₱")
                );

        BigDecimal interestRate =
                InputValidator.requirePositiveAmount(
                        prompt("Interest rate: ")
                );

        String accountNumber =
                AccountNumberGenerator.generate();

        SavingsAccount account =
                new SavingsAccount(
                        0L,
                        customerId,
                        accountNumber,
                        initialBalance,
                        null,
                        interestRate
                );

        Account saved =
                accountService.createAccount(account);

        System.out.println();
        System.out.println(
                "Savings account successfully created."
        );

        System.out.println(
                "Account ID: " +
                        saved.getAccountId()
        );

        System.out.println(
                "Account Number: " +
                        saved.getAccountNumber()
        );

        System.out.println(
                "Initial Balance: ₱" +
                        saved.getBalance()
        );
    }

    private void createCheckingAccount() {

        System.out.println();
        System.out.println("===== OPEN CHECKING ACCOUNT =====");

        long customerId =
                InputValidator.requirePositiveLong(
                        prompt("Customer ID: "),
                        "Customer ID"
                );

        BigDecimal initialBalance =
                InputValidator.requirePositiveAmount(
                        prompt("Initial deposit: ₱")
                );

        BigDecimal overdraftLimit =
                InputValidator.requirePositiveAmount(
                        prompt("Overdraft limit: ₱")
                );

        String accountNumber =
                AccountNumberGenerator.generate();

        CheckingAccount account =
                new CheckingAccount(
                        0L,
                        customerId,
                        accountNumber,
                        initialBalance,
                        null,
                        overdraftLimit
                );

        Account saved =
                accountService.createAccount(account);

        System.out.println();
        System.out.println(
                "Checking account successfully created."
        );

        System.out.println(
                "Account ID: " +
                        saved.getAccountId()
        );

        System.out.println(
                "Account Number: " +
                        saved.getAccountNumber()
        );

        System.out.println(
                "Initial Balance: ₱" +
                        saved.getBalance()
        );
    }

    private void findAccountById() {

        System.out.println();
        System.out.println("===== FIND ACCOUNT =====");

        long accountId =
                InputValidator.requirePositiveLong(
                        prompt("Account ID: "),
                        "Account ID"
                );

        Optional<Account> result =
                accountService.getAccountById(
                        accountId
                );

        if (result.isEmpty()) {

            System.out.println(
                    "Account not found."
            );

            return;
        }

        printAccount(result.get());
    }

    private void findAccountByNumber() {

        System.out.println();
        System.out.println("===== FIND ACCOUNT =====");

        String accountNumber =
                InputValidator.requireText(
                        prompt("Account Number: "),
                        "Account number"
                );

        Optional<Account> result =
                accountService.getAccountByNumber(
                        accountNumber
                );

        if (result.isEmpty()) {

            System.out.println(
                    "Account not found."
            );

            return;
        }

        printAccount(result.get());
    }

    private void listAllAccounts() {

        System.out.println();
        System.out.println("===== ALL ACCOUNTS =====");

        List<Account> accounts =
                accountService.getAllAccounts();

        if (accounts.isEmpty()) {

            System.out.println(
                    "No accounts found."
            );

            return;
        }

        for (Account account : accounts) {

            printAccountSummary(account);
        }
    }

    private void listCustomerAccounts() {

        System.out.println();
        System.out.println("===== CUSTOMER ACCOUNTS =====");

        long customerId =
                InputValidator.requirePositiveLong(
                        prompt("Customer ID: "),
                        "Customer ID"
                );

        List<Account> accounts =
                accountService.getAccountsByCustomerId(
                        customerId
                );

        if (accounts.isEmpty()) {

            System.out.println(
                    "No accounts found for this customer."
            );

            return;
        }

        for (Account account : accounts) {

            printAccountSummary(account);
        }
    }

    private void updateAccount() {

        System.out.println();
        System.out.println("===== UPDATE ACCOUNT =====");

        long accountId =
                InputValidator.requirePositiveLong(
                        prompt("Account ID: "),
                        "Account ID"
                );

        Optional<Account> result =
                accountService.getAccountById(
                        accountId
                );

        if (result.isEmpty()) {

            System.out.println(
                    "Account not found."
            );

            return;
        }

        Account account = result.get();

        /*
         * We are not allowing account type,
         * customer ID, account number, or balance
         * to be changed here.
         *
         * Those are important account properties.
         */

        if (account instanceof SavingsAccount savingsAccount) {

            System.out.println();
            System.out.println(
                    "Current interest rate: " +
                            savingsAccount.getInterestRate()
            );

            String input =
                    prompt(
                            "New interest rate " +
                                    "(press Enter to keep current): "
                    );

            if (!input.isBlank()) {

                BigDecimal interestRate =
                        InputValidator.requirePositiveAmount(
                                input
                        );

                savingsAccount.setInterestRate(
                        interestRate
                );
            }

        } else if (account instanceof CheckingAccount checkingAccount) {

            System.out.println();
            System.out.println(
                    "Current overdraft limit: ₱" +
                            checkingAccount.getOverdraftLimit()
            );

            String input =
                    prompt(
                            "New overdraft limit " +
                                    "(press Enter to keep current): "
                    );

            if (!input.isBlank()) {

                BigDecimal overdraftLimit =
                        InputValidator.requirePositiveAmount(
                                input
                        );

                checkingAccount.setOverdraftLimit(
                        overdraftLimit
                );
            }
        }

        accountService.updateAccount(account);

        System.out.println(
                "Account successfully updated."
        );
    }

    private void closeAccount() {

        System.out.println();
        System.out.println("===== CLOSE ACCOUNT =====");

        long accountId =
                InputValidator.requirePositiveLong(
                        prompt("Account ID: "),
                        "Account ID"
                );

        Optional<Account> result =
                accountService.getAccountById(
                        accountId
                );

        if (result.isEmpty()) {

            System.out.println(
                    "Account not found."
            );

            return;
        }

        Account account = result.get();

        System.out.println(
                "Account Number: " +
                        account.getAccountNumber()
        );

        System.out.println(
                "Current Balance: ₱" +
                        account.getBalance()
        );

        String confirmation =
                prompt(
                        "Are you sure you want to close this account? " +
                                "(yes/no): "
                );

        if (!confirmation.equalsIgnoreCase("yes")) {

            System.out.println(
                    "Account closure cancelled."
            );

            return;
        }

        accountService.closeAccount(accountId);

        System.out.println(
                "Account successfully closed."
        );
    }

    private void printAccount(Account account) {

        System.out.println();
        System.out.println("===== ACCOUNT DETAILS =====");

        System.out.println(
                "Account ID: " +
                        account.getAccountId()
        );

        System.out.println(
                "Customer ID: " +
                        account.getCustomerId()
        );

        System.out.println(
                "Account Number: " +
                        account.getAccountNumber()
        );

        System.out.println(
                "Account Type: " +
                        account.getClass().getSimpleName()
        );

        System.out.println(
                "Balance: ₱" +
                        account.getBalance()
        );

        if (account instanceof SavingsAccount savingsAccount) {

            System.out.println(
                    "Interest Rate: " +
                            savingsAccount.getInterestRate()
            );

        } else if (account instanceof CheckingAccount checkingAccount) {

            System.out.println(
                    "Overdraft Limit: ₱" +
                            checkingAccount.getOverdraftLimit()
            );
        }

        System.out.println(
                "Created At: " +
                        account.getCreatedAt()
        );
    }

    private void printAccountSummary(Account account) {

        System.out.println(
                account.getAccountId()
                        + " | "
                        + account.getAccountNumber()
                        + " | "
                        + account.getClass().getSimpleName()
                        + " | ₱"
                        + account.getBalance()
        );
    }

    private String prompt(String message) {

        System.out.print(message);

        return scanner.nextLine();
    }
}