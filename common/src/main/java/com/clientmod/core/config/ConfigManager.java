package com.clientmod.core.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Thread-safe Configuration Manager with atomic disk persistence and fallback defaults.
 */
public class ConfigManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Path configPath;
    private volatile ModConfig config;
    private final CopyOnWriteArrayList<Consumer<ModConfig>> listeners = new CopyOnWriteArrayList<>();

    public ConfigManager() {
        this(Path.of("config", "clientmod.json"));
    }

    public ConfigManager(Path configPath) {
        this.configPath = configPath;
        this.config = new ModConfig();
    }

    public synchronized void load() {
        if (configPath != null && Files.exists(configPath)) {
            try (Reader reader = Files.newBufferedReader(configPath)) {
                ModConfig loaded = GSON.fromJson(reader, ModConfig.class);
                if (loaded != null) {
                    this.config = loaded;
                    notifyListeners();
                    return;
                }
            } catch (Exception e) {
                LOGGER.warn("Failed to parse config file at {}, using default fallback: {}", configPath, e.getMessage());
            }
        }
        this.config = new ModConfig();
        save();
    }

    public synchronized void save() {
        if (configPath == null) {
            return;
        }
        try {
            if (configPath.getParent() != null) {
                Files.createDirectories(configPath.getParent());
            }
            Path tempPath = configPath.resolveSibling(configPath.getFileName().toString() + ".tmp");
            try (Writer writer = Files.newBufferedWriter(tempPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                GSON.toJson(this.config, writer);
            }
            try {
                Files.move(tempPath, configPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING, java.nio.file.StandardCopyOption.ATOMIC_MOVE);
            } catch (Exception e) {
                Files.move(tempPath, configPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to save config to {}: {}", configPath, e.getMessage());
        }
        notifyListeners();
    }

    public ModConfig getConfig() {
        return this.config;
    }

    public void setConfig(ModConfig newConfig) {
        if (newConfig != null) {
            this.config = newConfig;
            save();
        }
    }

    public void registerListener(Consumer<ModConfig> listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    private void notifyListeners() {
        ModConfig snapshot = this.config.copy();
        for (Consumer<ModConfig> listener : listeners) {
            try {
                listener.accept(snapshot);
            } catch (Exception e) {
                LOGGER.error("Error in config listener notification", e);
            }
        }
    }
}
