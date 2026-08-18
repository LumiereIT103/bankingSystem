package bank.dao;

import bank.model.Transaction;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface TransactionDAO {
    Transaction createTransaction(Transaction transaction);

    Transaction createTransaction(
            Connection connection,
            Transaction transaction
    );
    Optional<Transaction> findById(long transactionId);

    Optional<Transaction> findByReferenceNumber(
            String referenceNumber
    );

    List<Transaction> findByAccountId(
            long accountId
    );

    List<Transaction> findByAccountIdOrderByDateDesc(
            long accountId
    );

    List<Transaction> findRecentByAccountId(
            long accountId,
            int limit
    );
}
