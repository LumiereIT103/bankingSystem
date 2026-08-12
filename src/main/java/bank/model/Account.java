package bank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class Account {
    private Long accountId;
    private Long customerId;
    private String accountNumber;
    private BigDecimal balance;
    private LocalDateTime createdAt;

    protected Account(Long accountId, Long customerId, String accountNumber, BigDecimal balance, LocalDateTime createdAt) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.createdAt = createdAt;
    }
    protected Account(){

    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void deposit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Invalid deposit amount.");
            return;
        }
        balance = balance.add(amount);
    }

    public abstract void withdraw(BigDecimal amount);
}
