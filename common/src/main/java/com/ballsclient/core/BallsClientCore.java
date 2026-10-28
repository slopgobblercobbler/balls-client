package com.ballsclient.core;

import com.ballsclient.core.event.EventBus;
import com.ballsclient.core.module.ModuleManager;
import com.clientmod.core.config.ConfigManager;
import com.clientmod.core.config.ModConfig;
import com.clientmod.core.movement.AutoSprintController;
import com.clientmod.core.movement.MacroExecutionEngine;
import com.clientmod.core.telemetry.TelemetryTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Central runtime bootstrap and service locator for Balls Client.
 */
public final class BallsClientCore {
    public static final String MOD_ID = "ballsclient";
    public static final String MOD_NAME = "BallsClient";
    public static final String VERSION = "1.0.0";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final EventBus EVENT_BUS = new EventBus();
    private static ModuleManager moduleManager;
    private static ConfigManager configManager;
    private static TelemetryTracker telemetryTracker;
    private static AutoSprintController autoSprintController;
    private static MacroExecutionEngine macroEngine;
    private static boolean initialized = false;

    private BallsClientCore() {}

    public static synchronized void init() {
        if (initialized) {
            return;
        }
        LOGGER.info("Bootstrapping {} v{} Modular Core...", MOD_NAME, VERSION);

        configManager = new ConfigManager();
        configManager.load();

        telemetryTracker = new TelemetryTracker();
        autoSprintController = new AutoSprintController();
        macroEngine = new MacroExecutionEngine();

        if (moduleManager == null) {
            moduleManager = new ModuleManager(EVENT_BUS);
            moduleManager.registerDefaultModules();
            registerAllCatalogs(moduleManager);
            moduleManager.loadConfig();
        }

        initialized = true;
        LOGGER.info("{} Core successfully bootstrapped with {} registered modules.",
                MOD_NAME, moduleManager.getModules().size());
    }

    private static void registerAllCatalogs(ModuleManager manager) {
        String[] catalogs = {
            "com.ballsclient.core.module.movement.MovementModuleCatalog",
            "com.ballsclient.core.module.combat.CombatModuleCatalog",
            "com.ballsclient.core.module.player.PlayerModuleCatalog",
            "com.ballsclient.core.module.inventory.InventoryModuleCatalog",
            "com.ballsclient.core.module.automation.AutomationModuleCatalog",
            "com.ballsclient.core.module.telemetry.TelemetryModuleCatalog"
        };
        for (String catName : catalogs) {
            try {
                Class<?> catalogClass = Class.forName(catName);
                catalogClass.getMethod("registerAll", ModuleManager.class).invoke(null, manager);
            } catch (Exception e) {
                LOGGER.error("Failed to load catalog {}: {}", catName, e.getMessage(), e);
            }
        }
    }

    public static synchronized void shutdown() {
        if (!initialized) {
            return;
        }
        LOGGER.info("Shutting down {} Core...", MOD_NAME);
        if (moduleManager != null) {
            moduleManager.saveConfig();
        }
        initialized = false;
    }

    public static EventBus getEventBus() {
        return EVENT_BUS;
    }

    public static ModuleManager getModuleManager() {
        if (moduleManager == null) {
            moduleManager = new ModuleManager(EVENT_BUS);
        }
        return moduleManager;
    }

    public static void setModuleManager(ModuleManager manager) {
        moduleManager = manager;
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
