package com.ballsclient.core.module.combat.tactical;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Reach Display — Measures and renders exact attack reach distance per hit
 */
public class ReachDisplay extends Module {
    private final NumberSetting displayDurationMs = new NumberSetting("DisplayDurationMs", "Reach text fadeout duration in milliseconds", 2000.0, 500.0, 5000.0, 100.0);
    private final BooleanSetting colorCodedReach = new BooleanSetting("ColorCodedReach", "Color-code reach text based on distance threshold", true);

    public ReachDisplay() {
        super("reach-display", "Reach Display", "Measures and renders exact attack reach distance per hit", Category.COMBAT);
        addSettings(displayDurationMs, colorCodedReach);
    }

    public double getDisplayDurationMs() {
        return displayDurationMs.getValue();
    }

    public boolean isColorCodedReach() {
        return colorCodedReach.getValue();
    }

    public double computeHitDistance(double eyeX, double eyeY, double eyeZ, double targetHitX, double targetHitY, double targetHitZ) {
        double dx = targetHitX - eyeX;
        double dy = targetHitY - eyeY;
        double dz = targetHitZ - eyeZ;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
