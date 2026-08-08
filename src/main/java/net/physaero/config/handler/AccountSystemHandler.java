package net.physaero.config.handler;

import net.physaero.config.config.AccountSystem;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AccountSystemHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountSystemHandler.class);
    private static boolean restartRequired = false;

    public static boolean isRestartRequired() {
        return restartRequired;
    }

    public static void setRestartRequired(boolean required) {
        restartRequired = required;
    }

    /**
     * Toggles between e4all and Essentials mod jar files in the mods directory.
     *
     * @param chosenSystem The target account system.
     */
    public static void applyAccountSystem(AccountSystem chosenSystem) {
        applyAccountSystem(getGameDir(), chosenSystem);
    }

    private static Path getGameDir() {
        try {
            return FMLPaths.GAMEDIR.get();
        } catch (Throwable t) {
            return Path.of(".");
        }
    }

    /**
     * Toggles between e4all and Essentials mod jar files in the specified game directory.
     *
     * @param gameDir      The root game directory.
     * @param chosenSystem The target account system.
     */
    public static void applyAccountSystem(Path gameDir, AccountSystem chosenSystem) {
        Path modsDir = gameDir.resolve("mods");

        if (!Files.exists(modsDir) || !Files.isDirectory(modsDir)) {
            LOGGER.warn("[PhysAeroConfig] Mods directory does not exist at {}. Cannot switch account system.", modsDir);
            return;
        }

        List<Path> e4allFiles = findModFiles(modsDir, "e4all");
        List<Path> essentialsFiles = findModFiles(modsDir, "essential");

        if (e4allFiles.isEmpty()) {
            LOGGER.warn("[PhysAeroConfig] Could not find any e4all mod file in mods directory.");
        }

        if (essentialsFiles.isEmpty()) {
            LOGGER.warn("[PhysAeroConfig] Could not find any Essentials mod file in mods directory.");
        }

        if (chosenSystem == AccountSystem.ESSENTIALS) {
            for (Path path : essentialsFiles) {
                enableMod(path);
            }
            for (Path path : e4allFiles) {
                disableMod(path);
            }
        } else {
            for (Path path : e4allFiles) {
                enableMod(path);
            }
            for (Path path : essentialsFiles) {
                disableMod(path);
            }
        }
    }

    private static List<Path> findModFiles(Path modsDir, String keyword) {
        List<Path> matches = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(modsDir)) {
            for (Path entry : stream) {
                String fileName = entry.getFileName().toString().toLowerCase();
                if (fileName.contains(lowerKeyword) && (fileName.endsWith(".jar") || fileName.endsWith(".jar.disabled"))) {
                    matches.add(entry);
                }
            }
        } catch (IOException e) {
            LOGGER.error("[PhysAeroConfig] Failed to scan mods directory: {}", e.getMessage(), e);
        }

        return matches;
    }

    private static void enableMod(Path file) {
        String filename = file.getFileName().toString();
        if (filename.toLowerCase().endsWith(".jar.disabled")) {
            String newName = filename.substring(0, filename.length() - ".disabled".length());
            Path target = file.getParent().resolve(newName);
            try {
                Files.move(file, target);
                LOGGER.info("[PhysAeroConfig] Enabled mod: {} -> {}", filename, newName);
                restartRequired = true;
            } catch (IOException e) {
                LOGGER.error("[PhysAeroConfig] Failed to enable mod {}: {}", filename, e.getMessage(), e);
            }
        } else if (filename.toLowerCase().endsWith(".jar")) {
            LOGGER.info("[PhysAeroConfig] Mod {} is already enabled.", filename);
        }
    }

    private static void disableMod(Path file) {
        String filename = file.getFileName().toString();
        if (filename.toLowerCase().endsWith(".jar")) {
            String newName = filename + ".disabled";
            Path target = file.getParent().resolve(newName);
            try {
                Files.move(file, target);
                LOGGER.info("[PhysAeroConfig] Disabled mod: {} -> {}", filename, newName);
                restartRequired = true;
            } catch (IOException e) {
                LOGGER.error("[PhysAeroConfig] Failed to disable mod {}: {}", filename, e.getMessage(), e);
            }
        } else if (filename.toLowerCase().endsWith(".jar.disabled")) {
            LOGGER.info("[PhysAeroConfig] Mod {} is already disabled.", filename);
        }
    }
}
