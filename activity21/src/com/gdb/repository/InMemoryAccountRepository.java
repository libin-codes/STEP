package com.gdb.repository;

import com.gdb.domain.IAccount;
import java.util.*;

/**
 * In-memory implementation of AccountRepository backed by a Map.
 */
public class InMemoryAccountRepository implements AccountRepository {

    // ============================================================
    // 📝 STEP 1: Declare Fields
    //
    // INSTRUCTIONS:
    //   1. Declare a private final Map<Integer, IAccount> accounts = new HashMap<>()
    //   2. Declare a private int nextAccountNumber = 1001
    // ============================================================
    // TODO: Step 1 - declare fields
    private final Map<Integer, IAccount> accounts = new HashMap<>();
    private int nextAccountNumber = 1001;

    // ============================================================
    // 📝 STEP 2: Implement save(IAccount account)
    //
    // INSTRUCTIONS:
    //   Store the account in the map keyed by account.getAccountNumber().
    // ============================================================
    @Override
    public void save(IAccount account) {
        if (account != null) {
            accounts.put(account.getAccountNumber(), account);
        }
    }

    // ============================================================
    // 📝 STEP 3: Implement findById(int accountNumber)
    //
    // INSTRUCTIONS:
    //   Return the account associated with accountNumber, or null if not present.
    // ============================================================
    @Override
    public IAccount findById(int accountNumber) {
        return accounts.get(accountNumber);
    }

    // ============================================================
    // 📝 STEP 4: Implement findAll()
    //
    // INSTRUCTIONS:
    //   Return a new List containing all accounts in the map.
    // ============================================================
    @Override
    public List<IAccount> findAll() {
        // Defensive copy so callers cannot mutate our internal storage.
        return new ArrayList<>(accounts.values());
    }

    // ============================================================
    // 📝 STEP 5: Implement update(IAccount account)
    //
    // INSTRUCTIONS:
    //   Update the account in the map if it exists.
    // ============================================================
    @Override
    public void update(IAccount account) {
        if (account != null && accounts.containsKey(account.getAccountNumber())) {
            accounts.put(account.getAccountNumber(), account);
        }
    }

    // ============================================================
    // 📝 STEP 6: Implement delete(int accountNumber)
    //
    // INSTRUCTIONS:
    //   Remove the account with the given accountNumber from the map.
    // ============================================================
    @Override
    public void delete(int accountNumber) {
        accounts.remove(accountNumber);
    }

    // ============================================================
    // 📝 STEP 7: Implement exists(int accountNumber)
    //
    // INSTRUCTIONS:
    //   Return true if the map contains the given accountNumber, false otherwise.
    // ============================================================
    @Override
    public boolean exists(int accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    // ============================================================
    // 📝 STEP 8: Implement nextAccountNumber()
    //
    // INSTRUCTIONS:
    //   Return current nextAccountNumber and post-increment it.
    // ============================================================
    @Override
    public synchronized int nextAccountNumber() {
        return nextAccountNumber++;
    }
}
