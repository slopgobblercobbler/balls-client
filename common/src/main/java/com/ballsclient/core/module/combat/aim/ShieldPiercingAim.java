package com.ballsclient.core.module.combat.aim;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Shield Piercing Aim — Calculates enemy shield coverage angle and directs attacks to unblocked flank angles or exposed feet
 */
public class ShieldPiercingAim extends Module {
    private final NumberSetting bypassAngleThreshold = new NumberSetting("BypassAngleThreshold", "Shield protection cone angle", 90.0, 60.0, 120.0, 1.0);
    private final BooleanSetting aimAtFeet = new BooleanSetting("AimAtFeet", "Aim at lower bounding box if facing shield", true);

    public ShieldPiercingAim() {
        super("shield-piercing-aim", "Shield Piercing Aim", "Calculates enemy shield coverage angle and directs attacks to unblocked flank angles or exposed feet", Category.COMBAT);
        addSettings(bypassAngleThreshold, aimAtFeet);
    }

    public double getBypassAngleThreshold() {
        return bypassAngleThreshold.getValue();
    }

    public boolean isAimAtFeet() {
        return aimAtFeet.getValue();
    }

    public boolean isShieldBlocking(double targetFacingYaw, double attackerToTargetYaw, double coverageAngle) {
        double angleDiff = Math.abs(Math.IEEEremainder(targetFacingYaw - attackerToTargetYaw - 180.0, 360.0));
        return angleDiff <= (coverageAngle / 2.0);
    }
}
