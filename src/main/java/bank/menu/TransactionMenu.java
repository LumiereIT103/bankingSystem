package bank.menu;

import bank.exception.AccountNotFoundException;
import bank.exception.InsufficientBalanceException;
import bank.exception.InvalidTransactionException;
import bank.model.Transaction;
import bank.service.TransactionService;
import bank.util.InputValidator;
import bank.util.ReferenceNumberGenerator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class TransactionMenu {

    private final TransactionService transactionService;
    private final Scanner scanner;

    public TransactionMenu(
            TransactionService transactionService,
            Scanner scanner
    ) {
        this.transactionService = transactionService;
        this.scanner = scanner;
    }

    public void show() {

        while (true) {

            System.out.println();
            System.out.println("================================");
            System.out.println("          TRANSACTIONS");
            System.out.println("================================");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Transfer");
            System.out.println("4. Transaction History");
            System.out.println("5. Mini Statement");
            System.out.println("6. Find Transaction");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            try {

                switch (choice) {

                    case "1" -> deposit();

                    case "2" -> withdraw();

                    case "3" -> transfer();

                    case "4" -> transactionHistory();

                    case "5" -> miniStatement();

                    case "6" -> findTransaction();

                    case "0" -> {
                        return;
                    }

                    default ->
                            System.out.println(
                                    "Invalid choice."
                            );
                }

            } catch (AccountNotFoundException |
                     InsufficientBalanceException |
                     InvalidTransactionException e) {

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

    private void deposit() {

        System.out.println();
        System.out.println("===== DEPOSIT =====");

        String accountNumber =
                InputValidator.requireText(
                        prompt("Account Number: "),
                        "Account number"
                );

        BigDecimal amount =
                InputValidator.requirePositiveAmount(
                        prompt("Amount: ₱")
                );

        String referenceNumber =
                ReferenceNumberGenerator
                        .generateTransactionReference();

        Transaction transaction =
                transactionService.deposit(
                        accountNumber,
                        amount,
                        referenceNumber
                );

        System.out.println();
        System.out.println(
                "Deposit successful."
        );

        System.out.println(
                "Reference Number: " +
                        transaction.getReferenceNumber()
        );

        System.out.println(
                "Amount: ₱" +
                        transaction.getAmount()
        );

        System.out.println(
                "New Balance: ₱" +
                        transaction.getBalanceAfter()
        );
    }

    private void withdraw() {

        System.out.println();
        System.out.println("===== WITHDRAW =====");

        String accountNumber =
                InputValidator.requireText(
                        prompt("Account Number: "),
                        "Account number"
                );

        BigDecimal amount =
                InputValidator.requirePositiveAmount(
                        prompt("Amount: ₱")
                );

        String referenceNumber =
                ReferenceNumberGenerator
                        .generateTransactionReference();

        Transaction transaction =
                transactionService.withdraw(
                        accountNumber,
                        amount,
                        referenceNumber
                );

        System.out.println();
        System.out.println(
                "Withdrawal successful."
        );

        System.out.println(
                "Reference Number: " +
                        transaction.getReferenceNumber()
        );

        System.out.println(
                "Amount: ₱" +
                        transaction.getAmount()
        );

        System.out.println(
                "New Balance: ₱" +
                        transaction.getBalanceAfter()
        );
    }

    private void transfer() {

        System.out.println();
        System.out.println("===== TRANSFER =====");

        String fromAccount =
                InputValidator.requireText(
                        prompt("From Account Number: "),
                        "Source account number"
                );

        String toAccount =
                InputValidator.requireText(
                        prompt("To Account Number: "),
                        "Destination account number"
                );

        BigDecimal amount =
                InputValidator.requirePositiveAmount(
                        prompt("Amount: ₱")
                );

        System.out.println();
        System.out.println("Transfer Summary");
        System.out.println("----------------------------");
        System.out.println(
                "From: " + fromAccount
        );
        System.out.println(
                "To: " + toAccount
        );
        System.out.println(
                "Amount: ₱" + amount
        );

        String confirmation =
                prompt(
                        "Confirm transfer? (yes/no): "
                );

        if (!confirmation.equalsIgnoreCase("yes")) {

            System.out.println(
                    "Transfer cancelled."
            );

            return;
        }

        String transferReference =
                ReferenceNumberGenerator
                        .generateTransferReference();

        /*
         * TransactionService will use this transfer
         * reference for both TRANSFER_OUT and
         * TRANSFER_IN transaction records.
         */
        transactionService.transfer(
                fromAccount,
                toAccount,
                amount,
                transferReference
        );

        System.out.println();
        System.out.println(
                "Transfer successful."
        );

        System.out.println(
                "Transfer Reference: " +
                        transferReference
        );
    }

    private void transactionHistory() {

        System.out.println();
        System.out.println(
                "===== TRANSACTION HISTORY ====="
        );

        long accountId =
                InputValidator.requirePositiveLong(
                        prompt("Account ID: "),
                        "Account ID"
                );

        List<Transaction> transactions =
                transactionService
                        .getTransactionHistory(
                                accountId
                        );

        if (transactions.isEmpty()) {

            System.out.println(
                    "No transactions found."
            );

            return;
        }

        for (Transaction transaction :
                transactions) {

            printTransaction(transaction);
        }
    }

    private void miniStatement() {

        System.out.println();
        System.out.println(
                "===== MINI STATEMENT ====="
        );

        long accountId =
                InputValidator.requirePositiveLong(
                        prompt("Account ID: "),
                        "Account ID"
                );

        int limit =
                InputValidator.requirePositiveInt(
                        prompt("Number of transactions: "),
                        "Transaction limit"
                );

        List<Transaction> transactions =
                transactionService
                        .getMiniStatement(
                                accountId,
                                limit
                        );

        if (transactions.isEmpty()) {

            System.out.println(
                    "No transactions found."
            );

            return;
        }

        for (Transaction transaction :
                transactions) {

            printTransaction(transaction);
        }
    }

    private void findTransaction() {

        System.out.println();
        System.out.println(
                "===== FIND TRANSACTION ====="
        );

        System.out.println("1. Find by Transaction ID");
        System.out.println("2. Find by Reference Number");
        System.out.println("0. Back");
        System.out.print("Choose: ");

        String choice =
                scanner.nextLine();

        switch (choice) {

            case "1" -> findTransactionById();

            case "2" -> findTransactionByReference();

            case "0" -> {
                return;
            }

            default ->
                    System.out.println(
                            "Invalid choice."
                    );
        }
    }

    private void findTransactionById() {

        long transactionId =
                InputValidator.requirePositiveLong(
                        prompt("Transaction ID: "),
                        "Transaction ID"
                );

        Optional<Transaction> result =
                transactionService
                        .getTransactionById(
                                transactionId
                        );

        if (result.isEmpty()) {

            System.out.println(
                    "Transaction not found."
            );

            return;
        }

        printTransaction(result.get());
    }

    private void findTransactionByReference() {

        String referenceNumber =
                InputValidator.requireText(
                        prompt("Reference Number: "),
                        "Reference number"
                );

        Optional<Transaction> result =
                transactionService
                        .getTransactionByReference(
                                referenceNumber
                        );

        if (result.isEmpty()) {

            System.out.println(
                    "Transaction not found."
            );

            return;
        }

        printTransaction(result.get());
    }

    private void printTransaction(
            Transaction transaction
    ) {

        System.out.println();
        System.out.println("----------------------------");

        System.out.println(
                "Transaction ID: " +
                        transaction.getTransactionId()
        );

        System.out.println(
                "Reference: " +
                        transaction.getReferenceNumber()
        );

        System.out.println(
                "Type: " +
                        transaction.getType()
        );

        System.out.println(
                "Amount: ₱" +
                        transaction.getAmount()
        );

        System.out.println(
                "Balance After: ₱" +
                        transaction.getBalanceAfter()
        );

        System.out.println(
                "Created: " +
                        transaction.getCreatedAt()
        );
    }

    private String prompt(String message) {

        System.out.print(message);

        return scanner.nextLine();
    }
}