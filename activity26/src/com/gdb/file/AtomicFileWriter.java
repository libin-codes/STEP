package com.gdb.file;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Provides atomic, crash-resilient file write operations using temp files and atomic rename.
 */
public class AtomicFileWriter {

    // ============================================================
    // STEP 1: write(String path, List<String> lines)
    //   Write to "<path>.tmp" then atomically move it over the target.
    // ============================================================
    public static void write(String path, List<String> lines) throws IOException {
        Path target = Paths.get(path);
        Path parent = target.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        Path temp = Paths.get(path + ".tmp");
        try (BufferedWriter writer = Files.newBufferedWriter(temp, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
            if (lines != null) {
                for (String line : lines) {
                    writer.write(line == null ? "" : line);
                    writer.newLine();
                }
            }
            writer.flush();
        }

        try {
            Files.move(temp, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
