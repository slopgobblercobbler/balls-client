package com.ballsclient.core.module.player.world;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * VoidSafetyRing — Computes fall velocity and time-to-void perimeter warning below Y=-64.
 */
public class VoidSafetyRing extends Module {
    private final NumberSetting voidThresholdY = new NumberSetting("VoidThresholdY", "Void floor threshold elevation", -64.0, -128.0, 0.0, 1.0);
    private final NumberSetting warningDistance = new NumberSetting("WarningDistance", "Distance from void floor to activate alert", 15.0, 5.0, 50.0, 1.0);
    private final BooleanSetting predictFallVelocity = new BooleanSetting("PredictFallVelocity", "Compute dynamic trajectory including gravity", true);
    private final BooleanSetting autoClutch = new BooleanSetting("AutoClutch", "Activate clutch mitigation at boundary", true);

    public VoidSafetyRing() {
        super("void-safety-ring", "Void Safety Ring", "Computes fall velocity and time-to-void perimeter warning below Y=-64", Category.PLAYER);
        addSettings(voidThresholdY, warningDistance, predictFallVelocity, autoClutch);
    }

    public NumberSetting getVoidThresholdY() {
        return voidThresholdY;
    }

    public NumberSetting getWarningDistance() {
        return warningDistance;
    }

    public BooleanSetting getPredictFallVelocity() {
        return predictFallVelocity;
    }

    public BooleanSetting getAutoClutch() {
        return autoClutch;
    }

    public static double calculateTicksToVoid(double currentY, double vy, double voidThresholdY) {
        if (currentY <= voidThresholdY) return 0.0;
        if (vy >= 0) {
            // Upward or stationary: rough estimate under gravity ~0.08 blocks/tick^2
            double peakY = currentY + (vy * vy) / 0.16;
            return (vy / 0.08) + Math.sqrt((peakY - voidThresholdY) / 0.04);
        }
        // Downward velocity
        double dist = currentY - voidThresholdY;
        return dist / Math.abs(vy);
    }

    public static boolean isVoidImminent(double currentY, double vy, double voidThresholdY, double warningTicks) {
        double ticks = calculateTicksToVoid(currentY, vy, voidThresholdY);
        return ticks <= warningTicks;
    }
}
