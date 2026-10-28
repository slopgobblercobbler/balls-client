package com.ballsclient.core.module.movement.creative;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Short-range forward teleport dash through thin 1-block barriers.
 */
public class PhaseDash extends Module {
    private final NumberSetting dashDistance = new NumberSetting("DashDistance", "Dash teleport distance in blocks", 3.0, 1.0, 8.0, 0.5);
    private final NumberSetting cooldownTicks = new NumberSetting("CooldownTicks", "Cooldown interval in ticks between phase dashes", 30.0, 10.0, 100.0, 5.0);

    public PhaseDash() {
        super("phase-dash", "Phase Dash", "Short-range forward teleport dash through thin 1-block barriers", Category.MOVEMENT);
        addSettings(dashDistance, cooldownTicks);
    }

    public double getDashDistance() { return dashDistance.getValue(); }
    public double getCooldownTicks() { return cooldownTicks.getValue(); }

    public double[] calculateDashTarget(double posX, double posY, double posZ, float yaw, float pitch) {
        if (!isEnabled()) return new double[]{posX, posY, posZ};
        double yRad = Math.toRadians(yaw);
        double pRad = Math.toRadians(pitch);
        double dx = -Math.sin(yRad) * Math.cos(pRad) * getDashDistance();
        double dy = -Math.sin(pRad) * getDashDistance();
        double dz = Math.cos(yRad) * Math.cos(pRad) * getDashDistance();
        return new double[]{posX + dx, posY + dy, posZ + dz};
    }
}
