package com.ballsclient.core.module.telemetry.tps;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * WorldTimeDriftDetector — Compares server world-time advancement against local real-time elapsed seconds
 * to measure long-term cumulative server time lag.
 */
public class WorldTimeDriftDetector extends Module {
    private final NumberSetting alertThresholdSeconds = new NumberSetting("AlertThresholdSeconds", "Cumulative drift warning threshold in seconds", 10.0, 2.0, 60.0, 2.0);
    private final BooleanSetting autoRecalibrate = new BooleanSetting("AutoRecalibrate", "Recalibrates baseline drift reference upon day-night transitions", true);
    private final BooleanSetting showDriftRate = new BooleanSetting("ShowDriftRate", "Displays tick drift accumulation rate per minute", true);

    public WorldTimeDriftDetector() {
        super("world-time-drift-detector", "World Time Drift Detector", "Compares server world-time advancement against local real-time elapsed seconds to measure long-term cumulative server time lag.", Category.NETWORK);
        addSettings(alertThresholdSeconds, autoRecalibrate, showDriftRate);
    }

    public NumberSetting getAlertThresholdSeconds() {
        return alertThresholdSeconds;
    }

    public BooleanSetting getAutoRecalibrate() {
        return autoRecalibrate;
    }

    public BooleanSetting getShowDriftRate() {
        return showDriftRate;
    }
}
