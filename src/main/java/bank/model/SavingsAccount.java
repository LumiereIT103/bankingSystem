package bank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SavingsAccount extends Account {
    private BigDecimal interestRate;

    public SavingsAccount(){
        super();
    }
    public SavingsAccount(Long accountId, Long customerId,
                          String accountNumber, BigDecimal balance,
                          LocalDateTime createdAt,
                          BigDecimal interestRate) {
        super(accountId, customerId, accountNumber, balance, createdAt);
        this.interestRate = interestRate;
    }

    @Override
    public void withdraw(BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Withdrawal amount must be greater than zero."
            );
        }

        if (amount.compareTo(getBalance()) > 0) {
            throw new IllegalArgumentException(
                    "Insufficient balance."
            );
        }

        setBalance(getBalance().subtract(amount));
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
