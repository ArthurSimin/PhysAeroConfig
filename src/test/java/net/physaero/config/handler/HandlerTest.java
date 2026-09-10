package net.physaero.config.handler;

import net.physaero.config.config.AccountSystem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HandlerTest {

    @Test
    public void testFancyMenuHandlerModifySetting(@TempDir Path tempDir) throws IOException {
        Path fancyMenuDir = tempDir.resolve("config").resolve("fancymenu");
        Files.createDirectories(fancyMenuDir);
        Path optionsFile = fancyMenuDir.resolve("options.txt");

        List<String> optionsLines = List.of(
                "##[loading]",
                "B:allow_game_intro_skip = 'false';",
                "S:game_intro_animation_name = '[source:local]/config/fancymenu/assets/reimaginedintro/intro.fma';",
                "S:preload_resources = '[source:local]config/fancymenu/assets/reimaginedintro/intro.fma';"
        );
        Files.write(optionsFile, optionsLines, StandardCharsets.UTF_8);

        // Test: disable intro (keepIntro = false)
        FancyMenuHandler.applyIntroSetting(tempDir, false);

        List<String> updatedOptions = Files.readAllLines(optionsFile, StandardCharsets.UTF_8);
        assertTrue(updatedOptions.contains("B:allow_game_intro_skip = 'true';"));
        assertTrue(updatedOptions.contains("S:game_intro_animation_name = '';"));

        // Backup file should exist with exact original line
        Path backupFile = tempDir.resolve("config").resolve("physaeroconfig_intro_backup.txt");
        assertTrue(Files.exists(backupFile));
        assertEquals("S:game_intro_animation_name = '[source:local]/config/fancymenu/assets/reimaginedintro/intro.fma';", Files.readString(backupFile, StandardCharsets.UTF_8).trim());

        // Test: re-enable intro (keepIntro = true)
        FancyMenuHandler.applyIntroSetting(tempDir, true);

        updatedOptions = Files.readAllLines(optionsFile, StandardCharsets.UTF_8);
        assertTrue(updatedOptions.contains("B:allow_game_intro_skip = 'false';"));
        assertTrue(updatedOptions.contains("S:game_intro_animation_name = '[source:local]/config/fancymenu/assets/reimaginedintro/intro.fma';"));
        assertTrue(updatedOptions.contains("S:preload_resources = '[source:local]config/fancymenu/assets/reimaginedintro/intro.fma';"));
        assertTrue(updatedOptions.stream().noneMatch(line -> line.contains("audio.ogg")));
    }

    @Test
    public void testFancyMenuHandlerMissingDirGraceful(@TempDir Path tempDir) {
        assertDoesNotThrow(() -> FancyMenuHandler.applyIntroSetting(tempDir, false));
    }

    @Test
    public void testAccountSystemHandlerToggle(@TempDir Path tempDir) throws IOException {
        Path modsDir = tempDir.resolve("mods");
        Files.createDirectories(modsDir);

        Path e4allJar = modsDir.resolve("e4all-neoforge-1.6.4.jar");
        Path essentialsDisabled = modsDir.resolve("Essential_1-4-1-1_neoforge_1-21-1.jar.disabled");

        Files.createFile(e4allJar);
        Files.createFile(essentialsDisabled);

        // Switch to ESSENTIALS
        AccountSystemHandler.applyAccountSystem(tempDir, AccountSystem.ESSENTIALS);

        Path essentialsJar = modsDir.resolve("Essential_1-4-1-1_neoforge_1-21-1.jar");
        Path e4allDisabled = modsDir.resolve("e4all-neoforge-1.6.4.jar.disabled");

        assertTrue(Files.exists(essentialsJar), "Essentials jar should be enabled");
        assertFalse(Files.exists(essentialsDisabled), "Old disabled Essentials jar should not exist");
        assertTrue(Files.exists(e4allDisabled), "e4all jar should be disabled");
        assertFalse(Files.exists(e4allJar), "Old enabled e4all jar should not exist");

        // Switch back to E4ALL
        AccountSystemHandler.applyAccountSystem(tempDir, AccountSystem.E4ALL);

        assertTrue(Files.exists(e4allJar), "e4all jar should be re-enabled");
        assertTrue(Files.exists(essentialsDisabled), "Essentials jar should be re-disabled");
    }

    @Test
    public void testAccountSystemHandlerMissingModsGraceful(@TempDir Path tempDir) {
        assertDoesNotThrow(() -> AccountSystemHandler.applyAccountSystem(tempDir, AccountSystem.ESSENTIALS));
    }
}
