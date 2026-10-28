package com.ballsclient.core.module.movement.aerial;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Ascends vertical solid walls when colliding horizontally.
 */
public class WallClimb extends Module {
    private final NumberSetting climbSpeed = new NumberSetting("ClimbSpeed", "Ascent speed along vertical wall faces", 0.25, 0.1, 0.8, 0.05);
    private final BooleanSetting holdOnShift = new BooleanSetting("HoldOnShift", "Arrest vertical motion and cling to wall while sneaking", true);

    public WallClimb() {
        super("wall-climb", "Wall Climb", "Ascends vertical solid walls when colliding horizontally", Category.MOVEMENT);
        addSettings(climbSpeed, holdOnShift);
    }

    public double getClimbSpeed() { return climbSpeed.getValue(); }
    public boolean isHoldOnShift() { return holdOnShift.getValue(); }

    public double calculateClimbVelocity(boolean horizontalCollision, boolean sneaking) {
        if (!isEnabled() || !horizontalCollision) return 0.0;
        if (sneaking && isHoldOnShift()) return 0.0;
        return getClimbSpeed();
    }
}
