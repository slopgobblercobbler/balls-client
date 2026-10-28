package com.ballsclient.core.module.movement.failsafe;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Monitors elytra impact vectors against walls to prevent lethal crashes.
 */
public class KineticFailsafe extends Module {
    private final NumberSetting impactThresholdSpeed = new NumberSetting("ImpactThresholdSpeed", "Flight speed threshold considered dangerous", 1.4, 0.5, 3.0, 0.1);
    private final NumberSetting emergencyPitchAngle = new NumberSetting("EmergencyPitchAngle", "Upward pitch angle deployed upon collision alert", 80.0, 45.0, 90.0, 5.0);
    private final BooleanSetting autoBrake = new BooleanSetting("AutoBrake", "Deploy aerodynamic drag brake upon imminent impact", true);

    public KineticFailsafe() {
        super("kinetic-failsafe", "Kinetic Failsafe", "Monitors elytra impact vectors against walls to prevent lethal crashes", Category.MOVEMENT);
        addSettings(impactThresholdSpeed, emergencyPitchAngle, autoBrake);
    }

    public double getImpactThresholdSpeed() { return impactThresholdSpeed.getValue(); }
    public double getEmergencyPitchAngle() { return emergencyPitchAngle.getValue(); }
    public boolean isAutoBrake() { return autoBrake.getValue(); }

    public boolean isCrashImminent(double vx, double vy, double vz, double nx, double ny, double nz) {
        if (!isEnabled()) return false;
        double speed = Math.sqrt(vx * vx + vy * vy + vz * vz);
        double dot = vx * nx + vy * ny + vz * nz;
        return speed > getImpactThresholdSpeed() && dot < -0.5;
    }

    public double getEmergencyPitch() {
        return getEmergencyPitchAngle();
    }
}
