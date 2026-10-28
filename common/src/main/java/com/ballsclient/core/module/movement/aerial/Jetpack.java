package com.ballsclient.core.module.movement.aerial;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Continuous upward thrust simulating rocket jetpack while jumping.
 */
public class Jetpack extends Module {
    private final NumberSetting thrustPower = new NumberSetting("ThrustPower", "Vertical acceleration thrust per tick", 0.42, 0.1, 1.5, 0.02);
    private final NumberSetting maxVerticalSpeed = new NumberSetting("MaxVerticalSpeed", "Maximum vertical ascent speed clamp", 1.2, 0.3, 3.0, 0.1);
    private final BooleanSetting fuelSimulation = new BooleanSetting("FuelSimulation", "Simulate fuel burn rate with cooldown", false);

    public Jetpack() {
        super("jetpack", "Jetpack", "Continuous upward thrust simulating rocket jetpack while jumping", Category.MOVEMENT);
        addSettings(thrustPower, maxVerticalSpeed, fuelSimulation);
    }

    public double getThrustPower() { return thrustPower.getValue(); }
    public double getMaxVerticalSpeed() { return maxVerticalSpeed.getValue(); }
    public boolean isFuelSimulation() { return fuelSimulation.getValue(); }

    public double calculateThrustVelocity(double currentVy, boolean jumping) {
        if (!isEnabled() || !jumping) return currentVy;
        return Math.min(getMaxVerticalSpeed(), currentVy + getThrustPower());
    }
}
