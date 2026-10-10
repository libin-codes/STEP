package com.gdb.tests;

import com.gdb.db.*;
import com.gdb.domain.*;
import com.gdb.repository.*;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class TestConnectionPooling {

    private static final String DB_URL = "jdbc:sqlite:test_pool_gdb.db";
    private static final int ITERATIONS = 100;

    public static void main(String[] args) throws Exception {
        System.out.println("=".repeat(60));
        System.out.println("  ACTIVITY 25 — CONNECTION POOLING WITH HIKARICP (NFR)");
        System.out.println("=".repeat(60));

        Properties props = new Properties();
        props.setProperty("persistence.db.url", DB_URL);
        props.setProperty("persistence.db.driver", "org.sqlite.JDBC");
        props.setProperty("persistence.pool.enabled", "true");
        props.setProperty("persistence.pool.size", "5");
        props.setProperty("persistence.pool.timeout", "10000");

        File dbFile = new File(DB_URL.replace("jdbc:sqlite:", ""));
        if (dbFile.exists()) dbFile.delete();

        // ============================================================
        // STEP 5: Test Connection Pool Initialization
        // ============================================================
        System.out.println("\n[TEST 1] HikariCP Connection Pool Initialization:");
        HikariConnectionProvider provider = new HikariConnectionProvider(props);
        SchemaInitializer.initialize(provider);

        System.out.println("  Pool Name: " + provider.getDataSource().getPoolName());
        System.out.println("  Max Pool Size: " + provider.getDataSource().getMaximumPoolSize());
        boolean acquired;
        try (Connection conn = provider.getConnection()) {
            acquired = conn != null && !conn.isClosed();
        }
        System.out.println("  Connection acquired: " + acquired);
        assert acquired : "Pool must hand out a working connection";
        assert provider.getDataSource().getMaximumPoolSize() == 5 : "Pool size should honor config";
        passed();

        // ============================================================
        // STEP 6: Connection Borrowing & Returning from Pool
        // ============================================================
        System.out.println("\n[TEST 2] Borrow / Return / Reuse from Pool:");
        Class.forName("org.sqlite.JDBC");
        Connection pooled = provider.getConnection();
        try (Statement st = pooled.createStatement();
             ResultSet rs = st.executeQuery("SELECT 1")) {
            assert rs.next() : "Query over pooled connection should return a row";
        }
        pooled.close(); // returns to pool, does not close physical socket
        try (Connection reused = provider.getConnection()) {
            assert reused != null : "Pool should return a reusable connection";
        }
        System.out.println("  Borrowed, queried, returned, and reused a pooled connection.");
        passed();

        // ============================================================
        // STEP 7: Benchmark Pooled vs Non-Pooled Performance
        // ============================================================
        System.out.println("\n[TEST 3] Latency Benchmark (" + ITERATIONS + " sequential connection cycles):");
        // Warm up both paths.
        benchmarkDirect(20);
        benchmarkPooled(provider, 20);

        long directMs = benchmarkDirect(ITERATIONS);
        long pooledMs = benchmarkPooled(provider, ITERATIONS);
        double directAvg = (double) directMs / ITERATIONS;
        double pooledAvg = (double) pooledMs / ITERATIONS;
        double speedup = pooledMs == 0 ? Double.POSITIVE_INFINITY : (double) directMs / pooledMs;

        System.out.printf("  Unpooled Direct Connections: %d ms (avg %.3f ms/op)%n", directMs, directAvg);
        System.out.printf("  HikariCP Pooled Connections: %d ms (avg %.3f ms/op)%n", pooledMs, pooledAvg);
        System.out.printf("  Speedup Factor: ~%.1fx%n", speedup);
        assert pooledMs <= directMs : "Pooled acquisition should not be slower than unpooled";
        passed();

        // ============================================================
        // Repository operations over the pool
        // ============================================================
        System.out.println("\n[TEST 4] Repository Operations over HikariCP:");
        JdbcAccountRepository repo = new JdbcAccountRepository(provider);
        IAccount account = AccountFactory.createAccount("SAVINGS", 1001, "Rajesh Sharma", 30, 50000.0);
        repo.save(account);
        IAccount found = repo.findById(1001);
        System.out.println("  Saved Account #1001 via pooled repository.");
        System.out.println("  Retrieved Account #1001: Balance Rs. " + found.getBalance());
        assert found != null && found.getBalance() == 50000.0 : "Account should round-trip through pooled repo";
        passed();

        provider.shutdown();

        if (dbFile.exists()) dbFile.delete();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("  ALL ACTIVITY 25 CONNECTION POOLING TESTS PASSED!");
        System.out.println("=".repeat(60));
    }

    private static long benchmarkDirect(int iterations) throws Exception {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT 1")) {
                rs.next();
            }
        }
        return (System.nanoTime() - start) / 1_000_000;
    }

    private static long benchmarkPooled(HikariConnectionProvider provider, int iterations) throws Exception {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            try (Connection conn = provider.getConnection();
                 Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT 1")) {
                rs.next();
            }
        }
        return (System.nanoTime() - start) / 1_000_000;
    }

    private static void passed() {
        System.out.println("  -> PASSED");
    }
}
