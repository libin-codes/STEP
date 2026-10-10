package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.repository.*;
import com.gdb.service.AccountService;
import com.gdb.logging.*;
import java.time.LocalDateTime;
import java.util.List;

public class TestRepositoryInMemory {
    public static void main(String[] args) throws Exception {
        System.out.println("=".repeat(60));
        System.out.println("  ACTIVITY 21 — REPOSITORY PATTERN (IN-MEMORY)");
        System.out.println("=".repeat(60));

        AccountRepository accountRepo = new InMemoryAccountRepository();
        TransactionRepository txnRepo = new InMemoryTransactionRepository();
        TransactionLogger logger = new TransactionLogger(new MemoryLogDestination());

        AccountService service = new AccountService(accountRepo, txnRepo, logger);

        // ============================================================
        // 📝 STEP 17: Test Account Repository CRUD
        // ============================================================
        System.out.println("\n--- STEP 17: Account Repository CRUD ---");
        boolean step17 = true;
        try {
            // Auto-incrementing ID generation starts at 1001
            int firstNum = accountRepo.nextAccountNumber();
            step17 &= (firstNum == 1001);

            IAccount rajesh = AccountFactory.createAccount("SAVINGS", firstNum, "Rajesh Sharma", 30, 50000, 0);
            accountRepo.save(rajesh);
            System.out.println("  Saved:  " + rajesh.getAccountInfo());

            // findById returns the same stored entity
            IAccount found = accountRepo.findById(firstNum);
            System.out.println("  Found:  " + found.getAccountInfo());
            step17 &= (found == rajesh);

            // update() persists the new balance
            rajesh.deposit(15000); // Rs. 50000 -> Rs. 65000
            accountRepo.update(rajesh);
            IAccount updated = accountRepo.findById(firstNum);
            System.out.println("  Updated balance: Rs. " + updated.getBalance());
            step17 &= (updated.getBalance() == 65000.0);

            // Save a second account so findAll() proves it returns every entry
            int secondNum = accountRepo.nextAccountNumber();
            IAccount priya = AccountFactory.createAccount("CURRENT", secondNum, "Priya Verma", 28, 30000, 1);
            accountRepo.save(priya);
            System.out.println("  Saved:  " + priya.getAccountInfo());

            List<IAccount> all = accountRepo.findAll();
            System.out.println("  Total accounts in repository: " + all.size());
            step17 &= (all.size() == 2);

            // exists()
            step17 &= accountRepo.exists(firstNum);
            step17 &= !accountRepo.exists(9999);

            // nextAccountNumber() keeps incrementing sequentially
            int nextNum = accountRepo.nextAccountNumber();
            System.out.println("  nextAccountNumber(): " + nextNum + " (sequential from 1001)");
            step17 &= (nextNum == secondNum + 1);
        } catch (Exception ex) {
            step17 = false;
            ex.printStackTrace();
        }
        System.out.println("[STEP 17] Account repository CRUD: " + (step17 ? "[PASS]" : "[FAIL]"));

        // ============================================================
        // 📝 STEP 18: Test Transaction Repository Operations
        // ============================================================
        System.out.println("\n--- STEP 18: Transaction Repository Operations ---");
        boolean step18 = true;
        try {
            Transaction deposit = new Transaction(
                    Transaction.generateId(), LocalDateTime.now(), 1001,
                    TransactionType.DEPOSIT, 15000, 65000, "SUCCESS",
                    "Deposit of Rs. 15000", 0, 0);
            Transaction transfer = new Transaction(
                    Transaction.generateId(), LocalDateTime.now(), 1002,
                    TransactionType.TRANSFER, 5000, 25000, "SUCCESS",
                    "Transfer of Rs. 5000 to Account #1001", 1002, 1001);

            txnRepo.save(deposit);
            txnRepo.save(transfer);
            txnRepo.save(null); // null entries must be ignored
            System.out.println("  Saved 2 transaction records.");

            // #1001 matches both the deposit (account_number) and the incoming transfer (to_account)
            List<Transaction> for1001 = txnRepo.findByAccount(1001);
            System.out.println("  Found transactions for #1001: " + for1001.size());
            step18 &= (for1001.size() == 2);

            // findAll() returns every record (the null save was ignored)
            List<Transaction> allTxns = txnRepo.findAll();
            System.out.println("  findAll() returned: " + allTxns.size() + " records");
            step18 &= (allTxns.size() == 2);

            // clear() empties the ledger
            txnRepo.clear();
            System.out.println("  After clear(), findAll() returned: " + txnRepo.findAll().size() + " records");
            step18 &= txnRepo.findAll().isEmpty();
        } catch (Exception ex) {
            step18 = false;
            ex.printStackTrace();
        }
        System.out.println("[STEP 18] Transaction repository operations: " + (step18 ? "[PASS]" : "[FAIL]"));

        // ============================================================
        // 📝 STEP 19: Test Service Integration With Repositories
        // ============================================================
        System.out.println("\n--- STEP 19: Service Integration With Repositories ---");
        boolean step19 = true;
        try {
            // nextAccountNumber() now hands out 1003 and 1004
            IAccount john = service.openAccount("SAVINGS", "John Doe", 25, 15000);
            john.setPin(1234);
            IAccount jane = service.openAccount("SAVINGS", "Jane Smith", 30, 10000);
            jane.setPin(5678);
            System.out.println("  Opened via service: " + john.getAccountInfo());
            System.out.println("  Opened via service: " + jane.getAccountInfo());

            service.deposit(john.getAccountNumber(), 5000);                    // 15000 -> 20000
            service.withdraw(john.getAccountNumber(), 2000, 1234);             // 20000 -> 18000
            service.transfer(john.getAccountNumber(), jane.getAccountNumber(),
                    1000, 1234);                                               // 18000 -> 17000, Jane -> 11000

            System.out.println("  John balance after ops: Rs. " + john.getBalance());
            System.out.println("  Jane balance after ops: Rs. " + jane.getBalance());
            step19 &= (john.getBalance() == 17000.0);
            step19 &= (jane.getBalance() == 11000.0);

            // Service queries the transaction repository on our behalf
            List<Transaction> johnTxns = service.getTransactionRecords(john.getAccountNumber());
            List<Transaction> janeTxns = service.getTransactionRecords(jane.getAccountNumber());
            System.out.println("  Transactions for John (#" + john.getAccountNumber() + "): " + johnTxns.size());
            System.out.println("  Transactions for Jane (#" + jane.getAccountNumber() + "): " + janeTxns.size());
            step19 &= (johnTxns.size() == 3); // deposit + withdraw + outgoing transfer
            step19 &= (janeTxns.size() == 1); // incoming transfer

            List<IAccount> allAccounts = service.getAllAccounts();
            System.out.println("  Total accounts via service.getAllAccounts(): " + allAccounts.size());
            step19 &= (allAccounts.size() == 4); // Rajesh, Priya, John, Jane

            int allTxnCount = service.getTransactionRecords().size();
            System.out.println("  Total transaction records (post-clear): " + allTxnCount);
            step19 &= (allTxnCount == 3);
        } catch (Exception ex) {
            step19 = false;
            ex.printStackTrace();
        }
        System.out.println("[STEP 19] Service integration with repositories: " + (step19 ? "[PASS]" : "[FAIL]"));

        boolean allPass = step17 && step18 && step19;
        System.out.println("\n" + "=".repeat(60));
        System.out.println(allPass
                ? "  ALL ACTIVITY 21 IN-MEMORY REPOSITORY TESTS PASSED!"
                : "  SOME ACTIVITY 21 IN-MEMORY REPOSITORY TESTS FAILED!");
        System.out.println("=".repeat(60));

        System.out.println("Activity 21 Repository tests complete.");
    }
}
