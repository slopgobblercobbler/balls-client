package com.ballsclient.core.module.automation.redstone;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * RedstoneWireLossMonitor — Highlights signal drop across wire distance and warns when wire reaches signal 0.
 */
public class RedstoneWireLossMonitor extends Module {
    private final NumberSetting warnThreshold = new NumberSetting("WarnThreshold", "Signal power alert limit", 1.0, 0.0, 5.0, 1.0);

    public RedstoneWireLossMonitor() {
        super("redstone-wire-loss-monitor", "Redstone Wire Loss Monitor", "Highlights signal drop across wire distance and warns when wire reaches signal 0", Category.AUTOMATION);
        addSettings(warnThreshold);
    }

    public NumberSetting getWarnThreshold() {
        return warnThreshold;
    }

    public static int calculateRemainingSignal(int sourceSignal, int distanceBlocks) {
        return Math.max(0, sourceSignal - distanceBlocks);
    }

    public boolean isWarning(int signal) {
        return signal <= warnThreshold.getValue();
    }
}
