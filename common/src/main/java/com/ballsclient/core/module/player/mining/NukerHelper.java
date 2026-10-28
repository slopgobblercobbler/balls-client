package com.ballsclient.core.module.player.mining;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * NukerHelper — Spherical multi-block mining scheduler sorted by distance and hardness.
 */
public class NukerHelper extends Module {
    private final NumberSetting sphereRadius = new NumberSetting("SphereRadius", "Radial break sphere radius", 3.5, 1.0, 6.0, 0.5);
    private final ModeSetting targetType = new ModeSetting("TargetType", "Filter target block category", "All", "All", "OresOnly", "StoneOnly", "Flatten");
    private final NumberSetting maxBlocksPerTick = new NumberSetting("MaxBlocksPerTick", "Maximum break attempts per client tick", 2.0, 1.0, 5.0, 1.0);

    public NukerHelper() {
        super("nuker-helper", "Nuker Helper", "Spherical multi-block mining scheduler sorted by distance and hardness", Category.PLAYER);
        addSettings(sphereRadius, targetType, maxBlocksPerTick);
    }

    public NumberSetting getSphereRadius() {
        return sphereRadius;
    }

    public ModeSetting getTargetType() {
        return targetType;
    }

    public NumberSetting getMaxBlocksPerTick() {
        return maxBlocksPerTick;
    }

    public static boolean isNukerTarget(String blockId, double distance, double maxRadius, String targetType) {
        if (distance > maxRadius || blockId == null) return false;
        String idLower = blockId.toLowerCase();
        if ("All".equalsIgnoreCase(targetType)) {
            return !idLower.contains("bedrock") && !idLower.contains("portal");
        }
        if ("OresOnly".equalsIgnoreCase(targetType)) {
            return idLower.contains("ore") || idLower.contains("debris");
        }
        if ("StoneOnly".equalsIgnoreCase(targetType)) {
            return idLower.contains("stone") || idLower.contains("deepslate") || idLower.contains("diorite") || idLower.contains("granite") || idLower.contains("andesite");
        }
        if ("Flatten".equalsIgnoreCase(targetType)) {
            return true;
        }
        return false;
    }
}
