package com.ballsclient.core.module.movement.locomotion;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Overrides ladder and vine climb/descend speeds.
 */
public class FastLadder extends Module {
    private final NumberSetting climbSpeed = new NumberSetting("ClimbSpeed", "Vertical climb speed on ladders", 0.2872, 0.15, 1.0, 0.01);
    private final NumberSetting descendSpeed = new NumberSetting("DescendSpeed", "Vertical descend speed on ladders", 0.35, 0.15, 1.0, 0.01);
    private final BooleanSetting directional = new BooleanSetting("Directional", "Climb up when facing upwards, descend when facing downwards", true);

    public FastLadder() {
        super("fast-ladder", "Fast Ladder", "Overrides ladder and vine climb/descend speeds", Category.MOVEMENT);
        addSettings(climbSpeed, descendSpeed, directional);
    }

    public double getClimbSpeed() { return climbSpeed.getValue(); }
    public double getDescendSpeed() { return descendSpeed.getValue(); }
    public boolean isDirectional() { return directional.getValue(); }

    public double calculateClimbVelocity(boolean movingForward, float pitch, boolean movingBackward) {
        if (!isEnabled()) return movingForward ? 0.15 : (movingBackward ? -0.15 : 0.0);
        if (movingForward && (!isDirectional() || pitch < 0.0f)) return getClimbSpeed();
        if (movingBackward || (isDirectional() && pitch > 0.0f)) return -getDescendSpeed();
        return 0.0;
    }
}
