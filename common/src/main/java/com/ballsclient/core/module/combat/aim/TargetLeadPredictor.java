package com.ballsclient.core.module.combat.aim;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Target Lead Predictor — Extrapolates enemy 3D velocity and acceleration vectors to predict position at future ticks
 */
public class TargetLeadPredictor extends Module {
    private final NumberSetting ticksAhead = new NumberSetting("TicksAhead", "Prediction lookahead ticks", 5.0, 1.0, 20.0, 1.0);
    private final BooleanSetting groundClamp = new BooleanSetting("GroundClamp", "Clamp predicted position to ground if falling", true);

    public TargetLeadPredictor() {
        super("target-lead-predictor", "Target Lead Predictor", "Extrapolates enemy 3D velocity and acceleration vectors to predict position at future ticks", Category.COMBAT);
        addSettings(ticksAhead, groundClamp);
    }

    public double getTicksAhead() {
        return ticksAhead.getValue();
    }

    public boolean isGroundClamp() {
        return groundClamp.getValue();
    }

    public double[] predictFutureCoordinates(double x, double y, double z, double vx, double vy, double vz, int ticks, boolean groundClamp, double groundY) {
        double predX = x + vx * ticks;
        double predY = y + vy * ticks - 0.5 * 0.08 * ticks * ticks;
        double predZ = z + vz * ticks;
        if (groundClamp && predY < groundY) predY = groundY;
        return new double[]{predX, predY, predZ};
    }
}
