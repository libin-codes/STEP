package com.gdb.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Production-grade ConnectionProvider backed by HikariCP Connection Pool.
 */
public class HikariConnectionProvider implements ConnectionProvider {

    // ============================================================
    // 📝 STEP 1: Declare DataSource Field
    // ============================================================
    private HikariDataSource dataSource;

    // ============================================================
    // STEP 2: Constructor with HikariConfig
    // ============================================================
    public HikariConnectionProvider(Properties config) {
        Properties props = config != null ? config : new Properties();
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(props.getProperty("persistence.db.url", "jdbc:sqlite:gdb.db"));
        hikariConfig.setDriverClassName(props.getProperty("persistence.db.driver", "org.sqlite.JDBC"));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(props.getProperty("persistence.pool.size", "10")));
        hikariConfig.setConnectionTimeout(Long.parseLong(props.getProperty("persistence.pool.timeout", "30000")));
        hikariConfig.setIdleTimeout(Long.parseLong(props.getProperty("persistence.pool.idle.timeout", "600000")));
        hikariConfig.setPoolName("GdbHikariPool");

        try {
            this.dataSource = new HikariDataSource(hikariConfig);
        } catch (Exception e) {
            throw new com.gdb.exceptions.DataAccessException(
                    "Failed to initialize HikariCP connection pool: " + e.getMessage(), e);
        }
    }

    // ============================================================
    // STEP 3: getConnection()
    // ============================================================
    @Override
    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("HikariDataSource has not been initialized");
        }
        return dataSource.getConnection();
    }

    // ============================================================
    // STEP 4: shutdown()
    // ============================================================
    @Override
    public void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    @Override
    public String getProviderName() {
        return "HikariConnectionProvider [Pool size: " + (dataSource != null ? dataSource.getMaximumPoolSize() : "N/A") + "]";
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }
}
