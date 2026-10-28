package com.clientmod.core;

import com.clientmod.core.config.ConfigManager;
import com.clientmod.core.config.ModConfig;
import com.clientmod.core.movement.AutoSprintController;
import com.clientmod.core.movement.MacroExecutionEngine;
import com.clientmod.core.telemetry.TelemetryTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Headless Core Domain Bootstrap for ClientMod.
 * Contains ZERO Minecraft or graphics dependencies.
 */
public final class ClientModCore {
    public static final String MOD_ID = "clientmod";
    public static final String MOD_NAME = "ClientMod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static ConfigManager configManager;
    private static TelemetryTracker telemetryTracker;
    private static AutoSprintController autoSprintController;
    private static MacroExecutionEngine macroEngine;
    private static boolean initialized = false;

    private ClientModCore() {}

    public static synchronized void init() {
        if (initialized) {
            return;
        }
        LOGGER.info("Bootstrapping {} Core Domain...", MOD_NAME);

        configManager = new ConfigManager();
        configManager.load();

        telemetryTracker = new TelemetryTracker();
        autoSprintController = new AutoSprintController();
        macroEngine = new MacroExecutionEngine();

        initialized = true;
        LOGGER.info("{} Core Domain successfully initialized.", MOD_NAME);
    }

    public static ModConfig getConfig() {
        if (configManager == null) {
            return new ModConfig();
        }
        return configManager.getConfig();
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static TelemetryTracker getTelemetryTracker() {
        return telemetryTracker;
    }

    public static AutoSprintController getAutoSprintController() {
        return autoSprintController;
    }

    public static MacroExecutionEngine getMacroEngine() {
        return macroEngine;
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
