package com.gdb;

import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.command.TransactionCommand;
import com.gdb.logging.FileLogDestination;
import com.gdb.logging.LogDestination;
import com.gdb.logging.TransactionLogger;
import com.gdb.service.AccountService;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Global Digital Bank — Service Demo");
        System.out.println("=".repeat(60));

        // ============================================================
        // 📝 STEP 20: Wire Up Dependencies
        //
        // INSTRUCTIONS:
        //   1. LogDestination dest = new FileLogDestination();
        //   2. TransactionLogger logger = new TransactionLogger(dest);
        //   3. AccountService service = new AccountService(logger);
        // ============================================================
        LogDestination dest = new FileLogDestination();
        TransactionLogger logger = new TransactionLogger(dest);
        AccountService service = new AccountService(logger);

        // Start fresh: clear any persisted file log from previous runs.
        logger.clear();

        // ============================================================
        // 📝 STEP 21: Run A Demo Workflow
        //
        // INSTRUCTIONS:
        //   1. Open accounts, set PINs, perform deposit, withdraw, transfer.
        //   2. Print balances and transaction history.
        // ============================================================
        IAccount john = service.openAccount("SAVINGS", "John Doe", 25, 15000);
        john.setPin(1234);
        IAccount jane = service.openAccount("SAVINGS", "Jane Smith", 30, 10000);
        jane.setPin(5678);

        System.out.println();
        System.out.println("Opened: " + john.getAccountInfo());
        System.out.println("Opened: " + jane.getAccountInfo());

        Transaction txn = service.deposit(john.getAccountNumber(), 5000);
        System.out.println();
        System.out.println("Deposit: " + txn);

        Transaction wtxn = service.withdraw(john.getAccountNumber(), 2000, 1234);
        System.out.println();
        System.out.println("Withdrawal: " + wtxn);

        Transaction ttxn = service.transfer(john.getAccountNumber(), jane.getAccountNumber(), 1000, 1234);
        System.out.println();
        System.out.println("Transfer: " + ttxn);

        System.out.println();
        System.out.println("Final Balances:");
        System.out.println("  John (Account #" + john.getAccountNumber() + "): Rs. " + john.getBalance());
        System.out.println("  Jane (Account #" + jane.getAccountNumber() + "): Rs. " + jane.getBalance());

        System.out.println();
        List<TransactionCommand> history = service.getTransactionHistory();
        System.out.println("Transaction History (" + history.size() + " records):");
        int index = 1;
        for (TransactionCommand cmd : history) {
            System.out.println("  [" + index + "] " + cmd.getTransaction());
            index++;
        }
    }
}