package com.gdb.repository;

import java.io.*;
import java.util.Properties;

/**
 * Factory creating and supplying repository instances based on persistence.properties configuration.
 */
public class RepositoryFactory {

    private static AccountRepository accountRepositoryInstance;
    private static TransactionRepository transactionRepositoryInstance;

    // ============================================================
    // 📝 STEP 14: Read persistence.mode from Configuration
    //
    // INSTRUCTIONS:
    //   1. Load properties from "config/persistence.properties" or classpath resource.
    //   2. Return property value for "persistence.mode" (default to "memory" if missing).
    // ============================================================
    public static String getPersistenceMode() {
        Properties props = new Properties();
        try (InputStream in = openConfigStream()) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException ex) {
            // Missing/unreadable config falls back to the "memory" default below.
        }

        String mode = props.getProperty("persistence.mode");
        if (mode == null || mode.trim().isEmpty()) {
            return "memory";
        }
        return mode.trim().toLowerCase();
    }

    /**
     * Locate persistence.properties as a classpath resource first, then fall back to
     * common on-disk locations so the factory works from both bin/ and project root.
     */
    private static InputStream openConfigStream() {
        InputStream in = RepositoryFactory.class.getClassLoader()
                .getResourceAsStream("config/persistence.properties");
        if (in != null) {
            return in;
        }

        File file = new File("src/main/resources/config/persistence.properties");
        if (!file.exists()) {
            file = new File("bin/config/persistence.properties");
        }
        if (file.exists()) {
            try {
                return new FileInputStream(file);
            } catch (IOException ex) {
                // fall through to null
            }
        }
        return null;
    }

    // ============================================================
    // 📝 STEP 15: Implement getAccountRepository()
    //
    // INSTRUCTIONS:
    //   1. Check persistence mode:
    //      - "memory" -> return singleton InMemoryAccountRepository
    //      - "jdbc"   -> throw UnsupportedOperationException("JDBC repository not implemented yet - coming in Activity 22")
    //      - "file"   -> throw UnsupportedOperationException("File repository not implemented yet")
    //      - default  -> return InMemoryAccountRepository
    // ============================================================
    public static synchronized AccountRepository getAccountRepository() {
        if (accountRepositoryInstance == null) {
            switch (getPersistenceMode()) {
                case "jdbc":
                    throw new UnsupportedOperationException(
                            "JDBC repository not implemented yet - coming in Activity 22");
                case "file":
                    throw new UnsupportedOperationException(
                            "File repository not implemented yet");
                case "memory":
                default:
                    accountRepositoryInstance = new InMemoryAccountRepository();
            }
        }
        return accountRepositoryInstance;
    }

    // ============================================================
    // 📝 STEP 16: Implement getTransactionRepository()
    //
    // INSTRUCTIONS:
    //   1. Check persistence mode:
    //      - "memory" -> return singleton InMemoryTransactionRepository
    //      - "jdbc"   -> throw UnsupportedOperationException("JDBC repository not implemented yet - coming in Activity 22")
    //      - "file"   -> throw UnsupportedOperationException("File repository not implemented yet")
    //      - default  -> return InMemoryTransactionRepository
    // ============================================================
    public static synchronized TransactionRepository getTransactionRepository() {
        if (transactionRepositoryInstance == null) {
            switch (getPersistenceMode()) {
                case "jdbc":
                    throw new UnsupportedOperationException(
                            "JDBC repository not implemented yet - coming in Activity 22");
                case "file":
                    throw new UnsupportedOperationException(
                            "File repository not implemented yet");
                case "memory":
                default:
                    transactionRepositoryInstance = new InMemoryTransactionRepository();
            }
        }
        return transactionRepositoryInstance;
    }
}
