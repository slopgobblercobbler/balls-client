package com.ballsclient.core.module.movement.creative;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Climbs any solid vertical wall without ladders or vines.
 */
public class Spider extends Module {
    private final NumberSetting climbSpeed = new NumberSetting("ClimbSpeed", "Vertical climb speed along solid walls", 0.28, 0.1, 1.0, 0.02);
    private final BooleanSetting wallStick = new BooleanSetting("WallStick", "Prevent slipping backward while ascending", true);

    public Spider() {
        super("spider", "Spider", "Climbs any solid vertical wall without ladders or vines", Category.MOVEMENT);
        addSettings(climbSpeed, wallStick);
    }

    public double getClimbSpeed() { return climbSpeed.getValue(); }
    public boolean isWallStick() { return wallStick.getValue(); }

    public double getClimbVelocity(boolean horizontalCollision) {
        return (isEnabled() && horizontalCollision) ? getClimbSpeed() : 0.0;
    }
}
