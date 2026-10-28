package com.ballsclient.core.module.movement.speed;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Instant kinetic velocity pulse upon activating sprint with cooldown.
 */
public class SprintBurst extends Module {
    private final NumberSetting burstForce = new NumberSetting("BurstForce", "Impulse multiplier applied upon starting sprint", 1.8, 1.1, 3.5, 0.1);
    private final NumberSetting cooldownSeconds = new NumberSetting("CooldownSeconds", "Cooldown interval in seconds between bursts", 3.0, 0.5, 10.0, 0.5);

    private long lastBurstTimestamp = 0;

    public SprintBurst() {
        super("sprint-burst", "Sprint Burst", "Instant kinetic velocity pulse upon activating sprint with cooldown", Category.MOVEMENT);
        addSettings(burstForce, cooldownSeconds);
    }

    public double getBurstForce() { return burstForce.getValue(); }
    public double getCooldownSeconds() { return cooldownSeconds.getValue(); }

    public double[] triggerBurst(double vx, double vz, long currentTimestampMs) {
        if (!isEnabled()) return new double[]{vx, vz};
        long cooldownMs = (long) (getCooldownSeconds() * 1000.0);
        if (currentTimestampMs - lastBurstTimestamp >= cooldownMs) {
            lastBurstTimestamp = currentTimestampMs;
            return new double[]{vx * getBurstForce(), vz * getBurstForce()};
        }
        return new double[]{vx, vz};
    }
}
