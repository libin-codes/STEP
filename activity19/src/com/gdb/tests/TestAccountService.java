package com.gdb.tests;

import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.command.TransactionCommand;
import com.gdb.logging.*;
import com.gdb.service.AccountService;
import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InvalidPinException;
import java.util.List;

public class TestAccountService {
    public static void main(String[] args) throws Exception {
        System.out.println("=".repeat(60));
        System.out.println("  ACTIVITY 19 — ACCOUNT SERVICE DEMO");
        System.out.println("=".repeat(60));

        // ============================================================
        // 📝 STEP 12: Set Up The Service
        //
        // INSTRUCTIONS:
        //   1. LogDestination dest = new MemoryLogDestination();
        //   2. TransactionLogger logger = new TransactionLogger(dest);
        //   3. AccountService service = new AccountService(logger);
        // ============================================================
        LogDestination dest = new MemoryLogDestination();
        TransactionLogger logger = new TransactionLogger(dest);
        AccountService service = new AccountService(logger);

        // ============================================================
        // 📝 STEP 13: Open Two Accounts
        //
        // INSTRUCTIONS:
        //   1. IAccount john = service.openAccount("SAVINGS", "John Doe", 25, 15000);
        //   2. john.setPin(1234);
        //   3. IAccount jane = service.openAccount("SAVINGS", "Jane Smith", 30, 10000);
        //   4. jane.setPin(5678);
        //   5. Print both accounts using getAccountInfo().
        // ============================================================
        IAccount john = service.openAccount("SAVINGS", "John Doe", 25, 15000);
        john.setPin(1234);
        IAccount jane = service.openAccount("SAVINGS", "Jane Smith", 30, 10000);
        jane.setPin(5678);

        System.out.println();
        System.out.println("[STEP 13] Opened: " + john.getAccountInfo());
        System.out.println("[STEP 13] Opened: " + jane.getAccountInfo());

        // ============================================================
        // 📝 STEP 14: Deposit into John's Account
        //
        // INSTRUCTIONS:
        //   1. Transaction txn = service.deposit(john.getAccountNumber(), 5000);
        //   2. Print txn.
        // ============================================================
        System.out.println();
        Transaction txn = service.deposit(john.getAccountNumber(), 5000);
        System.out.println("[STEP 14] Deposit: " + txn);

        // ============================================================
        // 📝 STEP 15: Withdraw from John's Account
        //
        // INSTRUCTIONS:
        //   1. Transaction wtxn = service.withdraw(john.getAccountNumber(), 2000, 1234);
        //   2. Print wtxn.
        // ============================================================
        System.out.println();
        Transaction wtxn = service.withdraw(john.getAccountNumber(), 2000, 1234);
        System.out.println("[STEP 15] Withdrawal: " + wtxn);

        // ============================================================
        // 📝 STEP 16: Transfer from John to Jane
        //
        // INSTRUCTIONS:
        //   1. Transaction ttxn = service.transfer(john.getAccountNumber(), jane.getAccountNumber(), 1000, 1234);
        //   2. Print ttxn.
        // ============================================================
        System.out.println();
        Transaction ttxn = service.transfer(john.getAccountNumber(), jane.getAccountNumber(), 1000, 1234);
        System.out.println("[STEP 16] Transfer: " + ttxn);

        // ============================================================
        // 📝 STEP 17: Print Final Balances
        //
        // INSTRUCTIONS:
        //   Print john.getBalance() and jane.getBalance().
        // ============================================================
        System.out.println();
        System.out.println("[STEP 17] Final Balances:");
        System.out.println("  John (Account #" + john.getAccountNumber() + "): Rs. " + john.getBalance());
        System.out.println("  Jane (Account #" + jane.getAccountNumber() + "): Rs. " + jane.getBalance());

        // ============================================================
        // 📝 STEP 18: Print Transaction History
        //
        // INSTRUCTIONS:
        //   1. List<TransactionCommand> history = service.getTransactionHistory();
        //   2. Print size and each command's transaction.
        // ============================================================
        System.out.println();
        List<TransactionCommand> history = service.getTransactionHistory();
        System.out.println("[STEP 18] Transaction History (" + history.size() + " records):");
        int index = 1;
        for (TransactionCommand cmd : history) {
            System.out.println("  [" + index + "] " + cmd.getTransaction());
            index++;
        }

        // ============================================================
        // 📝 STEP 19: Test Error Cases
        //
        // INSTRUCTIONS:
        //   1. Try deposit to non-existent account 9999 -> catch AccountException.
        //   2. Try withdraw with wrong PIN -> catch InvalidPinException.
        // ============================================================
        System.out.println();
        System.out.println("[STEP 19] Error Handling Checks:");
        try {
            service.deposit(9999, 100);
        } catch (AccountException e) {
            System.out.println("  Deposit to missing account caught: " + e.getMessage() + " [PASS]");
        }
        try {
            service.withdraw(1001, 2000, 9999);
        } catch (InvalidPinException e) {
            System.out.println("  Withdrawal with wrong PIN caught: " + e.getMessage() + " [PASS]");
        }
    }
}