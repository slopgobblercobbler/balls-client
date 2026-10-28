package com.ballsclient.core.module.movement.speed;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Progressively accelerates forward speed the longer sprint is held.
 */
public class KineticSprint extends Module {
    private final NumberSetting accelerationRate = new NumberSetting("AccelerationRate", "Speed factor increment added per sprint tick", 0.02, 0.005, 0.1, 0.005);
    private final NumberSetting maxSpeedMultiplier = new NumberSetting("MaxSpeedMultiplier", "Peak sprint velocity ceiling multiplier", 2.2, 1.1, 4.0, 0.1);

    private double currentMultiplier = 1.0;

    public KineticSprint() {
        super("kinetic-sprint", "Kinetic Sprint", "Progressively accelerates forward speed the longer sprint is held", Category.MOVEMENT);
        addSettings(accelerationRate, maxSpeedMultiplier);
    }

    public double getAccelerationRate() { return accelerationRate.getValue(); }
    public double getMaxSpeedMultiplier() { return maxSpeedMultiplier.getValue(); }
    public double getCurrentMultiplier() { return currentMultiplier; }

    public double updateSprintMultiplier(boolean sprinting) {
        if (!isEnabled() || !sprinting) {
            currentMultiplier = 1.0;
        } else {
            currentMultiplier = Math.min(getMaxSpeedMultiplier(), currentMultiplier + getAccelerationRate());
        }
        return currentMultiplier;
    }
}
