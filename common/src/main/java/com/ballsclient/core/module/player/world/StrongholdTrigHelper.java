package com.ballsclient.core.module.player.world;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * StrongholdTrigHelper — Dual-throw Eye of Ender angular triangulation for portal room coordinates.
 */
public class StrongholdTrigHelper extends Module {
    private final BooleanSetting autoRecordThrows = new BooleanSetting("AutoRecordThrows", "Automatically capture Eye of Ender throw angles", true);
    private final NumberSetting minThrowDistance = new NumberSetting("MinThrowDistance", "Minimum distance between throw baseline stations", 25.0, 10.0, 100.0, 5.0);
    private final ColorSetting rayColor = new ColorSetting("RayColor", "Projected ray color", 0xFFFF5555);

    public StrongholdTrigHelper() {
        super("stronghold-trig-helper", "Stronghold Trig Helper", "Dual-throw Eye of Ender angular triangulation for portal room coordinates", Category.PLAYER);
        addSettings(autoRecordThrows, minThrowDistance, rayColor);
    }

    public BooleanSetting getAutoRecordThrows() {
        return autoRecordThrows;
    }

    public NumberSetting getMinThrowDistance() {
        return minThrowDistance;
    }

    public ColorSetting getRayColor() {
        return rayColor;
    }

    /**
     * Solves 2D intersection of two rays defined by (x1, z1, yaw1) and (x2, z2, yaw2).
     * Minecraft yaw: 0 is south (+Z), 90 is west (-X), 180 is north (-Z), 270 is east (+X).
     * Vector: dx = -sin(yaw), dz = cos(yaw).
     */
    public static double[] triangulate(double x1, double z1, double yaw1, double x2, double z2, double yaw2) {
        double rad1 = Math.toRadians(yaw1);
        double rad2 = Math.toRadians(yaw2);
        double dx1 = -Math.sin(rad1);
        double dz1 = Math.cos(rad1);
        double dx2 = -Math.sin(rad2);
        double dz2 = Math.cos(rad2);

        double det = dx1 * dz2 - dz1 * dx2;
        if (Math.abs(det) < 1e-6) {
            // Parallel or nearly parallel rays
            return new double[]{x1, z1};
        }

        double deltaX = x2 - x1;
        double deltaZ = z2 - z1;
        double t = (deltaX * dz2 - deltaZ * dx2) / det;

        double targetX = x1 + t * dx1;
        double targetZ = z1 + t * dz1;
        return new double[]{targetX, targetZ};
    }
}
