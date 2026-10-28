package com.ballsclient.core.module.movement.creative;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Parkour wall-running alongside walls before gravity resumes.
 */
public class WallRun extends Module {
    private final NumberSetting maxRunDurationTicks = new NumberSetting("MaxRunDurationTicks", "Maximum ticks wall-run can be maintained", 40.0, 10.0, 120.0, 5.0);
    private final NumberSetting runSpeed = new NumberSetting("RunSpeed", "Horizontal sprinting speed along wall surface", 1.35, 1.0, 2.5, 0.05);
    private final BooleanSetting tiltCamera = new BooleanSetting("TiltCamera", "Tilt camera sideways toward wall surface", true);

    public WallRun() {
        super("wall-run", "Wall Run", "Parkour wall-running alongside walls before gravity resumes", Category.MOVEMENT);
        addSettings(maxRunDurationTicks, runSpeed, tiltCamera);
    }

    public double getMaxRunDurationTicks() { return maxRunDurationTicks.getValue(); }
    public double getRunSpeed() { return runSpeed.getValue(); }
    public boolean isTiltCamera() { return tiltCamera.getValue(); }

    public boolean canWallRun(boolean againstWall, int currentTicks) {
        return isEnabled() && againstWall && currentTicks < (int) getMaxRunDurationTicks();
    }

    public double getWallRunSpeed() {
        return isEnabled() ? getRunSpeed() : 1.0;
    }
}
