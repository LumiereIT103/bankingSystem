package bank.dao;

import bank.model.Account;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface AccountDAO {
    Account createAccount(Account account);

    Optional<Account> findById(long accountId);

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findAll();

    List<Account> findByCustomerId(long customerId);

    boolean update(Account account);

    boolean updateBalance(
            long accountId,
            BigDecimal newBalance
    );

    boolean updateBalance(
            Connection connection,
            long accountId,
            BigDecimal newBalance
    );

    boolean deleteById(long accountId);
}
