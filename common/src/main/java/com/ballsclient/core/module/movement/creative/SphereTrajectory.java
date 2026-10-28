package com.ballsclient.core.module.movement.creative;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Constrains motion along computed spherical geodetic trajectory arcs.
 */
public class SphereTrajectory extends Module {
    private final NumberSetting sphereRadius = new NumberSetting("SphereRadius", "Radius of constraint sphere in blocks", 16.0, 4.0, 64.0, 2.0);
    private final NumberSetting arcSegments = new NumberSetting("ArcSegments", "Number of geodetic interpolation steps", 32.0, 8.0, 64.0, 4.0);
    private final BooleanSetting visualizeArc = new BooleanSetting("VisualizeArc", "Render 3D geodetic arc spline in world", true);

    public SphereTrajectory() {
        super("sphere-trajectory", "Sphere Trajectory", "Constrains motion along computed spherical geodetic trajectory arcs", Category.MOVEMENT);
        addSettings(sphereRadius, arcSegments, visualizeArc);
    }

    public double getSphereRadius() { return sphereRadius.getValue(); }
    public double getArcSegments() { return arcSegments.getValue(); }
    public boolean isVisualizeArc() { return visualizeArc.getValue(); }

    public double[] projectOntoSphere(double x, double y, double z, double cx, double cy, double cz) {
        if (!isEnabled()) return new double[]{x, y, z};
        double dx = x - cx;
        double dy = y - cy;
        double dz = z - cz;
        double d = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (d == 0.0) return new double[]{cx, cy + getSphereRadius(), cz};
        double scale = getSphereRadius() / d;
        return new double[]{cx + dx * scale, cy + dy * scale, cz + dz * scale};
    }
}
