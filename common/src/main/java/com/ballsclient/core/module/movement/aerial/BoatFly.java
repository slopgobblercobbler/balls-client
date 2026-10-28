package com.ballsclient.core.module.movement.aerial;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Overrides boat physics to allow 3D flight while riding boats.
 */
public class BoatFly extends Module {
    private final NumberSetting speed = new NumberSetting("Speed", "Horizontal flight speed while riding boat", 1.5, 0.5, 5.0, 0.1);
    private final NumberSetting verticalSpeed = new NumberSetting("VerticalSpeed", "Ascent/descent flight speed", 0.7, 0.1, 2.5, 0.05);
    private final BooleanSetting glideDown = new BooleanSetting("GlideDown", "Slowly glide downward when no control keys are pressed", false);

    public BoatFly() {
        super("boat-fly", "Boat Fly", "Overrides boat physics to allow 3D flight while riding boats", Category.MOVEMENT);
        addSettings(speed, verticalSpeed, glideDown);
    }

    public double getSpeed() { return speed.getValue(); }
    public double getVerticalSpeed() { return verticalSpeed.getValue(); }
    public boolean isGlideDown() { return glideDown.getValue(); }

    public double[] calculateBoatMotion(float yaw, double forwardInput, double strafeInput, boolean up, boolean down) {
        if (!isEnabled()) return new double[]{0.0, 0.0, 0.0};
        double rad = Math.toRadians(yaw);
        double vx = (-Math.sin(rad) * forwardInput + Math.cos(rad) * strafeInput) * getSpeed();
        double vz = (Math.cos(rad) * forwardInput + Math.sin(rad) * strafeInput) * getSpeed();
        double vy = up ? getVerticalSpeed() : (down ? -getVerticalSpeed() : (isGlideDown() ? -0.05 : 0.0));
        return new double[]{vx, vy, vz};
    }
}
