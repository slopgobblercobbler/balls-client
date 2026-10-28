package com.ballsclient.core.module.automation.farming;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * SnifferEggHatcherTracker — Tracks sniffer egg placement on moss blocks and displays incubation countdown timers.
 */
public class SnifferEggHatcherTracker extends Module {
    private final BooleanSetting alertOnHatch = new BooleanSetting("AlertOnHatch", "Emits sound and screen notification when egg hatches", true);

    public SnifferEggHatcherTracker() {
        super("sniffer-egg-hatcher-tracker", "Sniffer Egg Hatcher Tracker", "Tracks sniffer egg placement on moss blocks and displays incubation countdown timers", Category.AUTOMATION);
        addSettings(alertOnHatch);
    }

    public BooleanSetting getAlertOnHatch() {
        return alertOnHatch;
    }

    public static int getTotalIncubationTicks(boolean isOnMoss) {
        // Sniffer eggs hatch in 10 minutes (12000 ticks) on moss, or 20 minutes (24000 ticks) on other blocks
        return isOnMoss ? 12000 : 24000;
    }

    public static double getRemainingSeconds(int elapsedTicks, boolean isOnMoss) {
        int total = getTotalIncubationTicks(isOnMoss);
        int rem = Math.max(0, total - elapsedTicks);
        return rem / 20.0;
    }
}
