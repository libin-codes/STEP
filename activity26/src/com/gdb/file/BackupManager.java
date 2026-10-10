package com.gdb.file;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Manages timestamped backups of data files and maintains retention limits.
 */
public class BackupManager {

    // ============================================================
    // STEP 6: backup(String path, int maxBackups)
    //   Copy the current file to a timestamped backup, then prune
    //   the oldest backups beyond the retention limit.
    // ============================================================
    public static void backup(String path, int maxBackups) throws IOException {
        Path source = Paths.get(path);
        if (!Files.exists(source)) {
            return;
        }

        long stamp = System.currentTimeMillis();
        Path backup = Paths.get(path + ".bak." + stamp);
        int suffix = 1;
        while (Files.exists(backup)) {
            backup = Paths.get(path + ".bak." + stamp + "-" + (suffix++));
        }
        Files.copy(source, backup, StandardCopyOption.REPLACE_EXISTING);

        if (maxBackups >= 0) {
            pruneBackups(path, maxBackups);
        }
    }

    private static void pruneBackups(String path, int maxBackups) throws IOException {
        Path source = Paths.get(path);
        Path dir = source.toAbsolutePath().getParent();
        if (dir == null) {
            return;
        }
        String prefix = source.getFileName().toString() + ".bak.";
        List<Path> backups;
        try (Stream<Path> stream = Files.list(dir)) {
            backups = stream
                    .filter(p -> p.getFileName().toString().startsWith(prefix))
                    .sorted(Comparator.comparingLong(BackupManager::lastModified))
                    .collect(Collectors.toList());
        }

        int excess = backups.size() - maxBackups;
        for (int i = 0; i < excess; i++) {
            Files.deleteIfExists(backups.get(i));
        }
    }

    private static long lastModified(Path p) {
        try {
            return Files.getLastModifiedTime(p).toMillis();
        } catch (IOException e) {
            return Long.MAX_VALUE;
        }
    }
}
