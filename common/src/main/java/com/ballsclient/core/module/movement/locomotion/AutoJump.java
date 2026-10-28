package com.ballsclient.core.module.movement.locomotion;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Automatically jumps when encountering block elevation changes.
 */
public class AutoJump extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", "Auto jump trigger behavior", "Obstacle", "Obstacle", "Edge", "Continuous");
    private final NumberSetting minVelocity = new NumberSetting("MinVelocity", "Minimum horizontal velocity required to jump", 0.10, 0.0, 1.0, 0.05);
    private final BooleanSetting airCheck = new BooleanSetting("AirCheck", "Verify head space is clear before jumping", true);

    public AutoJump() {
        super("auto-jump", "Auto Jump", "Automatically jumps when encountering block elevation changes", Category.MOVEMENT);
        addSettings(mode, minVelocity, airCheck);
    }

    public String getMode() { return mode.getValue(); }
    public double getMinVelocity() { return minVelocity.getValue(); }
    public boolean isAirCheck() { return airCheck.getValue(); }

    public boolean shouldJump(double obstacleHeight, boolean hasHeadSpace, boolean onGround, double currentVelocity) {
        if (!isEnabled() || !onGround || currentVelocity < getMinVelocity()) return false;
        if (isAirCheck() && !hasHeadSpace) return false;
        if ("Continuous".equalsIgnoreCase(getMode())) return true;
        if ("Obstacle".equalsIgnoreCase(getMode())) return obstacleHeight >= 0.5 && obstacleHeight <= 1.25;
        return true;
    }
}
