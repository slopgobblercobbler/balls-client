package com.ballsclient.core.module.movement.aerial;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Multiplies Riptide trident launch velocity and bypasses rain check.
 */
public class TridentLaunch extends Module {
    private final NumberSetting multiplier = new NumberSetting("Multiplier", "Riptide launch kinetic velocity multiplier", 1.75, 1.0, 4.0, 0.05);
    private final BooleanSetting trajectoryGuide = new BooleanSetting("TrajectoryGuide", "Smooth trajectory along camera look vector", true);

    public TridentLaunch() {
        super("trident-launch", "Trident Launch", "Multiplies Riptide trident launch velocity and bypasses rain check", Category.MOVEMENT);
        addSettings(multiplier, trajectoryGuide);
    }

    public double getMultiplier() { return multiplier.getValue(); }
    public boolean isTrajectoryGuide() { return trajectoryGuide.getValue(); }

    public double[] boostTridentImpulse(double vx, double vy, double vz) {
        if (!isEnabled()) return new double[]{vx, vy, vz};
        return new double[]{vx * getMultiplier(), vy * getMultiplier(), vz * getMultiplier()};
    }
}
