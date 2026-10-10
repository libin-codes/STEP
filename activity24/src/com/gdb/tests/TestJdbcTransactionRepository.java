package com.gdb.tests;

import com.gdb.command.*;
import com.gdb.db.*;
import com.gdb.domain.*;
import com.gdb.exceptions.DataAccessException;
import com.gdb.logging.*;
import com.gdb.repository.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public class TestJdbcTransactionRepository {

    public static void main(String[] args) throws Exception {
        System.out.println("=".repeat(60));
        System.out.println("  ACTIVITY 24 — JDBC TRANSACTION REPOSITORY & DATA MIGRATION");
        System.out.println("=".repeat(60));

        String testDbUrl = "jdbc:sqlite:test_txn_gdb.db";
        File dbFile = new File("test_txn_gdb.db");
        if (dbFile.exists()) dbFile.delete();

        ConnectionProvider provider = new JdbcConnectionProvider(testDbUrl);
        SchemaInitializer.initialize(provider);
        JdbcTransactionRepository txnRepo = new JdbcTransactionRepository(provider);
        JdbcAccountRepository accRepo = new JdbcAccountRepository(provider);

        // ============================================================
        // STEP 7: Test Transaction Save & Query by Account
        // ============================================================
        System.out.println("\n[TEST 1] Transaction Save & Query:");
        Transaction deposit = new Transaction(Transaction.generateId(), LocalDateTime.now(), 1001,
                TransactionType.DEPOSIT, 25000.0, 50000.0, "SUCCESS", "Deposit of Rs. 25000.0", 0, 0);
        Transaction withdraw = new Transaction(Transaction.generateId(), LocalDateTime.now(), 1001,
                TransactionType.WITHDRAW, 10000.0, 40000.0, "SUCCESS", "Withdrawal of Rs. 10000.0", 0, 0);
        txnRepo.save(deposit);
        txnRepo.save(withdraw);
        System.out.println("  Saved 2 transaction records.");

        List<Transaction> forAccount = txnRepo.findByAccount(1001);
        System.out.println("  Found transactions for Account #1001: " + forAccount.size());
        assert forAccount.size() == 2 : "Expected 2 transactions for account 1001";
        assert txnRepo.findAll().size() == 2 : "Expected 2 transactions in total";
        passed(1);

        // ============================================================
        // STEP 8: Atomic transfer across accounts
        // ============================================================
        System.out.println("\n[TEST 2] Atomic Transfer Execution:");
        IAccount sender = AccountFactory.createAccount("SAVINGS", 1001, "Rajesh Sharma", 30, 50000.0);
        IAccount receiver = AccountFactory.createAccount("SAVINGS", 1002, "Priya Patel", 28, 30000.0);
        accRepo.save(sender);
        accRepo.save(receiver);
        System.out.println("  Initial Sender Balance: Rs. " + accRepo.findById(1001).getBalance()
                + ", Receiver Balance: Rs. " + accRepo.findById(1002).getBalance());
        System.out.println("  Executing atomic transfer of Rs. 10000.0...");
        txnRepo.transfer(1001, 1002, 10000.0);

        double senderBal = accRepo.findById(1001).getBalance();
        double receiverBal = accRepo.findById(1002).getBalance();
        System.out.println("  Updated Sender Balance: Rs. " + senderBal);
        System.out.println("  Updated Receiver Balance: Rs. " + receiverBal);
        assert senderBal == 40000.0 : "Sender balance should be 40000";
        assert receiverBal == 40000.0 : "Receiver balance should be 40000";
        System.out.println("  Transfer audit transaction recorded in database.");
        assert txnRepo.findByAccount(1001).size() == 3 : "Transfer should add an audit record";
        passed(2);

        // ============================================================
        // STEP 9: Atomic rollback on insufficient balance
        // ============================================================
        System.out.println("\n[TEST 3] Atomic Rollback on Insufficient Balance:");
        System.out.println("  Attempting transfer of Rs. 999999.0 (exceeds balance)...");
        try {
            txnRepo.transfer(1001, 1002, 999999.0);
            assert false : "Expected DataAccessException for insufficient balance";
        } catch (DataAccessException e) {
            System.out.println("  Caught expected exception: " + e.getMessage());
        }
        double senderAfter = accRepo.findById(1001).getBalance();
        double receiverAfter = accRepo.findById(1002).getBalance();
        System.out.println("  Sender Balance unchanged: Rs. " + senderAfter);
        System.out.println("  Receiver Balance unchanged: Rs. " + receiverAfter);
        assert senderAfter == 40000.0 : "Sender balance must be unchanged after rollback";
        assert receiverAfter == 40000.0 : "Receiver balance must be unchanged after rollback";
        passed(3);

        // ============================================================
        // STEP 10: Data Migration from Memory to JDBC
        // ============================================================
        System.out.println("\n[TEST 4] Data Migration from Memory to JDBC:");
        // Migrate accounts: InMemoryAccountRepository -> JdbcAccountRepository
        InMemoryAccountRepository srcAccounts = new InMemoryAccountRepository();
        for (int i = 0; i < 3; i++) {
            srcAccounts.save(AccountFactory.createAccount("SAVINGS", 2001 + i, "Migrated " + i, 30 + i, 15000.0 + i * 1000));
        }
        JdbcAccountRepository destAccounts = new JdbcAccountRepository(provider);
        List<IAccount> sourceAccounts = srcAccounts.findAll();
        for (IAccount acc : sourceAccounts) {
            destAccounts.save(acc);
        }

        // Migrate transactions: MemoryLogDestination -> JdbcTransactionRepository
        MemoryLogDestination sourceLog = new MemoryLogDestination();
        IAccount loggerAccount = AccountFactory.createAccount("SAVINGS", 3001, "Log Source", 30, 50000.0);
        TransactionLogger logger = new TransactionLogger(sourceLog);
        for (int i = 0; i < 5; i++) {
            DepositCommand cmd = new DepositCommand(loggerAccount, 1000.0 + i * 100);
            cmd.execute();
            logger.log(cmd);
        }

        JdbcTransactionRepository destTxn = new JdbcTransactionRepository(provider);
        destTxn.clear();
        int migrated = DataMigrator.migrate(sourceLog, destTxn);
        System.out.println("  Migrated " + sourceAccounts.size() + " accounts and " + migrated + " transaction records.");
        assert sourceAccounts.size() == 3 : "Expected 3 migrated accounts";
        assert migrated == 5 : "Expected 5 migrated transactions";
        assert destTxn.findAll().size() == 5 : "Expected 5 transactions persisted in DB";
        System.out.println("  Verified migrated accounts in SQLite database.");
        assert destAccounts.findById(2001) != null : "Migrated account 2001 should exist";
        passed(4);

        provider.shutdown();
        if (dbFile.exists()) dbFile.delete();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("  ALL ACTIVITY 24 JDBC TRANSACTION TESTS PASSED!");
        System.out.println("=".repeat(60));
    }

    private static void passed(int testNo) {
        System.out.println("  -> PASSED");
    }
}
