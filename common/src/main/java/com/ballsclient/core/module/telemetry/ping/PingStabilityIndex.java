package com.ballsclient.core.module.telemetry.ping;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * PingStabilityIndex — Evaluates connection consistency score (0-100%) computed
 * from the coefficient of variation of round-trip times.
 */
public class PingStabilityIndex extends Module {
    private final NumberSetting windowSize = new NumberSetting("WindowSize", "Sample window size for stability score calculation", 30.0, 10.0, 100.0, 5.0);
    private final NumberSetting smoothingFactor = new NumberSetting("SmoothingFactor", "Smoothing factor for stability calculation", 0.15, 0.05, 0.5, 0.05);
    private final BooleanSetting lowStabilityAlert = new BooleanSetting("LowStabilityAlert", "Alerts when stability index drops below acceptable threshold", true);

    public PingStabilityIndex() {
        super("ping-stability-index", "Ping Stability Index", "Evaluates connection consistency score (0-100%) computed from the coefficient of variation of round-trip times.", Category.NETWORK);
        addSettings(windowSize, smoothingFactor, lowStabilityAlert);
    }

    public NumberSetting getWindowSize() {
        return windowSize;
    }

    public NumberSetting getSmoothingFactor() {
        return smoothingFactor;
    }

    public BooleanSetting getLowStabilityAlert() {
        return lowStabilityAlert;
    }
}
