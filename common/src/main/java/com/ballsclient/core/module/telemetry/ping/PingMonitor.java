package com.ballsclient.core.module.telemetry.ping;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * PingMonitor — Real-time round-trip latency HUD displaying instantaneous, minimum, maximum,
 * and average ping in milliseconds.
 */
public class PingMonitor extends Module {
    private final NumberSetting updateIntervalMs = new NumberSetting("UpdateIntervalMs", "HUD update interval in milliseconds", 500.0, 100.0, 2000.0, 50.0);
    private final BooleanSetting colorCodeThresholds = new BooleanSetting("ColorCodeThresholds", "Color-codes ping based on latency quality thresholds", true);
    private final BooleanSetting showMinMax = new BooleanSetting("ShowMinMax", "Displays session minimum and maximum latency values", true);

    public PingMonitor() {
        super("ping-monitor", "Ping Monitor", "Real-time round-trip latency HUD displaying instantaneous, minimum, maximum, and average ping in milliseconds.", Category.NETWORK);
        addSettings(updateIntervalMs, colorCodeThresholds, showMinMax);
    }

    public NumberSetting getUpdateIntervalMs() {
        return updateIntervalMs;
    }

    public BooleanSetting getColorCodeThresholds() {
        return colorCodeThresholds;
    }

    public BooleanSetting getShowMinMax() {
        return showMinMax;
    }
}
