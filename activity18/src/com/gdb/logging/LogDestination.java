package com.gdb.logging;

import com.gdb.command.TransactionCommand;
import java.util.List;

/**
 * Implementor interface for Bridge Pattern decoupling transaction log storage backends.
 */
public interface LogDestination {
    // STEPS 1-4: Implementor contract — each destination must support these four operations.
    void write(TransactionCommand cmd);

    List<TransactionCommand> readAll();

    void clear();

    String getDestinationName();
}