package com.ballsclient.core.module.player.utility;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * EndCrystalSafetySensor — Calculates distance and terrain line-of-sight exposure to nearby End Crystals.
 */
public class EndCrystalSafetySensor extends Module {
    private final NumberSetting lethalDistanceThreshold = new NumberSetting("LethalDistanceThreshold", "Distance below which unblocked crystals are lethal", 6.0, 2.0, 12.0, 0.5);
    private final BooleanSetting alertObstructionMissing = new BooleanSetting("AlertObstructionMissing", "Warn when line of sight to crystal is unblocked by obsidian", true);

    public EndCrystalSafetySensor() {
        super("end-crystal-safety-sensor", "End Crystal Safety Sensor", "Calculates distance and terrain line-of-sight exposure to nearby End Crystals", Category.PLAYER);
        addSettings(lethalDistanceThreshold, alertObstructionMissing);
    }

    public NumberSetting getLethalDistanceThreshold() {
        return lethalDistanceThreshold;
    }

    public BooleanSetting getAlertObstructionMissing() {
        return alertObstructionMissing;
    }

    public static boolean isExposedToCrystal(double distance, boolean hasFootBlocker, double lethalThreshold) {
        if (hasFootBlocker) return false;
        return distance <= lethalThreshold;
    }
}
