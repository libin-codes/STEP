package com.gdb.domain;

import com.gdb.exceptions.*;

public class CurrentAccount extends AbstractAccount {
    private double overdraftLimit;

    public CurrentAccount(String accountNumber, String name, int age, double balance, String status, String pin, double overdraftLimit) {
        super(accountNumber, name, age, balance, "CURRENT", status, pin);
        this.overdraftLimit = overdraftLimit;
    }

    // Current debit rule:
    //   1. If amount > (balance + overdraftLimit) -> throw new InsufficientBalanceException("Overdraft limit exceeded")
    //   2. Otherwise subtract amount from balance (the balance may go negative, down to -overdraftLimit).
    @Override
    public void processDebit(double amount) throws AccountException {
        if (amount > (balance + overdraftLimit)) {
            throw new InsufficientBalanceException("Overdraft limit exceeded");
        }
        this.balance -= amount;
    }

    public double getOverdraftLimit() { return overdraftLimit; }
    public void setOverdraftLimit(double overdraftLimit) { this.overdraftLimit = overdraftLimit; }
}
