package com.ballsclient.core.module.movement.aerial;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Fast camera yaw spinning during descent generates aerodynamic lift.
 */
public class HelicopterGlide extends Module {
    private final NumberSetting rotationSpeed = new NumberSetting("RotationSpeed", "Yaw rotation speed in degrees per tick", 25.0, 5.0, 60.0, 1.0);
    private final NumberSetting decelerationFactor = new NumberSetting("DecelerationFactor", "Aerodynamic lift damping applied to downward velocity", 0.85, 0.5, 0.99, 0.01);

    public HelicopterGlide() {
        super("helicopter-glide", "Helicopter Glide", "Fast camera yaw spinning during descent generates aerodynamic lift", Category.MOVEMENT);
        addSettings(rotationSpeed, decelerationFactor);
    }

    public double getRotationSpeed() { return rotationSpeed.getValue(); }
    public double getDecelerationFactor() { return decelerationFactor.getValue(); }

    public float calculateRotatedYaw(float currentYaw) {
        if (!isEnabled()) return currentYaw;
        return (float) ((currentYaw + getRotationSpeed()) % 360.0);
    }

    public double calculateLiftVelocity(double currentVy) {
        if (!isEnabled()) return currentVy;
        return currentVy < 0.0 ? currentVy * getDecelerationFactor() : currentVy;
    }
}
