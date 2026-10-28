package com.ballsclient.core.module.automation.redstone;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * TargetBlockHitDetector — Displays projectile hit distance from target block center and resulting signal (1-15).
 */
public class TargetBlockHitDetector extends Module {
    private final BooleanSetting showDistanceMillimeters = new BooleanSetting("ShowDistanceMillimeters", "Displays impact offset distance in millimeters", true);

    public TargetBlockHitDetector() {
        super("target-block-hit-detector", "Target Block Hit Detector", "Displays projectile hit distance from target block center and resulting signal (1-15)", Category.AUTOMATION);
        addSettings(showDistanceMillimeters);
    }

    public BooleanSetting getShowDistanceMillimeters() {
        return showDistanceMillimeters;
    }

    public static int calculateSignalStrength(double hitOffsetX, double hitOffsetY) {
        double r = Math.sqrt(hitOffsetX * hitOffsetX + hitOffsetY * hitOffsetY);
        if (r >= 0.5) {
            return 1;
        }
        double factor = 1.0 - (r / 0.5);
        int signal = (int) Math.ceil(15.0 * factor);
        return Math.max(1, Math.min(15, signal));
    }
}
