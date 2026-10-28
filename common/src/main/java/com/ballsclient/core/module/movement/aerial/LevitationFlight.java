package com.ballsclient.core.module.movement.aerial;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Modulates levitation effect into full 3D directional hover flight.
 */
public class LevitationFlight extends Module {
    private final NumberSetting controlSpeed = new NumberSetting("ControlSpeed", "Horizontal and vertical flight speed", 0.35, 0.1, 1.0, 0.05);
    private final BooleanSetting hoverLock = new BooleanSetting("HoverLock", "Lock altitude in place when vertical controls are neutral", true);

    public LevitationFlight() {
        super("levitation-flight", "Levitation Flight", "Modulates levitation effect into full 3D directional hover flight", Category.MOVEMENT);
        addSettings(controlSpeed, hoverLock);
    }

    public double getControlSpeed() { return controlSpeed.getValue(); }
    public boolean isHoverLock() { return hoverLock.getValue(); }

    public double[] calculateFlightVector(float yaw, double forward, double strafe, boolean jump, boolean sneak) {
        if (!isEnabled()) return new double[]{0.0, 0.0, 0.0};
        double rad = Math.toRadians(yaw);
        double vx = (-Math.sin(rad) * forward + Math.cos(rad) * strafe) * getControlSpeed();
        double vz = (Math.cos(rad) * forward + Math.sin(rad) * strafe) * getControlSpeed();
        double vy = jump ? getControlSpeed() : (sneak ? -getControlSpeed() : (isHoverLock() ? 0.0 : 0.05));
        return new double[]{vx, vy, vz};
    }
}
