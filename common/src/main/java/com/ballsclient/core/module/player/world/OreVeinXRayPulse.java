package com.ballsclient.core.module.player.world;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * OreVeinXRayPulse — Acoustic pulse wave simulation revealing contiguous ore clusters.
 */
public class OreVeinXRayPulse extends Module {
    private final NumberSetting pulseRadius = new NumberSetting("PulseRadius", "Maximum wavefront radius in blocks", 16.0, 4.0, 32.0, 1.0);
    private final ModeSetting targetOre = new ModeSetting("TargetOre", "Ore classification target", "Diamonds", "All", "Diamonds", "AncientDebris", "Gold", "Iron");
    private final NumberSetting pulseSpeed = new NumberSetting("PulseSpeed", "Wavefront propagation speed blocks/sec", 2.0, 0.5, 5.0, 0.5);
    private final BooleanSetting clusterAggregation = new BooleanSetting("ClusterAggregation", "Highlight contiguous clusters", true);

    public OreVeinXRayPulse() {
        super("ore-vein-xray-pulse", "Ore Vein Pulse", "Acoustic pulse wave simulation revealing contiguous ore clusters", Category.PLAYER);
        addSettings(pulseRadius, targetOre, pulseSpeed, clusterAggregation);
    }

    public NumberSetting getPulseRadius() {
        return pulseRadius;
    }

    public ModeSetting getTargetOre() {
        return targetOre;
    }

    public NumberSetting getPulseSpeed() {
        return pulseSpeed;
    }

    public BooleanSetting getClusterAggregation() {
        return clusterAggregation;
    }

    public static boolean matchesTarget(String blockId, String targetOre) {
        if (blockId == null || targetOre == null) return false;
        String idLower = blockId.toLowerCase();
        if ("All".equalsIgnoreCase(targetOre)) {
            return idLower.contains("ore") || idLower.contains("debris");
        }
        if ("Diamonds".equalsIgnoreCase(targetOre)) {
            return idLower.contains("diamond_ore");
        }
        if ("AncientDebris".equalsIgnoreCase(targetOre)) {
            return idLower.contains("ancient_debris");
        }
        if ("Gold".equalsIgnoreCase(targetOre)) {
            return idLower.contains("gold_ore");
        }
        if ("Iron".equalsIgnoreCase(targetOre)) {
            return idLower.contains("iron_ore");
        }
        return idLower.contains(targetOre.toLowerCase());
    }

    public static double getWavefrontDistance(double originX, double originY, double originZ, double bx, double by, double bz, double elapsedSeconds, double pulseSpeed) {
        double dx = bx - originX;
        double dy = by - originY;
        double dz = bz - originZ;
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        double waveRadius = elapsedSeconds * pulseSpeed;
        return Math.abs(dist - waveRadius);
    }
}
