package com.ballsclient.core.module.combat.aim;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Bow Aim Assist — Smoothly guides crosshair toward lead-intercept coordinate based on bow draw tension and target 3D velocity
 */
public class BowAimAssist extends Module {
    private final NumberSetting aimSpeed = new NumberSetting("AimSpeed", "Interpolation aim speed", 8.5, 1.0, 20.0, 0.5);
    private final NumberSetting maxFov = new NumberSetting("MaxFov", "Maximum FOV angle to engage assist", 45.0, 5.0, 180.0, 1.0);
    private final BooleanSetting leadTarget = new BooleanSetting("LeadTarget", "Account for target velocity vector", true);
    private final BooleanSetting prioritizeHead = new BooleanSetting("PrioritizeHead", "Target head level instead of center", false);

    public BowAimAssist() {
        super("bow-aim-assist", "Bow Aim Assist", "Smoothly guides crosshair toward lead-intercept coordinate based on bow draw tension and target 3D velocity", Category.COMBAT);
        addSettings(aimSpeed, maxFov, leadTarget, prioritizeHead);
    }

    public double getAimSpeed() {
        return aimSpeed.getValue();
    }

    public double getMaxFov() {
        return maxFov.getValue();
    }

    public boolean isLeadTarget() {
        return leadTarget.getValue();
    }

    public boolean isPrioritizeHead() {
        return prioritizeHead.getValue();
    }

    public double[] calculateLeadAngles(double eyeX, double eyeY, double eyeZ, double targetX, double targetY, double targetZ, double targetVx, double targetVy, double targetVz, double arrowSpeed, double gravity) {
        double dx = targetX - eyeX;
        double dy = targetY - eyeY;
        double dz = targetZ - eyeZ;
        double horizontalDist = Math.sqrt(dx * dx + dz * dz);
        double flightTimeTicks = horizontalDist / Math.max(0.1, arrowSpeed);
        double futureX = targetX + targetVx * flightTimeTicks;
        double futureY = targetY + targetVy * flightTimeTicks + 0.5 * gravity * flightTimeTicks * flightTimeTicks;
        double futureZ = targetZ + targetVz * flightTimeTicks;
        double fdx = futureX - eyeX;
        double fdy = futureY - eyeY;
        double fdz = futureZ - eyeZ;
        double targetYaw = Math.toDegrees(Math.atan2(-fdx, fdz));
        double fHoriz = Math.sqrt(fdx * fdx + fdz * fdz);
        double targetPitch = Math.toDegrees(-Math.atan2(fdy, fHoriz));
        return new double[]{targetYaw, targetPitch, flightTimeTicks};
    }
}
