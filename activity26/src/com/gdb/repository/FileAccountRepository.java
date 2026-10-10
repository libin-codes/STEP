package com.gdb.repository;

import com.gdb.domain.*;
import com.gdb.exceptions.DataAccessException;
import com.gdb.file.AtomicFileWriter;
import com.gdb.file.BackupManager;
import com.gdb.file.CsvMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/**
 * File-backed implementation of AccountRepository.
 * Stores account records in a CSV/text file with atomic writes.
 */
public class FileAccountRepository implements AccountRepository {

    private static final int DEFAULT_MAX_BACKUPS = 5;

    private final String filePath;
    private final boolean autosave;
    private final int maxBackups;
    private int nextAccountNumber;
    private final Map<Integer, IAccount> cache = new HashMap<>();

    public FileAccountRepository(String filePath, boolean autosave, int startAccountNumber, int maxBackups) {
        this.filePath = filePath;
        this.autosave = autosave;
        this.nextAccountNumber = startAccountNumber;
        this.maxBackups = maxBackups;
        load();
    }

    public FileAccountRepository(String filePath, boolean autosave, int startAccountNumber) {
        this(filePath, autosave, startAccountNumber, DEFAULT_MAX_BACKUPS);
    }

    public FileAccountRepository(String filePath, int startAccountNumber) {
        this(filePath, true, startAccountNumber, DEFAULT_MAX_BACKUPS);
    }

    public FileAccountRepository(String filePath) {
        this(filePath, true, 1001, DEFAULT_MAX_BACKUPS);
    }

    // ============================================================
    // STEP 1: load()
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
                IAccount account = CsvMapper.fromCsv(line);
                if (account != null) {
                    cache.put(account.getAccountNumber(), account);
                    if (account.getAccountNumber() >= nextAccountNumber) {
                        nextAccountNumber = account.getAccountNumber() + 1;
                    }
                }
            }
        } catch (IOException e) {
            throw new DataAccessException("Failed to load accounts from file: " + e.getMessage(), e);
        }
    }

    // ============================================================
    // STEP 2: flush()
    // ============================================================
    public synchronized void flush() {
        List<String> lines = new ArrayList<>();
        lines.add(CsvMapper.ACCOUNT_HEADER);
        for (IAccount account : cache.values()) {
            lines.add(CsvMapper.toCsv(account));
        }
        try {
            if (maxBackups > 0 && Files.exists(Paths.get(filePath))) {
                BackupManager.backup(filePath, maxBackups);
            }
            AtomicFileWriter.write(filePath, lines);
        } catch (IOException e) {
            throw new DataAccessException("Failed to flush accounts to file: " + e.getMessage(), e);
        }
    }

    // ============================================================
    // STEP 3: save(IAccount account)
    // ============================================================
    @Override
    public synchronized void save(IAccount account) {
        if (account == null) {
            return;
        }
        cache.put(account.getAccountNumber(), account);
        if (account.getAccountNumber() >= nextAccountNumber) {
            nextAccountNumber = account.getAccountNumber() + 1;
        }
        if (autosave) {
            flush();
        }
    }

    // ============================================================
    // STEP 4: findById(int accountNumber)
    // ============================================================
    @Override
    public synchronized IAccount findById(int accountNumber) {
        return cache.get(accountNumber);
    }

    // ============================================================
    // STEP 5: findAll()
    // ============================================================
    @Override
    public synchronized List<IAccount> findAll() {
        return new ArrayList<>(cache.values());
    }

    // ============================================================
    // STEP 6: update(IAccount account)
    // ============================================================
    @Override
    public synchronized void update(IAccount account) {
        if (account == null || !cache.containsKey(account.getAccountNumber())) {
            return;
        }
        cache.put(account.getAccountNumber(), account);
        if (autosave) {
            flush();
        }
    }

    // ============================================================
    // STEP 7: delete(int accountNumber)
    // ============================================================
    @Override
    public synchronized void delete(int accountNumber) {
        if (cache.remove(accountNumber) != null && autosave) {
            flush();
        }
    }

    // ============================================================
    // STEP 8: exists(int accountNumber)
    // ============================================================
    @Override
    public synchronized boolean exists(int accountNumber) {
        return cache.containsKey(accountNumber);
    }

    // ============================================================
    // STEP 9: nextAccountNumber()
    // ============================================================
    @Override
    public synchronized int nextAccountNumber() {
        return nextAccountNumber++;
    }
}
