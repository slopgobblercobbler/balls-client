package com.ballsclient.core.module.player.world;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * DaylightCycleTracker — Solar angle evaluator, bed sleep window alert, and phantom spawn warning.
 */
public class DaylightCycleTracker extends Module {
    private final BooleanSetting sleepWindowAlert = new BooleanSetting("SleepWindowAlert", "Alert when bedtime window opens (tick 12542)", true);
    private final BooleanSetting phantomWarning = new BooleanSetting("PhantomWarning", "Warn when awake ticks exceed phantom threshold", true);
    private final NumberSetting phantomThresholdTicks = new NumberSetting("PhantomThresholdTicks", "Awake ticks before phantom generation", 72000.0, 24000.0, 120000.0, 1200.0);

    public DaylightCycleTracker() {
        super("daylight-cycle-tracker", "Daylight Cycle Tracker", "Solar angle evaluator, bed sleep window alert, and phantom spawn warning", Category.PLAYER);
        addSettings(sleepWindowAlert, phantomWarning, phantomThresholdTicks);
    }

    public BooleanSetting getSleepWindowAlert() {
        return sleepWindowAlert;
    }

    public BooleanSetting getPhantomWarning() {
        return phantomWarning;
    }

    public NumberSetting getPhantomThresholdTicks() {
        return phantomThresholdTicks;
    }

    public static boolean canSleepInBed(long worldTimeOfDay) {
        long timeInDay = Math.floorMod(worldTimeOfDay, 24000L);
        return timeInDay >= 12542L && timeInDay <= 23458L;
    }

    public static boolean isPhantomThreat(long ticksAwake, double threshold) {
        return ticksAwake >= threshold;
    }
}
