package com.clientmod.core;

import com.ballsclient.core.BallsClientCore;
import com.clientmod.core.config.ConfigManager;
import com.clientmod.core.config.ModConfig;
import com.clientmod.core.movement.AutoSprintController;
import com.clientmod.core.movement.MacroExecutionEngine;
import com.clientmod.core.telemetry.TelemetryTracker;
import org.slf4j.Logger;

/**
 * Backward compatibility facade delegating to BallsClientCore.
 */
public final class ClientModCore {
    public static final String MOD_ID = BallsClientCore.MOD_ID;
    public static final String MOD_NAME = BallsClientCore.MOD_NAME;
    public static final Logger LOGGER = BallsClientCore.LOGGER;

    private ClientModCore() {}

    public static synchronized void init() {
        BallsClientCore.init();
    }

    public static ModConfig getConfig() {
        return BallsClientCore.getConfig();
    }

    public static ConfigManager getConfigManager() {
        return BallsClientCore.getConfigManager();
    }

    public static TelemetryTracker getTelemetryTracker() {
        return BallsClientCore.getTelemetryTracker();
    }

    public static AutoSprintController getAutoSprintController() {
        return BallsClientCore.getAutoSprintController();
    }

    public static MacroExecutionEngine getMacroEngine() {
        return BallsClientCore.getMacroEngine();
    }

    public static boolean isInitialized() {
        return BallsClientCore.isInitialized();
    }
}
