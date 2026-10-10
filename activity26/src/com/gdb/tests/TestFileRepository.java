package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.repository.*;

import java.io.File;
import java.io.FilenameFilter;
import java.time.LocalDateTime;
import java.util.List;

public class TestFileRepository {

    public static void main(String[] args) throws Exception {
        System.out.println("=".repeat(60));
        System.out.println("  ACTIVITY 26 — FILE REPOSITORY (CSV + ATOMIC WRITES + BACKUP)");
        System.out.println("=".repeat(60));

        String accPath = "test_data/accounts.csv";
        String txnPath = "test_data/transactions.csv";

        // Clean test files
        deleteTestData("test_data");

        // ============================================================
        // STEP 18: Test FileAccountRepository CRUD & Disk Persistence
        // ============================================================
        System.out.println("\n[TEST 1] Account Save & Read Back from CSV:");
        FileAccountRepository repo = new FileAccountRepository(accPath, true, 1001, 2);
        IAccount acc1 = AccountFactory.createAccount("SAVINGS", 1001, "Rajesh Sharma", 30, 50000.0);
        acc1.setPin(1234);
        repo.save(acc1);
        System.out.println("  Saved: " + acc1.getAccountInfo());

        // Re-open the repository so the account is reconstructed from disk (CSV round-trip).
        FileAccountRepository reloaded = new FileAccountRepository(accPath, true, 1001, 2);
        IAccount found = reloaded.findById(1001);
        System.out.println("  Found: " + found.getAccountInfo());
        assert found != null : "Account 1001 should be read back from CSV";
        assert found.getAccountNumber() == 1001 : "Account number should round-trip";
        assert "Rajesh Sharma".equals(found.getAccountHolderName()) : "Name should round-trip";
        assert found.getBalance() == 50000.0 : "Balance should round-trip";
        assert found.verifyPin(1234) : "PIN should round-trip";
        passed();

        // ============================================================
        // STEP 19: Multiple Accounts & Atomic Update
        // ============================================================
        System.out.println("\n[TEST 2] Multiple Accounts & Atomic Update:");
        IAccount acc2 = AccountFactory.createAccount("CURRENT", 1002, "Priya Patel", 28, 40000.0);
        reloaded.save(acc2);

        IAccount update = reloaded.findById(1001);
        update.deposit(10000.0);
        reloaded.update(update);

        IAccount updated = reloaded.findById(1001);
        System.out.println("  Updated balance: Rs. " + updated.getBalance() + " (Expected: 60000.0)");
        assert updated.getBalance() == 60000.0 : "Balance should be updated on disk";

        List<IAccount> allAccounts = reloaded.findAll();
        System.out.println("  Total accounts in CSV: " + allAccounts.size() + " (Expected: 2)");
        assert allAccounts.size() == 2 : "Should have 2 accounts";
        passed();

        // ============================================================
        // STEP 20: Test FileTransactionRepository & CSV Logging
        // ============================================================
        System.out.println("\n[TEST 3] Transaction Save & Query in CSV:");
        FileTransactionRepository txnRepo = new FileTransactionRepository(txnPath);
        txnRepo.save(new Transaction(Transaction.generateId(), LocalDateTime.now(), 1001,
                TransactionType.DEPOSIT, 10000.0, 60000.0, "SUCCESS", "Deposit of Rs. 10000.0", 0, 0));
        txnRepo.save(new Transaction(Transaction.generateId(), LocalDateTime.now(), 1001,
                TransactionType.WITHDRAW, 5000.0, 55000.0, "SUCCESS", "Withdrawal of Rs. 5000.0", 0, 0));
        txnRepo.save(new Transaction(Transaction.generateId(), LocalDateTime.now(), 1002,
                TransactionType.DEPOSIT, 5000.0, 45000.0, "SUCCESS", "Deposit of Rs. 5000.0", 0, 0));

        int for1001 = txnRepo.findByAccount(1001).size();
        int for1002 = txnRepo.findByAccount(1002).size();
        System.out.println("  Found transactions for #1001: " + for1001 + " (Expected: 2)");
        System.out.println("  Found transactions for #1002: " + for1002 + " (Expected: 1)");
        assert for1001 == 2 : "Account 1001 should have 2 transactions";
        assert for1002 == 1 : "Account 1002 should have 1 transaction";

        // Verify the CSV file survives a reload.
        FileTransactionRepository txnReloaded = new FileTransactionRepository(txnPath);
        assert txnReloaded.findAll().size() == 3 : "Transactions should round-trip through CSV";
        passed();

        // ============================================================
        // STEP 21: Atomic Transfer via FileAccountRepository
        // ============================================================
        System.out.println("\n[TEST 4] Atomic Transfer via FileAccountRepository:");
        IAccount sender = reloaded.findById(1001);
        IAccount receiver = reloaded.findById(1002);
        sender.withdraw(5000.0, 1234);
        receiver.deposit(5000.0);
        reloaded.update(sender);
        reloaded.update(receiver);

        double senderBal = reloaded.findById(1001).getBalance();
        double receiverBal = reloaded.findById(1002).getBalance();
        System.out.println("  Sender balance: Rs. " + senderBal + " (Expected: 55000.0)");
        System.out.println("  Receiver balance: Rs. " + receiverBal + " (Expected: 45000.0)");
        assert senderBal == 55000.0 : "Sender balance should be 55000";
        assert receiverBal == 45000.0 : "Receiver balance should be 45000";

        // The transfer should also be representable in the file-ledger.
        txnRepo.transfer(1001, 1002, 5000.0);
        assert txnRepo.findByAccount(1001).size() >= 3 : "Transfer should be logged";
        passed();

        // ============================================================
        // STEP 22: Verify Timestamped Backups Created
        // ============================================================
        System.out.println("\n[TEST 5] Verify Timestamped Backups Created:");
        File dir = new File("test_data");
        String prefix = "accounts.csv.bak.";
        File[] backups = dir.listFiles((FilenameFilter) (d, name) -> name.startsWith(prefix));
        int backupCount = backups == null ? 0 : backups.length;
        System.out.println("  Found " + backupCount + " backup files for accounts.csv.");
        assert backupCount >= 2 : "At least 2 timestamped backups should exist";
        passed();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("  ALL ACTIVITY 26 FILE REPOSITORY TESTS PASSED!");
        System.out.println("=".repeat(60));
    }

    private static void deleteTestData(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            return;
        }
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
        dir.delete();
    }

    private static void passed() {
        System.out.println("  -> PASSED");
    }
}
