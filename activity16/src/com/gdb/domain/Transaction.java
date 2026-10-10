package com.gdb.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Model class representing a financial transaction in the GDB banking system.
 */
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private static long counter = 0;

    // ============================================================
    // 📝 STEP 2: Declare Transaction Fields
    //
    // INSTRUCTIONS:
    //   1. transactionId (String) - Unique identifier for the transaction
    //   2. timestamp (LocalDateTime) - Timestamp when transaction occurred
    //   3. accountNumber (int) - Account on which transaction was performed
    //   4. type (TransactionType) - DEPOSIT, WITHDRAW, or TRANSFER
    //   5. amount (double) - Monetary value of the transaction
    //   6. balanceAfter (double) - Account balance after transaction execution
    //   7. status (String) - "SUCCESS" or "FAILED"
    //   8. description (String) - Human-readable summary
    //   9. fromAccount (int) - Source account (0 if not a TRANSFER)
    //   10. toAccount (int) - Destination account (0 if not a TRANSFER)
    // ============================================================
    private String transactionId;
    private LocalDateTime timestamp;
    private int accountNumber;
    private TransactionType type;
    private double amount;
    private double balanceAfter;
    private String status;
    private String description;
    private int fromAccount;
    private int toAccount;

    public Transaction() {
    }

    // ============================================================
    // 📝 STEP 3: Constructor With All Fields
    //
    // INSTRUCTIONS:
    //   Initialize all instance variables from constructor parameters.
    // ============================================================
    // TODO: implement all-arguments constructor
    public Transaction(String transactionId, LocalDateTime timestamp, int accountNumber,
                       TransactionType type, double amount, double balanceAfter,
                       String status, String description, int fromAccount, int toAccount) {
        this.transactionId = transactionId;
        this.timestamp = timestamp;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.status = status;
        this.description = description;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    // ============================================================
    // 📝 STEP 4: Getters and Setters
    //
    // INSTRUCTIONS:
    //   Provide standard accessors and mutators for all fields.
    // ============================================================
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public int getAccountNumber() { return accountNumber; }
    public void setAccountNumber(int accountNumber) { this.accountNumber = accountNumber; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public double getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(double balanceAfter) { this.balanceAfter = balanceAfter; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getFromAccount() { return fromAccount; }
    public void setFromAccount(int fromAccount) { this.fromAccount = fromAccount; }

    public int getToAccount() { return toAccount; }
    public void setToAccount(int toAccount) { this.toAccount = toAccount; }

    // ============================================================
    // 📝 STEP 5: toString Display Format
    //
    // INSTRUCTIONS:
    //   Format transaction into a readable summary string.
    // ============================================================
    // TODO: implement toString() method
    @Override
    public String toString() {
        return "[" + transactionId + "] " + type + " | Rs. " + amount +
               " | Balance After: Rs. " + balanceAfter +
               " | Status: " + status + " | " + description;
    }

    // ============================================================
    // 📝 STEP 6: Static Helper generateId()
    //
    // INSTRUCTIONS:
    //   Generate a unique transaction identifier string:
    //   "TXN-" + System.currentTimeMillis() + "-" + (++counter)
    // ============================================================
    // TODO: generate unique transaction ID
    public static synchronized String generateId() {
        return "TXN-" + System.currentTimeMillis() + "-" + (++counter);
    }
}
