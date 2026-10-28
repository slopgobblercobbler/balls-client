package com.ballsclient.core.persistence;

import com.ballsclient.core.event.EventBus;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.ModuleManager;
import com.ballsclient.core.module.render.camera.SmoothZoom;
import com.ballsclient.core.module.render.hud.CoordinatesHUD;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("Module Config JSON Serialization & Recovery Test Suite")
class ModuleSerializationTest {

    @Test
    @DisplayName("Persistence: 105+ modules JSON serialization and atomic reload fidelity")
    void testRoundTripSerialization(@TempDir Path tempDir) {
        Path configFile = tempDir.resolve("modules.json");
        EventBus bus = new EventBus();
        ModuleManager saveManager = new ModuleManager(bus, configFile);
        saveManager.registerDefaultModules();

        // Mutate CoordinatesHUD
        CoordinatesHUD coords = saveManager.getModule(CoordinatesHUD.class);
        coords.setEnabled(true);
        coords.<BooleanSetting>getSetting("Nether").setValue(false);
        coords.<ColorSetting>getSetting("Color").setValue(0xFF00AAFF);

        // Mutate SmoothZoom
        SmoothZoom zoom = saveManager.getModule(SmoothZoom.class);
        zoom.setEnabled(true);
        zoom.<NumberSetting>getSetting("ZoomFactor").setValue(6.5);

        // Save to disk
        saveManager.saveConfig();
        assertThat(Files.exists(configFile)).isTrue();
        assertThat(Files.exists(configFile.resolveSibling("modules.json.tmp"))).isFalse();

        // Create fresh manager instance to verify disk reload
        ModuleManager loadManager = new ModuleManager(bus, configFile);
        loadManager.registerDefaultModules();

        // Initial state before load is default
        assertThat(loadManager.getModule(CoordinatesHUD.class).isEnabled()).isFalse();

        // Load from disk
        loadManager.loadConfig();

        // Verify restored states
        CoordinatesHUD restoredCoords = loadManager.getModule(CoordinatesHUD.class);
        assertThat(restoredCoords.isEnabled()).isTrue();
        assertThat(restoredCoords.<BooleanSetting>getSetting("Nether").getValue()).isFalse();
        assertThat(restoredCoords.<ColorSetting>getSetting("Color").getValue()).isEqualTo(0xFF00AAFF);

        SmoothZoom restoredZoom = loadManager.getModule(SmoothZoom.class);
        assertThat(restoredZoom.isEnabled()).isTrue();
        assertThat(restoredZoom.<NumberSetting>getSetting("ZoomFactor").getValue()).isEqualTo(6.5);
    }

    @Test
    @DisplayName("Persistence: Missing config file does not throw or corrupt defaults")
    void testMissingConfigFileHandling(@TempDir Path tempDir) {
        Path missingFile = tempDir.resolve("non_existent_dir").resolve("missing_config.json");
        EventBus bus = new EventBus();
        ModuleManager manager = new ModuleManager(bus, missingFile);
        manager.registerDefaultModules();

        assertThatCode(manager::loadConfig).doesNotThrowAnyException();
        for (Module m : manager.getModules()) {
            assertThat(m.isEnabled()).isFalse();
        }
    }

    @Test
    @DisplayName("Recovery: Corrupted or malformed JSON falls back gracefully to defaults")
    void testCorruptedConfigFileFallback(@TempDir Path tempDir) throws IOException {
        Path corruptFile = tempDir.resolve("corrupted.json");
        Files.writeString(corruptFile, "{ \"modules\": { \"coordinates-hud\": { \"enabled\": true, INVALID_JSON");

        EventBus bus = new EventBus();
        ModuleManager manager = new ModuleManager(bus, corruptFile);
        manager.registerDefaultModules();

        assertThatCode(manager::loadConfig).doesNotThrowAnyException();
        assertThat(manager.getModule(CoordinatesHUD.class).isEnabled()).isFalse();
    }
}
