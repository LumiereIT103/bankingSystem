package bank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Account {
    private long accountId;
    private String accountNumber;
    private String accountName;
    private byte accountPin;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Account(){

    }

        public Account(String accountNumber, String accountName, byte accountPin, BigDecimal balance){
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.accountPin = accountPin;
        this.balance = balance;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public byte getAccountPin() {
        return accountPin;
    }

    public void setAccountPin(byte accountPin) {
        this.accountPin = accountPin;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


}
