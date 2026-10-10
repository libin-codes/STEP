package com.gdb.command;

import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.service.TransferService;

public class TransferCommand implements TransactionCommand {
    private static final long serialVersionUID = 1L;

    // ============================================================
    // 📝 STEP 3: Declare Fields
    //
    // INSTRUCTIONS:
    //   1. private IAccount fromAccount;
    //   2. private IAccount toAccount;
    //   3. private double amount;
    //   4. private int pin;
    //   5. private Transaction transaction;
    // ============================================================
    private final IAccount fromAccount;
    private final IAccount toAccount;
    private final double amount;
    private final int pin;
    private Transaction transaction;

    // ============================================================
    // 📝 STEP 4: Write Constructor
    //
    // INSTRUCTIONS:
    //   Accept (IAccount from, IAccount to, double amount, int pin); store all.
    // ============================================================
    public TransferCommand(IAccount from, IAccount to, double amount, int pin) {
        this.fromAccount = from;
        this.toAccount = to;
        this.amount = amount;
        this.pin = pin;
    }

    // ============================================================
    // 📝 STEP 5: Implement execute()
    //
    // INSTRUCTIONS:
    //   1. Call new TransferService().transferWithTransaction(fromAccount, toAccount, amount, pin).
    //   2. Store returned Transaction in this.transaction.
    // ============================================================
    @Override
    public void execute() throws Exception {
        this.transaction = new TransferService().transferWithTransaction(fromAccount, toAccount, amount, pin);
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
