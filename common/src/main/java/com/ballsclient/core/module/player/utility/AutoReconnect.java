package com.ballsclient.core.module.player.utility;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * AutoReconnect — Detects disconnects and executes exponential backoff auto-rejoin sequences.
 */
public class AutoReconnect extends Module {
    private final NumberSetting initialDelaySeconds = new NumberSetting("InitialDelaySeconds", "First reconnect attempt delay in seconds", 5.0, 1.0, 30.0, 1.0);
    private final NumberSetting maxBackoffSeconds = new NumberSetting("MaxBackoffSeconds", "Maximum retry backoff clamp duration", 60.0, 10.0, 300.0, 5.0);
    private final BooleanSetting reconnectOnKick = new BooleanSetting("ReconnectOnKick", "Attempt reconnect if kicked for AFK or restart", true);

    public AutoReconnect() {
        super("auto-reconnect", "Auto Reconnect", "Detects disconnects and executes exponential backoff auto-rejoin sequences", Category.PLAYER);
        addSettings(initialDelaySeconds, maxBackoffSeconds, reconnectOnKick);
    }

    public NumberSetting getInitialDelaySeconds() {
        return initialDelaySeconds;
    }

    public NumberSetting getMaxBackoffSeconds() {
        return maxBackoffSeconds;
    }

    public BooleanSetting getReconnectOnKick() {
        return reconnectOnKick;
    }

    public static double computeBackoff(int attempt, double baseDelay, double maxDelay) {
        return Math.min(maxDelay, baseDelay * Math.pow(1.5, attempt));
    }
}
