package com.ballsclient.core.module.movement.locomotion;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * Automatically forces sprint without double-tapping W.
 */
public class AutoSprint extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", "Sprint activation mode", "Vanilla", "Vanilla", "OmniDirectional", "HungerConserving");
    private final BooleanSetting checkHunger = new BooleanSetting("CheckHunger", "Disable sprinting when food level is 6 or below", true);
    private final BooleanSetting checkBlindness = new BooleanSetting("CheckBlindness", "Disable sprinting when affected by Blindness", true);
    private final BooleanSetting checkCollisions = new BooleanSetting("CheckCollisions", "Stop sprint if colliding horizontally", true);

    public AutoSprint() {
        super("auto-sprint", "Auto Sprint", "Automatically forces sprint without double-tapping W", Category.MOVEMENT);
        addSettings(mode, checkHunger, checkBlindness, checkCollisions);
    }

    public String getMode() { return mode.getValue(); }
    public boolean isCheckHunger() { return checkHunger.getValue(); }
    public boolean isCheckBlindness() { return checkBlindness.getValue(); }
    public boolean isCheckCollisions() { return checkCollisions.getValue(); }

    public boolean shouldSprint(double forwardInput, double strafeInput, int foodLevel, boolean isBlind, boolean isColliding) {
        if (!isEnabled()) return false;
        if (isCheckBlindness() && isBlind) return false;
        if (isCheckCollisions() && isColliding) return false;
        if (isCheckHunger() && foodLevel <= 6) return false;
        if ("OmniDirectional".equalsIgnoreCase(getMode())) {
            return Math.abs(forwardInput) > 0.0 || Math.abs(strafeInput) > 0.0;
        }
        return forwardInput > 0.0;
    }
}
