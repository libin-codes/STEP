package com.gdb.logging;

import com.gdb.command.TransactionCommand;
import com.gdb.db.SimulatedDatabase;
import java.util.ArrayList;
import java.util.List;

public class DatabaseLogDestination implements LogDestination {
    private static final String TABLE = "transaction_log";

    // STEP 11: Declare Field
    private final SimulatedDatabase db;

    // STEP 12: Constructor
    public DatabaseLogDestination(SimulatedDatabase db) {
        this.db = db;
    }

    // STEP 13: write(cmd) — insert command into database table
    @Override
    public void write(TransactionCommand cmd) {
        db.insert(TABLE, cmd);
    }

    // STEP 14: readAll() — retrieve all commands from database table
    @Override
    public List<TransactionCommand> readAll() {
        List<TransactionCommand> commands = new ArrayList<>();
        for (Object obj : db.selectAll(TABLE)) {
            commands.add((TransactionCommand) obj);
        }
        return commands;
    }

    // STEP 15: clear() — delete all records from database table
    @Override
    public void clear() {
        db.deleteAll(TABLE);
    }

    // STEP 16: getDestinationName()
    @Override
    public String getDestinationName() {
        return "DATABASE";
    }
}