package com.ballsclient.core.module.movement.speed;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Strafe bunny-hopping engine that accelerates with consecutive jumps.
 */
public class BunnyHop extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", "Bunny hop acceleration algorithm", "Legit", "Legit", "NCP", "Matrix", "Strafe");
    private final NumberSetting speedFactor = new NumberSetting("SpeedFactor", "Speed multiplier applied upon consecutive jump", 1.42, 1.0, 3.0, 0.02);
    private final NumberSetting airControl = new NumberSetting("AirControl", "Mid-air steering retention factor", 0.98, 0.5, 1.0, 0.01);
    private final NumberSetting jumpHeight = new NumberSetting("JumpHeight", "Vertical jump velocity impulse", 0.42, 0.2, 0.8, 0.02);

    public BunnyHop() {
        super("bunny-hop", "Bunny Hop", "Strafe bunny-hopping engine that accelerates with consecutive jumps", Category.MOVEMENT);
        addSettings(mode, speedFactor, airControl, jumpHeight);
    }

    public String getMode() { return mode.getValue(); }
    public double getSpeedFactor() { return speedFactor.getValue(); }
    public double getAirControl() { return airControl.getValue(); }
    public double getJumpHeight() { return jumpHeight.getValue(); }

    public double[] calculateHopVelocity(double speed, float yaw) {
        if (!isEnabled()) return new double[]{0.0, 0.0, 0.0};
        double s = speed * getSpeedFactor();
        double rad = Math.toRadians(yaw);
        return new double[]{-s * Math.sin(rad), getJumpHeight(), s * Math.cos(rad)};
    }
}
