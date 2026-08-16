package bank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CheckingAccount extends Account{
    private BigDecimal overdraftLimit;

    public BigDecimal getOverdraftLimit() {
        return overdraftLimit;
    }
    public void setOverdraftLimit(BigDecimal overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }
    public CheckingAccount(Long accountId, Long customerId,
                          String accountNumber, BigDecimal balance,
                          LocalDateTime createdAt,
                          BigDecimal interestRate) {
        super(accountId, customerId, accountNumber, balance, createdAt);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Withdrawal amount must be greater than zero."
            );
        }

        BigDecimal minimumAllowedBalance =
                overdraftLimit.negate();

        BigDecimal resultingBalance =
                getBalance().subtract(amount);

        if (resultingBalance.compareTo(minimumAllowedBalance) < 0) {
            throw new IllegalArgumentException(
                    "Withdrawal exceeds overdraft limit."
            );
        }

        setBalance(resultingBalance);
    }
}
