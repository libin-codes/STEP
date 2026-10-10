package com.gdb.repository;

import com.gdb.domain.Transaction;
import com.gdb.domain.TransactionType;
import com.gdb.exceptions.DataAccessException;
import com.gdb.file.AtomicFileWriter;
import com.gdb.file.BackupManager;
import com.gdb.file.CsvMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * File-backed implementation of TransactionRepository.
 * Stores transaction records in a text file and supports atomic balance transfers.
 */
public class FileTransactionRepository implements TransactionRepository {

    private static final int DEFAULT_MAX_BACKUPS = 5;

    private final String filePath;
    private final boolean autosave;
    private final int maxBackups;
    private final List<Transaction> cache = new ArrayList<>();

    public FileTransactionRepository(String filePath, boolean autosave, int startId, int maxBackups) {
        this.filePath = filePath;
        this.autosave = autosave;
        this.maxBackups = maxBackups;
        load();
    }

    public FileTransactionRepository(String filePath, boolean autosave, int startId) {
        this(filePath, autosave, startId, DEFAULT_MAX_BACKUPS);
    }

    public FileTransactionRepository(String filePath, int startId) {
        this(filePath, true, startId, DEFAULT_MAX_BACKUPS);
    }

    public FileTransactionRepository(String filePath) {
        this(filePath, true, 1, DEFAULT_MAX_BACKUPS);
    }

    // ============================================================
    // STEP 1: load() and flush()
    // ============================================================
    public synchronized void load() {
        cache.clear();
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Transaction transaction = CsvMapper.fromCsvTransaction(line);
                if (transaction != null) {
                    cache.add(transaction);
                }
            }
        } catch (IOException e) {
            throw new DataAccessException("Failed to load transactions from file: " + e.getMessage(), e);
        }
    }

    public synchronized void flush() {
        List<String> lines = new ArrayList<>();
        lines.add(CsvMapper.TRANSACTION_HEADER);
        for (Transaction transaction : cache) {
            lines.add(CsvMapper.toCsv(transaction));
        }
        try {
            if (maxBackups > 0 && Files.exists(Paths.get(filePath))) {
                BackupManager.backup(filePath, maxBackups);
            }
            AtomicFileWriter.write(filePath, lines);
        } catch (IOException e) {
            throw new DataAccessException("Failed to flush transactions to file: " + e.getMessage(), e);
        }
    }

    // ============================================================
    // STEP 2: save(Transaction transaction)
    // ============================================================
    @Override
    public synchronized void save(Transaction transaction) {
        if (transaction == null) {
            return;
        }
        cache.add(transaction);
        if (autosave) {
            flush();
        }
    }

    // ============================================================
    // STEP 3: findByAccount(int accountNumber)
    // ============================================================
    @Override
    public synchronized List<Transaction> findByAccount(int accountNumber) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : cache) {
            if (t.getAccountNumber() == accountNumber
                    || t.getFromAccount() == accountNumber
                    || t.getToAccount() == accountNumber) {
                result.add(t);
            }
        }
        return result;
    }

    // ============================================================
    // STEP 4: findAll()
    // ============================================================
    @Override
    public synchronized List<Transaction> findAll() {
        return new ArrayList<>(cache);
    }

    // ============================================================
    // STEP 5: clear()
    // ============================================================
    @Override
    public synchronized void clear() {
        cache.clear();
        if (autosave) {
            flush();
        }
    }

    // ============================================================
    // STEP 6: transfer(int fromAccountNumber, int toAccountNumber, double amount)
    //   Records the transfer audit entry in the file ledger. Balance
    //   mutation is performed by FileAccountRepository, since this
    //   repository owns transactions.csv only.
    // ============================================================
    @Override
    public synchronized void transfer(int fromAccountNumber, int toAccountNumber, double amount) {
        if (amount <= 0) {
            throw new DataAccessException("Transfer amount must be positive. Provided: " + amount);
        }
        Transaction txn = new Transaction(
                Transaction.generateId(),
                LocalDateTime.now(),
                fromAccountNumber,
                TransactionType.TRANSFER,
                amount,
                0.0,
                "SUCCESS",
                "Transfer of Rs. " + amount + " to Account #" + toAccountNumber,
                fromAccountNumber,
                toAccountNumber);
        save(txn);
    }
}
