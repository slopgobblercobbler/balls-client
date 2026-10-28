package com.ballsclient.core.module.combat.aim;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Fishing Rod Aim — Computes hook gravity (0.03) and initial velocity (1.5) to grapple or disrupt opponents
 */
public class FishingRodAim extends Module {
    private final NumberSetting hookVelocity = new NumberSetting("HookVelocity", "Fishing hook initial velocity", 1.5, 1.0, 2.0, 0.05);
    private final NumberSetting hookGravity = new NumberSetting("HookGravity", "Fishing hook gravitational acceleration", 0.03, 0.02, 0.05, 0.005);

    public FishingRodAim() {
        super("fishing-rod-aim", "Fishing Rod Aim", "Computes hook gravity (0.03) and initial velocity (1.5) to grapple or disrupt opponents", Category.COMBAT);
        addSettings(hookVelocity, hookGravity);
    }

    public double getHookVelocity() {
        return hookVelocity.getValue();
    }

    public double getHookGravity() {
        return hookGravity.getValue();
    }

    public double calculateHookFlightTicks(double horizontalDistance, double velocity) {
        return horizontalDistance / Math.max(0.1, velocity);
    }
}
