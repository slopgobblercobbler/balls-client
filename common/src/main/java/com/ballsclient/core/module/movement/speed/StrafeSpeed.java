package com.ballsclient.core.module.movement.speed;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Overrides sideways and backward strafing speed to match forward sprint.
 */
public class StrafeSpeed extends Module {
    private final NumberSetting speedMultiplier = new NumberSetting("SpeedMultiplier", "Strafe velocity multiplier", 1.35, 1.0, 2.5, 0.05);
    private final BooleanSetting onGroundOnly = new BooleanSetting("OnGroundOnly", "Only apply strafe velocity bonus while on ground", false);

    public StrafeSpeed() {
        super("strafe-speed", "Strafe Speed", "Overrides sideways and backward strafing speed to match forward sprint", Category.MOVEMENT);
        addSettings(speedMultiplier, onGroundOnly);
    }

    public double getSpeedMultiplier() { return speedMultiplier.getValue(); }
    public boolean isOnGroundOnly() { return onGroundOnly.getValue(); }

    public double[] calculateStrafeVector(float yaw, double forward, double strafe, double sprintSpeed) {
        if (!isEnabled()) return new double[]{0.0, 0.0};
        double angle = Math.toRadians(yaw + (forward == 0 ? (strafe > 0 ? 90 : -90) : (forward > 0 ? (strafe > 0 ? 45 : (strafe < 0 ? -45 : 0)) : (strafe > 0 ? 135 : (strafe < 0 ? -135 : 180)))));
        double s = sprintSpeed * getSpeedMultiplier();
        return new double[]{-Math.sin(angle) * s, Math.cos(angle) * s};
    }
}
