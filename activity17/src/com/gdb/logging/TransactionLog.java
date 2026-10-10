package com.gdb.logging;

import com.gdb.command.TransactionCommand;
import java.io.*;
import java.util.*;

public class TransactionLog {
    private static final String FILE_PATH = "data/transactions.ser";

    // ============================================================
    // Helper: AppendableObjectOutputStream (COMPLETE — non-placeholder)
    // ============================================================
    private static class AppendableObjectOutputStream extends ObjectOutputStream {
        public AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }
        @Override
        protected void writeStreamHeader() throws IOException {
            // do not write header when appending to an existing stream
        }
    }

    // ============================================================
    // 📝 STEP 7: Implement log(TransactionCommand cmd)
    //
    // INSTRUCTIONS:
    //   1. Create parent directory (data/) if missing.
    //   2. If file does not exist or is empty (length == 0), use new ObjectOutputStream(...).
    //   3. If file already has data, use new AppendableObjectOutputStream(new FileOutputStream(file, true)).
    //   4. Write cmd object, flush, and close stream.
    // ============================================================
    public synchronized void log(TransactionCommand cmd) throws IOException {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(FILE_PATH);
        ObjectOutputStream oos;
        if (file.exists() && file.length() > 0) {
            oos = new AppendableObjectOutputStream(new FileOutputStream(file, true));
        } else {
            oos = new ObjectOutputStream(new FileOutputStream(file));
        }
        oos.writeObject(cmd);
        oos.flush();
        oos.close();
    }

    // ============================================================
    // 📝 STEP 8: Implement readAll()
    //
    // INSTRUCTIONS:
    //   1. If file does not exist, return empty List.
    //   2. Open ObjectInputStream and loop calling readObject() until EOFException.
    //   3. Collect and return List<TransactionCommand>.
    // ============================================================
    public synchronized List<TransactionCommand> readAll() throws IOException, ClassNotFoundException {
        List<TransactionCommand> commands = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return commands;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                try {
                    commands.add((TransactionCommand) ois.readObject());
                } catch (EOFException eof) {
                    break;
                }
            }
        }
        return commands;
    }

    // ============================================================
    // 📝 STEP 9: Implement clear()
    //
    // INSTRUCTIONS:
    //   Delete the log file if it exists.
    // ============================================================
    public synchronized void clear() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }
}
