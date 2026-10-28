package com.ballsclient.core.module.movement.creative;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Enters circular orbital rotation around a focal block/entity point.
 */
public class OrbitalAnchor extends Module {
    private final NumberSetting orbitalRadius = new NumberSetting("OrbitalRadius", "Circular orbital rotation radius in blocks", 6.0, 2.0, 24.0, 1.0);
    private final NumberSetting angularSpeed = new NumberSetting("AngularSpeed", "Orbital revolution speed in degrees per tick", 4.0, 0.5, 15.0, 0.5);
    private final BooleanSetting radialStabilizer = new BooleanSetting("RadialStabilizer", "Constrain elevation and prevent orbital decay", true);

    public OrbitalAnchor() {
        super("orbital-anchor", "Orbital Anchor", "Enters circular orbital rotation around a focal block/entity point", Category.MOVEMENT);
        addSettings(orbitalRadius, angularSpeed, radialStabilizer);
    }

    public double getOrbitalRadius() { return orbitalRadius.getValue(); }
    public double getAngularSpeed() { return angularSpeed.getValue(); }
    public boolean isRadialStabilizer() { return radialStabilizer.getValue(); }

    public double[] calculateNextPosition(double anchorX, double anchorZ, double currentAngleDeg) {
        if (!isEnabled()) return new double[]{anchorX, anchorZ, currentAngleDeg};
        double newAngle = currentAngleDeg + getAngularSpeed();
        double rad = Math.toRadians(newAngle);
        double x = anchorX + getOrbitalRadius() * Math.cos(rad);
        double z = anchorZ + getOrbitalRadius() * Math.sin(rad);
        return new double[]{x, z, newAngle};
    }
}
