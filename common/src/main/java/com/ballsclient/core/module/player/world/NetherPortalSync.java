package com.ballsclient.core.module.player.world;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * NetherPortalSync — 8:1 Overworld/Nether coordinate mapping, portal search radius & elevation clamping.
 */
public class NetherPortalSync extends Module {
    private final ModeSetting dimensionMode = new ModeSetting("DimensionMode", "Dimension conversion mode", "AutoDetect", "OverworldToNether", "NetherToOverworld", "AutoDetect");
    private final BooleanSetting clampNetherRoof = new BooleanSetting("ClampNetherRoof", "Restricts target Y in Nether to [0, 127]", true);
    private final NumberSetting maxSearchRadius = new NumberSetting("MaxSearchRadius", "Search distance in destination dimension", 128.0, 16.0, 256.0, 8.0);
    private final BooleanSetting prioritizeEuclidean = new BooleanSetting("PrioritizeEuclidean", "Select nearest 3D portal candidate among matches", true);

    public NetherPortalSync() {
        super("nether-portal-sync", "Nether Portal Sync", "8:1 Overworld/Nether coordinate mapping, portal search radius & elevation clamping", Category.PLAYER);
        addSettings(dimensionMode, clampNetherRoof, maxSearchRadius, prioritizeEuclidean);
    }

    public ModeSetting getDimensionMode() {
        return dimensionMode;
    }

    public BooleanSetting getClampNetherRoof() {
        return clampNetherRoof;
    }

    public NumberSetting getMaxSearchRadius() {
        return maxSearchRadius;
    }

    public BooleanSetting getPrioritizeEuclidean() {
        return prioritizeEuclidean;
    }

    public static double[] scaleCoordinates(double x, double y, double z, boolean toNether, boolean clampCeiling) {
        if (toNether) {
            return toNether(x, y, z, clampCeiling);
        } else {
            return toOverworld(x, y, z);
        }
    }

    public static double[] toNether(double x, double y, double z, boolean clampCeiling) {
        double netherX = x / 8.0;
        double netherZ = z / 8.0;
        double netherY = y;
        if (clampCeiling) {
            netherY = Math.max(0.0, Math.min(127.0, y));
        }
        return new double[]{netherX, netherY, netherZ};
    }

    public static double[] toOverworld(double x, double y, double z) {
        double overworldX = x * 8.0;
        double overworldZ = z * 8.0;
        double overworldY = Math.max(-64.0, Math.min(319.0, y));
        return new double[]{overworldX, overworldY, overworldZ};
    }

    public static double horizontalDistance(double x1, double z1, double x2, double z2) {
        double dx = x1 - x2;
        double dz = z1 - z2;
        return Math.sqrt(dx * dx + dz * dz);
    }

    public static double euclidean3D(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        double dz = z1 - z2;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public static boolean isWithinSearchRadius(double targetX, double targetZ, double portalX, double portalZ, double maxRadius) {
        return horizontalDistance(targetX, targetZ, portalX, portalZ) <= maxRadius;
    }

    public static int findNearestPortal(double targetX, double targetY, double targetZ, double[][] candidatePortals, double maxRadius) {
        if (candidatePortals == null || candidatePortals.length == 0) return -1;
        int bestIndex = -1;
        double minDistance3D = Double.MAX_VALUE;

        for (int i = 0; i < candidatePortals.length; i++) {
            double[] portal = candidatePortals[i];
            if (portal == null || portal.length < 3) continue;
            double dist2D = horizontalDistance(targetX, targetZ, portal[0], portal[2]);
            if (dist2D <= maxRadius) {
                double dist3D = euclidean3D(targetX, targetY, targetZ, portal[0], portal[1], portal[2]);
                if (dist3D < minDistance3D) {
                    minDistance3D = dist3D;
                    bestIndex = i;
                }
            }
        }
        return bestIndex;
    }
}
