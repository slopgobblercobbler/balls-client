package com.ballsclient.core.module.movement.aerial;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Multi-jump capability mid-air by resetting ground state flags.
 */
public class AirJump extends Module {
    private final NumberSetting maxJumps = new NumberSetting("MaxJumps", "Maximum consecutive mid-air jumps", 3.0, 1.0, 10.0, 1.0);
    private final NumberSetting jumpVelocity = new NumberSetting("JumpVelocity", "Vertical impulse velocity per air jump", 0.42, 0.2, 1.0, 0.02);
    private final BooleanSetting resetOnGround = new BooleanSetting("ResetOnGround", "Reset remaining jump counter when landing", true);

    private int airJumpsRemaining = 3;

    public AirJump() {
        super("air-jump", "Air Jump", "Multi-jump capability mid-air by resetting ground state flags", Category.MOVEMENT);
        addSettings(maxJumps, jumpVelocity, resetOnGround);
    }

    public double getMaxJumps() { return maxJumps.getValue(); }
    public double getJumpVelocity() { return jumpVelocity.getValue(); }
    public boolean isResetOnGround() { return resetOnGround.getValue(); }
    public int getAirJumpsRemaining() { return airJumpsRemaining; }

    public boolean canJumpMidAir() {
        return isEnabled() && airJumpsRemaining > 0;
    }

    public double onJump() {
        if (canJumpMidAir()) {
            airJumpsRemaining--;
            return getJumpVelocity();
        }
        return 0.0;
    }

    public void onGroundTouch() {
        if (isResetOnGround()) {
            airJumpsRemaining = (int) getMaxJumps();
        }
    }
}
