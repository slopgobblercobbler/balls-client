package com.ballsclient.core.module.movement.aerial;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Horizontal kinetic impulse injection at jump arc peak for wide gaps.
 */
public class LongJump extends Module {
    private final NumberSetting boostSpeed = new NumberSetting("BoostSpeed", "Horizontal boost velocity scaling multiplier", 1.5, 1.1, 4.0, 0.05);
    private final NumberSetting decayRate = new NumberSetting("DecayRate", "Velocity decay factor per tick following boost", 0.95, 0.80, 0.99, 0.01);
    private final NumberSetting triggerDelayTicks = new NumberSetting("TriggerDelayTicks", "Ticks after jump before boost triggers", 2.0, 0.0, 10.0, 1.0);

    public LongJump() {
        super("long-jump", "Long Jump", "Horizontal kinetic impulse injection at jump arc peak for wide gaps", Category.MOVEMENT);
        addSettings(boostSpeed, decayRate, triggerDelayTicks);
    }

    public double getBoostSpeed() { return boostSpeed.getValue(); }
    public double getDecayRate() { return decayRate.getValue(); }
    public double getTriggerDelayTicks() { return triggerDelayTicks.getValue(); }

    public double[] applyBoost(double vx, double vz, int ticksSinceJump) {
        if (!isEnabled()) return new double[]{vx, vz};
        if (ticksSinceJump == (int) getTriggerDelayTicks()) {
            return new double[]{vx * getBoostSpeed(), vz * getBoostSpeed()};
        }
        if (ticksSinceJump > (int) getTriggerDelayTicks()) {
            return new double[]{vx * getDecayRate(), vz * getDecayRate()};
        }
        return new double[]{vx, vz};
    }
}
