package com.ballsclient.core.module.movement.locomotion;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Elastic bungee spring physics anchored to target block/entity.
 */
public class TetherLocomotion extends Module {
    private final NumberSetting springK = new NumberSetting("SpringK", "Hooke's spring stiffness constant", 0.15, 0.01, 0.5, 0.01);
    private final NumberSetting damping = new NumberSetting("Damping", "Spring oscillation damping factor", 0.08, 0.01, 0.3, 0.01);
    private final NumberSetting maxTetherLength = new NumberSetting("MaxTetherLength", "Maximum spring extension reach in blocks", 32.0, 8.0, 64.0, 2.0);

    public TetherLocomotion() {
        super("tether-locomotion", "Tether Locomotion", "Elastic bungee spring physics anchored to target block/entity", Category.MOVEMENT);
        addSettings(springK, damping, maxTetherLength);
    }

    public double getSpringK() { return springK.getValue(); }
    public double getDamping() { return damping.getValue(); }
    public double getMaxTetherLength() { return maxTetherLength.getValue(); }

    public double calculateSpringForce(double currentDist, double equilibriumLength, double velocity) {
        if (!isEnabled()) return 0.0;
        double clampedDist = Math.min(currentDist, getMaxTetherLength());
        double delta = clampedDist - equilibriumLength;
        return -getSpringK() * delta - getDamping() * velocity;
    }
}
