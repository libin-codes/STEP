package com.gdb.tests;

import com.gdb.command.*;
import com.gdb.domain.*;
import com.gdb.logging.TransactionLog;
import java.util.List;

public class TestCommandLogging {
    public static void main(String[] args) throws Exception {
        System.out.println("=".repeat(60));
        System.out.println("  ACTIVITY 17 — COMMAND PATTERN + FILE LOGGING");
        System.out.println("=".repeat(60));

        TransactionLog log = new TransactionLog();
        log.clear(); // start fresh

        // ============================================================
        // 📝 STEP 10: Create Test Accounts
        //
        // INSTRUCTIONS:
        //   1. acc1 = AccountFactory.createAccount("SAVINGS", 1001, "John Doe", 25, 15000);
        //   2. acc1.setPin(1234);
        //   3. acc2 = AccountFactory.createAccount("SAVINGS", 1002, "Jane Smith", 30, 10000);
        // ============================================================
        IAccount acc1 = AccountFactory.createAccount("SAVINGS", 1001, "John Doe", 25, 15000);
        acc1.setPin(1234);
        IAccount acc2 = AccountFactory.createAccount("SAVINGS", 1002, "Jane Smith", 30, 10000);

        // ============================================================
        // 📝 STEP 11: Execute and Log DepositCommand
        //
        // INSTRUCTIONS:
        //   1. Create DepositCommand depCmd = new DepositCommand(acc1, 5000);
        //   2. depCmd.execute();
        //   3. log.log(depCmd);
        //   4. Print depCmd.getTransaction();
        // ============================================================
        DepositCommand depCmd = new DepositCommand(acc1, 5000);
        depCmd.execute();
        log.log(depCmd);
        System.out.println("[STEP 11] Logged: " + depCmd.getTransaction());

        // ============================================================
        // 📝 STEP 12: Execute and Log WithdrawCommand
        //
        // INSTRUCTIONS:
        //   1. Create WithdrawCommand wthCmd = new WithdrawCommand(acc1, 2000, 1234);
        //   2. wthCmd.execute();
        //   3. log.log(wthCmd);
        //   4. Print wthCmd.getTransaction();
        // ============================================================
        WithdrawCommand wthCmd = new WithdrawCommand(acc1, 2000, 1234);
        wthCmd.execute();
        log.log(wthCmd);
        System.out.println("[STEP 12] Logged: " + wthCmd.getTransaction());

        // ============================================================
        // 📝 STEP 13: Execute and Log TransferCommand
        //
        // INSTRUCTIONS:
        //   1. Create TransferCommand trfCmd = new TransferCommand(acc1, acc2, 3000, 1234);
        //   2. trfCmd.execute();
        //   3. log.log(trfCmd);
        //   4. Print trfCmd.getTransaction();
        // ============================================================
        TransferCommand trfCmd = new TransferCommand(acc1, acc2, 3000, 1234);
        trfCmd.execute();
        log.log(trfCmd);
        System.out.println("[STEP 13] Logged: " + trfCmd.getTransaction());

        // ============================================================
        // 📝 STEP 14: Read All Commands Back
        //
        // INSTRUCTIONS:
        //   1. List<TransactionCommand> history = log.readAll();
        //   2. Print history size and iterate printing each cmd.getTransaction();
        // ============================================================
        List<TransactionCommand> history = log.readAll();
        System.out.println();
        System.out.println("[STEP 14] Read " + history.size() + " commands from transaction log:");
        for (int i = 0; i < history.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + history.get(i).getTransaction());
        }

        // ============================================================
        // 📝 STEP 15: Verify File Persistence
        //
        // INSTRUCTIONS:
        //   1. TransactionLog freshLog = new TransactionLog();
        //   2. List<TransactionCommand> persisted = freshLog.readAll();
        //   3. Verify persisted.size() matches previous count.
        // ============================================================
        TransactionLog freshLog = new TransactionLog();
        List<TransactionCommand> persisted = freshLog.readAll();
        System.out.println();
        if (persisted.size() == history.size()) {
            System.out.println("[STEP 15] Fresh reader verified " + persisted.size() + " persisted commands [PASS]");
        } else {
            System.out.println("[STEP 15] Persistence check FAILED: expected " + history.size() + " but read " + persisted.size());
        }
    }
}
