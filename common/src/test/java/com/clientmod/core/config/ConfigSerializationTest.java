package com.clientmod.core.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigSerializationTest {

    @Test
    void testDefaultConfigCreationAndPersistence(@TempDir Path tempDir) {
        Path configFile = tempDir.resolve("test_config.json");
        ConfigManager manager = new ConfigManager(configFile);
        manager.load();

        ModConfig config = manager.getConfig();
        assertThat(config).isNotNull();
        assertThat(config.enabled).isTrue();
        assertThat(config.coordinatesHud).isTrue();
        assertThat(config.fullbright).isFalse();

        // Check file exists on disk
        assertThat(Files.exists(configFile)).isTrue();
    }

    @Test
    void testConfigModificationAndReload(@TempDir Path tempDir) {
        Path configFile = tempDir.resolve("test_config.json");
        ConfigManager manager = new ConfigManager(configFile);
        manager.load();

        ModConfig config = manager.getConfig();
        config.fullbright = true;
        config.autoSprint = true;
        config.zoomFactor = 0.5;
        manager.save();

        ConfigManager reloadManager = new ConfigManager(configFile);
        reloadManager.load();
        ModConfig reloaded = reloadManager.getConfig();

        assertThat(reloaded.fullbright).isTrue();
        assertThat(reloaded.autoSprint).isTrue();
        assertThat(reloaded.zoomFactor).isEqualTo(0.5);
    }

    @Test
    void testCorruptedConfigFallback(@TempDir Path tempDir) throws Exception {
        Path configFile = tempDir.resolve("corrupted.json");
        Files.writeString(configFile, "{ invalid json content [[");

        ConfigManager manager = new ConfigManager(configFile);
        manager.load();

        ModConfig config = manager.getConfig();
        assertThat(config).isNotNull();
        assertThat(config.enabled).isTrue();
    }
}
