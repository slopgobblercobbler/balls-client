package com.ballsclient.core.module.movement.locomotion;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Enables sprinting backwards, sideways, and in diagonal vectors.
 */
public class DirectionalSprint extends Module {
    private final BooleanSetting allowBackwards = new BooleanSetting("AllowBackwards", "Allow sprinting while walking backwards", true);
    private final BooleanSetting allowSideways = new BooleanSetting("AllowSideways", "Allow sprinting while strafing sideways", true);
    private final NumberSetting angleTolerance = new NumberSetting("AngleTolerance", "Maximum input vector angular deviation in degrees", 180.0, 45.0, 180.0, 5.0);

    public DirectionalSprint() {
        super("directional-sprint", "Directional Sprint", "Enables sprinting backwards, sideways, and in diagonal vectors", Category.MOVEMENT);
        addSettings(allowBackwards, allowSideways, angleTolerance);
    }

    public boolean isAllowBackwards() { return allowBackwards.getValue(); }
    public boolean isAllowSideways() { return allowSideways.getValue(); }
    public double getAngleTolerance() { return angleTolerance.getValue(); }

    public boolean canSprint(double forwardInput, double strafeInput) {
        if (!isEnabled()) return forwardInput > 0.0;
        if (forwardInput > 0.0) return true;
        if (forwardInput < 0.0 && isAllowBackwards()) return true;
        if (Math.abs(strafeInput) > 0.0 && isAllowSideways()) return true;
        return false;
    }
}
