package com.gdb.logging;

import com.gdb.command.TransactionCommand;
import java.io.IOException;
import java.util.List;

public class FileLogDestination implements LogDestination {
    // STEP 5: Declare Field
    private TransactionLog log;

    // STEP 6: Constructor
    public FileLogDestination() {
        this.log = new TransactionLog();
    }

    // STEP 7: write(cmd) — delegate to log.log(cmd)
    // TransactionLog.log throws IOException, but the LogDestination contract declares
    // no checked exceptions, so wrap it in an unchecked RuntimeException.
    @Override
    public void write(TransactionCommand cmd) {
        try {
            log.log(cmd);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write transaction to file log: " + e.getMessage(), e);
        }
    }

    // STEP 8: readAll() — delegate to log.readAll()
    @Override
    public List<TransactionCommand> readAll() {
        try {
            return log.readAll();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to read file log: " + e.getMessage(), e);
        }
    }

    // STEP 9: clear() — delegate to log.clear()
    @Override
    public void clear() {
        log.clear();
    }

    // STEP 10: getDestinationName()
    @Override
    public String getDestinationName() {
        return "FILE";
    }
}