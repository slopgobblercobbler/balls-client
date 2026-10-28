package com.ballsclient.core.module.combat.aim;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Headshot Tracker — Biases projectile pitch toward upper 20% of entity bounding box for critical hit calculation
 */
public class HeadshotTracker extends Module {
    private final NumberSetting headRatio = new NumberSetting("HeadRatio", "Height ratio for head targeting", 0.85, 0.7, 0.95, 0.01);
    private final BooleanSetting compensationForDrop = new BooleanSetting("CompensationForDrop", "Auto-elevate pitch for ballistic drop", true);

    public HeadshotTracker() {
        super("headshot-tracker", "Headshot Tracker", "Biases projectile pitch toward upper 20% of entity bounding box for critical hit calculation", Category.COMBAT);
        addSettings(headRatio, compensationForDrop);
    }

    public double getHeadRatio() {
        return headRatio.getValue();
    }

    public boolean isCompensationForDrop() {
        return compensationForDrop.getValue();
    }

    public double calculateAimElevation(double eyeY, double targetMinY, double targetMaxY, double ratio) {
        double targetAimY = targetMinY + (targetMaxY - targetMinY) * ratio;
        return targetAimY - eyeY;
    }
}
