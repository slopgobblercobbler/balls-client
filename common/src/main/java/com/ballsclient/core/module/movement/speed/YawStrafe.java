package com.ballsclient.core.module.movement.speed;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Rotates yaw towards movement vector to preserve directional speed.
 */
public class YawStrafe extends Module {
    private final BooleanSetting smoothYaw = new BooleanSetting("SmoothYaw", "Smoothly interpolate camera yaw towards target vector", true);
    private final NumberSetting maxTurnRate = new NumberSetting("MaxTurnRate", "Maximum turn rate in degrees per tick", 45.0, 10.0, 180.0, 5.0);

    public YawStrafe() {
        super("yaw-strafe", "Yaw Strafe", "Rotates yaw towards movement vector to preserve directional speed", Category.MOVEMENT);
        addSettings(smoothYaw, maxTurnRate);
    }

    public boolean isSmoothYaw() { return smoothYaw.getValue(); }
    public double getMaxTurnRate() { return maxTurnRate.getValue(); }

    public float calculateTargetYaw(double vx, double vz) {
        return (float) Math.toDegrees(Math.atan2(-vx, vz));
    }
}
