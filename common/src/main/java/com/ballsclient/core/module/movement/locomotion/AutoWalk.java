package com.ballsclient.core.module.movement.locomotion;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * Locks forward traversal input continuously for AFK travel.
 */
public class AutoWalk extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", "Traversal routing mode", "LockForward", "LockForward", "SmartPath", "KeySimulate");
    private final BooleanSetting autoUnstuck = new BooleanSetting("AutoUnstuck", "Pulse jump when colliding with obstacles", true);

    public AutoWalk() {
        super("auto-walk", "Auto Walk", "Locks forward traversal input continuously for AFK travel", Category.MOVEMENT);
        addSettings(mode, autoUnstuck);
    }

    public String getMode() { return mode.getValue(); }
    public boolean isAutoUnstuck() { return autoUnstuck.getValue(); }

    public double getForwardMovement() {
        return isEnabled() ? 1.0 : 0.0;
    }

    public boolean checkStuck(int consecutiveStuckTicks) {
        return isEnabled() && isAutoUnstuck() && consecutiveStuckTicks >= 10;
    }
}
