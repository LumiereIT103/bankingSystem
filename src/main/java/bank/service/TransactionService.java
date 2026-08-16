package bank.service;

import bank.dao.AccountDAO;
import bank.dao.TransactionDAO;
import bank.model.Account;
import bank.model.Transaction;
import bank.model.TransactionType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class TransactionService {

    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    public TransactionService(
            AccountDAO accountDAO,
            TransactionDAO transactionDAO
    ) {
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
    }

    public Transaction deposit(
            String accountNumber,
            BigDecimal amount,
            String referenceNumber
    ) {

        validateAccountNumber(accountNumber);
        validateAmount(amount);
        validateReferenceNumber(referenceNumber);

        Account account =
                getAccountByNumber(accountNumber);

        BigDecimal newBalance =
                account.getBalance().add(amount);

        boolean updated =
                accountDAO.updateBalance(
                        account.getAccountId(),
                        newBalance
                );

        if (!updated) {
            throw new IllegalStateException(
                    "Failed to update account balance."
            );
        }

        Transaction transaction =
                new Transaction();

        transaction.setReferenceNumber(
                referenceNumber
        );

        transaction.setAccountId(
                account.getAccountId()
        );

        transaction.setType(
                TransactionType.DEPOSIT
        );

        transaction.setAmount(amount);

        transaction.setBalanceAfter(
                newBalance
        );

        return transactionDAO.createTransaction(transaction);
    }

    public Transaction withdraw(
            String accountNumber,
            BigDecimal amount,
            String referenceNumber
    ) {

        validateAccountNumber(accountNumber);
        validateAmount(amount);
        validateReferenceNumber(referenceNumber);

        Account account =
                getAccountByNumber(accountNumber);

        BigDecimal currentBalance =
                account.getBalance();

        if (amount.compareTo(currentBalance) > 0) {

            throw new IllegalArgumentException(
                    "Insufficient balance."
            );
        }

        BigDecimal newBalance =
                currentBalance.subtract(amount);

        boolean updated =
                accountDAO.updateBalance(
                        account.getAccountId(),
                        newBalance
                );

        if (!updated) {
            throw new IllegalStateException(
                    "Failed to update account balance."
            );
        }

        Transaction transaction =
                new Transaction();

        transaction.setReferenceNumber(
                referenceNumber
        );

        transaction.setAccountId(
                account.getAccountId()
        );

        transaction.setType(
                TransactionType.WITHDRAW
        );

        transaction.setAmount(amount);

        transaction.setBalanceAfter(
                newBalance
        );

        return transactionDAO.createTransaction(transaction);
    }

    public void transfer(
            String fromAccountNumber,
            String toAccountNumber,
            BigDecimal amount,
            String referenceNumber
    ) {

        validateAccountNumber(fromAccountNumber);
        validateAccountNumber(toAccountNumber);
        validateAmount(amount);
        validateReferenceNumber(referenceNumber);

        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new IllegalArgumentException(
                    "Source and destination accounts must be different."
            );
        }

        Account sender =
                getAccountByNumber(fromAccountNumber);

        Account receiver =
                getAccountByNumber(toAccountNumber);

        if (amount.compareTo(sender.getBalance()) > 0) {
            throw new IllegalArgumentException(
                    "Insufficient balance."
            );
        }

        BigDecimal senderNewBalance =
                sender.getBalance().subtract(amount);

        BigDecimal receiverNewBalance =
                receiver.getBalance().add(amount);

        boolean senderUpdated =
                accountDAO.updateBalance(
                        sender.getAccountId(),
                        senderNewBalance
                );

        if (!senderUpdated) {
            throw new IllegalStateException(
                    "Failed to update sender balance."
            );
        }

        boolean receiverUpdated =
                accountDAO.updateBalance(
                        receiver.getAccountId(),
                        receiverNewBalance
                );

        if (!receiverUpdated) {
            throw new IllegalStateException(
                    "Failed to update receiver balance."
            );
        }

        Transaction outgoing =
                new Transaction();

        outgoing.setReferenceNumber(
                referenceNumber
        );

        outgoing.setAccountId(
                sender.getAccountId()
        );

        outgoing.setType(
                TransactionType.TRANSFER_OUT
        );

        outgoing.setAmount(amount);

        outgoing.setBalanceAfter(
                senderNewBalance
        );

        Transaction incoming =
                new Transaction();

        incoming.setReferenceNumber(
                referenceNumber
        );

        incoming.setAccountId(
                receiver.getAccountId()
        );

        incoming.setType(
                TransactionType.TRANSFER_IN
        );

        incoming.setAmount(amount);

        incoming.setBalanceAfter(
                receiverNewBalance
        );

        transactionDAO.createTransaction(outgoing);
        transactionDAO.createTransaction(incoming);
    }

    public List<Transaction> getTransactionHistory(
            long accountId
    ) {

        if (accountId <= 0) {
            throw new IllegalArgumentException(
                    "Account ID must be greater than zero."
            );
        }

        return transactionDAO
                .findByAccountIdOrderByDateDesc(accountId);
    }

    public List<Transaction> getMiniStatement(
            long accountId,
            int limit
    ) {

        if (accountId <= 0) {
            throw new IllegalArgumentException(
                    "Account ID must be greater than zero."
            );
        }

        if (limit <= 0) {
            throw new IllegalArgumentException(
                    "Limit must be greater than zero."
            );
        }

        return transactionDAO
                .findRecentByAccountId(
                        accountId,
                        limit
                );
    }

    public Optional<Transaction> getTransactionById(
            long transactionId
    ) {

        if (transactionId <= 0) {
            throw new IllegalArgumentException(
                    "Transaction ID must be greater than zero."
            );
        }

        return transactionDAO.findById(
                transactionId
        );
    }

    public Optional<Transaction> getTransactionByReference(
            String referenceNumber
    ) {

        validateReferenceNumber(referenceNumber);

        return transactionDAO
                .findByReferenceNumber(referenceNumber);
    }

    private Account getAccountByNumber(
            String accountNumber
    ) {

        return accountDAO
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Account not found: "
                                        + accountNumber
                        )
                );
    }

    private void validateAmount(
            BigDecimal amount
    ) {

        if (amount == null) {
            throw new IllegalArgumentException(
                    "Amount cannot be null."
            );
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than zero."
            );
        }
    }

    private void validateAccountNumber(
            String accountNumber
    ) {

        if (accountNumber == null
                || accountNumber.isBlank()) {

            throw new IllegalArgumentException(
                    "Account number is required."
            );
        }
    }

    private void validateReferenceNumber(
            String referenceNumber
    ) {

        if (referenceNumber == null
                || referenceNumber.isBlank()) {

            throw new IllegalArgumentException(
                    "Reference number is required."
            );
        }
    }
}