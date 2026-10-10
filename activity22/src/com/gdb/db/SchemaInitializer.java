package com.gdb.db;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Initializes the database schema idempotently by executing schema.sql DDL statements.
 */
public class SchemaInitializer {

    // ============================================================
    // 📝 STEP 5: Implement initialize(ConnectionProvider provider)
    //
    // INSTRUCTIONS:
    //   1. Obtain a Connection from provider.
    //   2. Read the contents of schema.sql from resource stream or file.
    //   3. Split SQL script into individual statements by semicolon ';'.
    //   4. Execute each non-empty statement using Statement.execute(sql).
    // ============================================================
    public static void initialize(ConnectionProvider provider) {
        String schemaSql = readSchemaSql();
        try (Connection conn = provider.getConnection();
             Statement stmt = conn.createStatement()) {
            // Split the script into individual statements by ';'
            String[] statements = schemaSql.split(";");
            for (String part : statements) {
                String sql = part.trim();
                if (!sql.isEmpty()) {
                    stmt.execute(sql);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database schema: " + e.getMessage(), e);
        }
    }

    // ============================================================
    // 📝 STEP 6: Helper Method to Read SQL Script
    // ============================================================
    public static String readSchemaSql() {
        // 1. Try classpath first
        String classpath = readFromStream(
                SchemaInitializer.class.getClassLoader().getResourceAsStream("schema.sql"));
        if (!classpath.isEmpty()) {
            return classpath;
        }

        // 2. Try file paths
        String[] paths = { "src/main/resources/schema.sql", "bin/schema.sql" };
        for (String path : paths) {
            File file = new File(path);
            if (file.exists()) {
                String content = readFromFile(file);
                if (!content.isEmpty()) {
                    return content;
                }
            }
        }

        // 3. None found
        return "";
    }

    private static String readFromStream(InputStream in) {
        if (in == null) {
            return "";
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return readAllLines(reader);
        } catch (IOException e) {
            return "";
        }
    }

    private static String readFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            return readAllLines(reader);
        } catch (IOException e) {
            return "";
        }
    }

    private static String readAllLines(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append('\n');
        }
        return sb.toString();
    }
}
