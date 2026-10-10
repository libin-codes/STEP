package com.gdb.repository;

import com.gdb.domain.Transaction;
import java.util.*;
import java.util.stream.Collectors;

/**
 * In-memory implementation of TransactionRepository backed by a List.
 */
public class InMemoryTransactionRepository implements TransactionRepository {

    // ============================================================
    // 📝 STEP 9: Declare Fields
    //
    // INSTRUCTIONS:
    //   Declare a private final List<Transaction> transactions = new ArrayList<>()
    // ============================================================
    // TODO: Step 9 - declare transactions list
    private final List<Transaction> transactions = new ArrayList<>();

    // ============================================================
    // 📝 STEP 10: Implement save(Transaction transaction)
    //
    // INSTRUCTIONS:
    //   Append the transaction to the list if not null.
    // ============================================================
    @Override
    public void save(Transaction transaction) {
        if (transaction != null) {
            transactions.add(transaction);
        }
    }

    // ============================================================
    // 📝 STEP 11: Implement findByAccount(int accountNumber)
    //
    // INSTRUCTIONS:
    //   Filter and return transactions matching accountNumber (or from/to account).
    // ============================================================
    @Override
    public List<Transaction> findByAccount(int accountNumber) {
        return transactions.stream()
                .filter(t -> t.getAccountNumber() == accountNumber
                          || t.getFromAccount() == accountNumber
                          || t.getToAccount() == accountNumber)
                .collect(Collectors.toList());
    }

    // ============================================================
    // 📝 STEP 12: Implement findAll()
    //
    // INSTRUCTIONS:
    //   Return a copy of all stored transactions.
    // ============================================================
    @Override
    public List<Transaction> findAll() {
        // Defensive copy so callers cannot mutate our internal storage.
        return new ArrayList<>(transactions);
    }

    // ============================================================
    // 📝 STEP 13: Implement clear()
    //
    // INSTRUCTIONS:
    //   Clear all transactions from the list.
    // ============================================================
    @Override
    public void clear() {
        transactions.clear();
    }
}
