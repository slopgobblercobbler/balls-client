package com.ballsclient.core.module.movement.aerial;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Multi-mode elytra flight controller (Control, Boost, Static, PitchLock).
 */
public class ElytraFly extends Module {
    private final ModeSetting flightMode = new ModeSetting("FlightMode", "Elytra flight algorithm", "Control", "Control", "Boost", "StaticVector", "PitchLock");
    private final NumberSetting horizontalSpeed = new NumberSetting("HorizontalSpeed", "Horizontal cruising speed multiplier", 1.8, 0.5, 5.0, 0.1);
    private final NumberSetting verticalSpeed = new NumberSetting("VerticalSpeed", "Vertical climb/descent speed multiplier", 0.8, 0.1, 3.0, 0.05);
    private final BooleanSetting easyTakeoff = new BooleanSetting("EasyTakeoff", "Auto-jump and open wings on ground jump", true);

    public ElytraFly() {
        super("elytra-fly", "Elytra Fly", "Multi-mode elytra flight controller (Control, Boost, Static, PitchLock)", Category.MOVEMENT);
        addSettings(flightMode, horizontalSpeed, verticalSpeed, easyTakeoff);
    }

    public String getFlightMode() { return flightMode.getValue(); }
    public double getHorizontalSpeed() { return horizontalSpeed.getValue(); }
    public double getVerticalSpeed() { return verticalSpeed.getValue(); }
    public boolean isEasyTakeoff() { return easyTakeoff.getValue(); }

    public double[] calculateFlightVelocity(float yaw, boolean jump, boolean sneak) {
        if (!isEnabled()) return new double[]{0.0, 0.0, 0.0};
        double rad = Math.toRadians(yaw);
        double vx = -Math.sin(rad) * getHorizontalSpeed();
        double vz = Math.cos(rad) * getHorizontalSpeed();
        double vy = jump ? getVerticalSpeed() : (sneak ? -getVerticalSpeed() : 0.0);
        return new double[]{vx, vy, vz};
    }
}
