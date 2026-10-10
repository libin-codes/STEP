package com.gdb.command;

import com.gdb.domain.Account;
import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;

public class WithdrawCommand implements TransactionCommand {
    private static final long serialVersionUID = 1L;

    // ============================================================
    // 📝 STEP 3: Declare Fields
    //
    // INSTRUCTIONS:
    //   1. private IAccount account;
    //   2. private double amount;
    //   3. private int pin;
    //   4. private Transaction transaction;
    // ============================================================
    private final IAccount account;
    private final double amount;
    private final int pin;
    private Transaction transaction;

    // ============================================================
    // 📝 STEP 4: Write Constructor
    //
    // INSTRUCTIONS:
    //   Accept (IAccount account, double amount, int pin); store all.
    // ============================================================
    public WithdrawCommand(IAccount account, double amount, int pin) {
        this.account = account;
        this.amount = amount;
        this.pin = pin;
    }

    // ============================================================
    // 📝 STEP 5: Implement execute()
    //
    // INSTRUCTIONS:
    //   1. Call account.withdrawWithTransaction(amount, pin).
    //   2. Store returned Transaction in this.transaction.
    // ============================================================
    @Override
    public void execute() throws Exception {
        this.transaction = ((Account) account).withdrawWithTransaction(amount, pin);
    }

    // ============================================================
    // 📝 STEP 6: Implement getTransaction()
    //
    // INSTRUCTIONS:
    //   Return this.transaction.
    // ============================================================
    @Override
    public Transaction getTransaction() {
        return transaction;
    }
}
