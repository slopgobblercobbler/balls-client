package com.ballsclient.core;

import com.ballsclient.core.event.EventBus;
import com.ballsclient.core.module.ModuleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Central runtime bootstrap and service locator for Balls Client.
 */
public final class BallsClientCore {
    public static final String MOD_ID = "ballsclient";
    public static final String MOD_NAME = "Balls Client";
    public static final String VERSION = "1.0.0";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final EventBus EVENT_BUS = new EventBus();
    private static ModuleManager moduleManager;
    private static boolean initialized = false;

    private BallsClientCore() {}

    public static synchronized void init() {
        if (initialized) {
            return;
        }
        LOGGER.info("Bootstrapping {} v{} Modular Core...", MOD_NAME, VERSION);

        if (moduleManager == null) {
            moduleManager = new ModuleManager(EVENT_BUS);
            moduleManager.registerDefaultModules();
            moduleManager.loadConfig();
        }

        initialized = true;
        LOGGER.info("{} Core successfully bootstrapped with {} registered modules.",
                MOD_NAME, moduleManager.getModules().size());
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

    public static boolean isInitialized() {
        return initialized;
    }
}
