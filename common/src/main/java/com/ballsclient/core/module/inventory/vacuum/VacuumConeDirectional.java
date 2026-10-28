package com.ballsclient.core.module.inventory.vacuum;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * VacuumConeDirectional — Constrains vacuum suction to player FOV forward cone rather than 360 sphere.
 */
public class VacuumConeDirectional extends Module {
    private final NumberSetting fovDegrees = new NumberSetting("FovDegrees", "Field-of-view angle for vacuum suction cone", 70.0, 30.0, 180.0, 5.0);

    public VacuumConeDirectional() {
        super("vacuum-cone-directional", "Directional Vacuum Cone", "Constrains suction to player FOV cone", Category.INVENTORY);
        addSettings(fovDegrees);
    }

    public NumberSetting getFovDegrees() {
        return fovDegrees;
    }

    public static boolean isInDirectionalCone(double playerYaw, double playerPitch, double dx, double dy, double dz, double coneHalfAngleDeg) {
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (dist <= 0.0001) return true;

        // Player look direction vector
        double yawRad = Math.toRadians(playerYaw);
        double pitchRad = Math.toRadians(playerPitch);
        double lookX = -Math.sin(yawRad) * Math.cos(pitchRad);
        double lookY = -Math.sin(pitchRad);
        double lookZ = Math.cos(yawRad) * Math.cos(pitchRad);

        // Dot product with normalized direction to item
        double dot = (lookX * dx + lookY * dy + lookZ * dz) / dist;
        double angleRad = Math.acos(Math.max(-1.0, Math.min(1.0, dot)));
        return Math.toDegrees(angleRad) <= coneHalfAngleDeg;
    }
}
