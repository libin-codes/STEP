package com.gdb.db;

import com.gdb.command.TransactionCommand;
import com.gdb.logging.LogDestination;
import com.gdb.repository.TransactionRepository;

import java.util.List;

/**
 * Utility for migrating legacy file/in-memory transaction command logs into relational SQLite database.
 */
public class DataMigrator {

    // ============================================================
    // STEP 6: migrate(LogDestination source, TransactionRepository target)
    //   Copy every logged transaction into the target repository.
    //   Returns the number of records successfully migrated.
    // ============================================================
    public static int migrate(LogDestination source, TransactionRepository target) {
        if (source == null || target == null) {
            return 0;
        }
        List<TransactionCommand> commands = source.readAll();
        int count = 0;
        for (TransactionCommand cmd : commands) {
            if (cmd != null && cmd.getTransaction() != null) {
                target.save(cmd.getTransaction());
                count++;
            }
        }
        return count;
    }
}
