package com.ballsclient.core.module.movement.creative;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Rubber ball restitution physics reflecting off solid surfaces.
 */
public class BouncyPhysics extends Module {
    private final NumberSetting restitution = new NumberSetting("Restitution", "Kinetic energy retention coefficient upon impact", 0.85, 0.10, 1.0, 0.05);
    private final BooleanSetting wallBounce = new BooleanSetting("WallBounce", "Reflect off vertical walls", true);
    private final BooleanSetting floorBounce = new BooleanSetting("FloorBounce", "Reflect off horizontal floors and ceilings", true);
    private final NumberSetting elasticity = new NumberSetting("Elasticity", "Surface elasticity multiplier", 1.1, 0.5, 2.0, 0.1);

    public BouncyPhysics() {
        super("bouncy-physics", "Bouncy Physics", "Rubber ball restitution physics reflecting off solid surfaces", Category.MOVEMENT);
        addSettings(restitution, wallBounce, floorBounce, elasticity);
    }

    public double getRestitution() { return restitution.getValue(); }
    public boolean isWallBounce() { return wallBounce.getValue(); }
    public boolean isFloorBounce() { return floorBounce.getValue(); }
    public double getElasticity() { return elasticity.getValue(); }

    public double[] reflectVelocity(double vx, double vy, double vz, double nx, double ny, double nz) {
        if (!isEnabled()) return new double[]{vx, vy, vz};
        double dot = vx * nx + vy * ny + vz * nz;
        double e = getRestitution() * getElasticity();
        double rx = vx - (1.0 + e) * dot * nx;
        double ry = vy - (1.0 + e) * dot * ny;
        double rz = vz - (1.0 + e) * dot * nz;
        return new double[]{rx, ry, rz};
    }
}
