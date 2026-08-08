package net.physaero.config.handler;

import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FancyMenuHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(FancyMenuHandler.class);
    private static final String SKIP_KEY = "B:allow_game_intro_skip";
    private static final String ANIMATION_KEY = "S:game_intro_animation_name";

    /**
     * Applies intro settings to FancyMenu's options.txt.
     *
     * @param keepIntro true to keep the intro, false to disable it.
     */
    public static void applyIntroSetting(boolean keepIntro) {
        applyIntroSetting(getGameDir(), keepIntro);
    }

    private static Path getGameDir() {
        try {
            return FMLPaths.GAMEDIR.get();
        } catch (Throwable t) {
            return Path.of(".");
        }
    }

    /**
     * Applies intro settings to FancyMenu's options.txt.
     *
     * @param gameDir   The root game directory.
     * @param keepIntro true to keep the intro, false to disable it.
     */
    public static void applyIntroSetting(Path gameDir, boolean keepIntro) {
        Path fancyMenuDir = gameDir.resolve("config").resolve("fancymenu");
        Path optionsFile = fancyMenuDir.resolve("options.txt");

        if (!Files.exists(fancyMenuDir) || !Files.exists(optionsFile)) {
            LOGGER.info("[PhysAeroConfig] FancyMenu directory or options.txt does not exist. Skipping.");
            return;
        }

        try {
            List<String> lines = Files.readAllLines(optionsFile, StandardCharsets.UTF_8);
            List<String> updatedLines = new ArrayList<>();

            String originalAnimLine = findLineStartingWith(lines, ANIMATION_KEY);

            if (keepIntro) {
                String restoredLine = null;
                if (originalAnimLine != null && isValueEmpty(originalAnimLine)) {
                    restoredLine = loadSavedIntroLine(gameDir);
                }

                for (String line : lines) {
                    String trimmed = line.trim();
                    if (trimmed.startsWith(SKIP_KEY)) {
                        updatedLines.add(SKIP_KEY + " = 'false';");
                    } else if (trimmed.startsWith(ANIMATION_KEY)) {
                        if (restoredLine != null) {
                            updatedLines.add(restoredLine);
                            LOGGER.info("[PhysAeroConfig] Restored original intro line: {}", restoredLine);
                        } else {
                            updatedLines.add(line);
                        }
                    } else {
                        updatedLines.add(line);
                    }
                }
            } else {
                if (originalAnimLine != null && !isValueEmpty(originalAnimLine)) {
                    saveIntroLine(gameDir, originalAnimLine);
                    LOGGER.info("[PhysAeroConfig] Saved original intro line to backup: {}", originalAnimLine);
                }

                for (String line : lines) {
                    String trimmed = line.trim();
                    if (trimmed.startsWith(SKIP_KEY)) {
                        updatedLines.add(SKIP_KEY + " = 'true';");
                    } else if (trimmed.startsWith(ANIMATION_KEY)) {
                        updatedLines.add(ANIMATION_KEY + " = '';");
                    } else {
                        updatedLines.add(line);
                    }
                }
            }

            Files.write(optionsFile, updatedLines, StandardCharsets.UTF_8);
            LOGGER.info("[PhysAeroConfig] Successfully updated FancyMenu options.txt (keepIntro={})", keepIntro);
        } catch (IOException e) {
            LOGGER.warn("[PhysAeroConfig] Failed to update FancyMenu options.txt: {}", e.getMessage());
        }
    }

    private static String findLineStartingWith(List<String> lines, String key) {
        for (String line : lines) {
            if (line.trim().startsWith(key)) {
                return line.trim();
            }
        }
        return null;
    }

    private static boolean isValueEmpty(String line) {
        int firstQuote = line.indexOf('\'');
        int lastQuote = line.lastIndexOf('\'');
        if (firstQuote >= 0 && lastQuote > firstQuote) {
            return line.substring(firstQuote + 1, lastQuote).trim().isEmpty();
        }
        return true;
    }

    private static void saveIntroLine(Path gameDir, String line) {
        Path backupFile = gameDir.resolve("config").resolve("physaeroconfig_intro_backup.txt");
        try {
            Files.writeString(backupFile, line, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.warn("[PhysAeroConfig] Failed to save intro backup: {}", e.getMessage());
        }
    }

    private static String loadSavedIntroLine(Path gameDir) {
        Path backupFile = gameDir.resolve("config").resolve("physaeroconfig_intro_backup.txt");
        if (!Files.exists(backupFile)) {
            return null;
        }
        try {
            return Files.readString(backupFile, StandardCharsets.UTF_8).trim();
        } catch (IOException e) {
            LOGGER.warn("[PhysAeroConfig] Failed to load intro backup: {}", e.getMessage());
            return null;
        }
    }
}
