package com.gdb.tests;

import com.gdb.db.ConnectionProvider;
import com.gdb.db.JdbcConnectionProvider;
import com.gdb.db.SchemaInitializer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TestJdbcConnection {
    public static void main(String[] args) throws Exception {
        System.out.println("=".repeat(60));
        System.out.println("  ACTIVITY 22 — JDBC FOUNDATION (CONNECTION & SCHEMA)");
        System.out.println("=".repeat(60));

        String testDbUrl = "jdbc:sqlite:test_gdb.db";
        ConnectionProvider provider = new JdbcConnectionProvider(testDbUrl);

        // ============================================================
        // 📝 STEP 8: Test Direct JDBC Connection & Simple Query
        // ============================================================
        try (Connection conn = provider.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 1")) {
            rs.next();
            System.out.println("[STEP 8] SELECT 1 returned: " + rs.getInt(1));
        }

        // ============================================================
        // 📝 STEP 9: Initialize Schema & Verify Tables
        // ============================================================
        SchemaInitializer.initialize(provider);

        List<String> tableNames = new ArrayList<>();
        try (Connection conn = provider.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT name FROM sqlite_master WHERE type='table' ORDER BY name")) {
            while (rs.next()) {
                tableNames.add(rs.getString(1));
            }
        }

        System.out.println("[STEP 9] Tables found: " + String.join(", ", tableNames));
        boolean hasAccounts = tableNames.contains("accounts");
        boolean hasTransactions = tableNames.contains("transactions");
        System.out.println("  Table 'accounts' exists: " + hasAccounts);
        System.out.println("  Table 'transactions' exists: " + hasTransactions);
        System.out.println("  Total tables: " + tableNames.size() + " (expected 2)");
        System.out.println("[STEP 9] "
                + (hasAccounts && hasTransactions ? "[PASS]" : "[FAIL]")
                + " Both required tables (accounts, transactions) present.");

        provider.shutdown();
        System.out.println("Activity 22 JDBC tests complete.");
    }
}
