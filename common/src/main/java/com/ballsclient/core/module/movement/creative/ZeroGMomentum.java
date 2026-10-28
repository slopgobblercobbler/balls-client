package com.ballsclient.core.module.movement.creative;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Newtonian inertia retention where momentum persists indefinitely.
 */
public class ZeroGMomentum extends Module {
    private final NumberSetting inertiaRetention = new NumberSetting("InertiaRetention", "Momentum preservation coefficient per tick", 0.999, 0.90, 1.0, 0.001);
    private final NumberSetting thrustPower = new NumberSetting("ThrustPower", "Acceleration impulse per tick while thruster key held", 0.08, 0.01, 0.5, 0.01);
    private final BooleanSetting stabilizerKey = new BooleanSetting("StabilizerKey", "Brake thrusters to cancel inertia when stabilizer key pressed", true);

    public ZeroGMomentum() {
        super("zero-g-momentum", "Zero-G Momentum", "Newtonian inertia retention where momentum persists indefinitely", Category.MOVEMENT);
        addSettings(inertiaRetention, thrustPower, stabilizerKey);
    }

    public double getInertiaRetention() { return inertiaRetention.getValue(); }
    public double getThrustPower() { return thrustPower.getValue(); }
    public boolean isStabilizerKey() { return stabilizerKey.getValue(); }

    public double applyInertia(double v, double thrustInput) {
        if (!isEnabled()) return v;
        return v * getInertiaRetention() + thrustInput * getThrustPower();
    }
}
