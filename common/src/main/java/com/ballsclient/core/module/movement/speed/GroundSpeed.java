package com.ballsclient.core.module.movement.speed;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Multiplies flat-ground running velocity without jumping.
 */
public class GroundSpeed extends Module {
    private final NumberSetting speedFactor = new NumberSetting("SpeedFactor", "Ground running velocity boost multiplier", 1.30, 1.0, 2.5, 0.05);
    private final BooleanSetting frictionComp = new BooleanSetting("FrictionComp", "Compensate ground friction deceleration", true);

    public GroundSpeed() {
        super("ground-speed", "Ground Speed", "Multiplies flat-ground running velocity without jumping", Category.MOVEMENT);
        addSettings(speedFactor, frictionComp);
    }

    public double getSpeedFactor() { return speedFactor.getValue(); }
    public boolean isFrictionComp() { return frictionComp.getValue(); }

    public double accelerateGroundSpeed(double speed) {
        if (!isEnabled()) return speed;
        return speed * getSpeedFactor();
    }
}
