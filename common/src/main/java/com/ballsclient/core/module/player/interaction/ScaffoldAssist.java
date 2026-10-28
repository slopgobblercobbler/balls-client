package com.ballsclient.core.module.player.interaction;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ScaffoldAssist — Downward raycast anchor calculation for seamless bridging beneath player feet.
 */
public class ScaffoldAssist extends Module {
    private final ModeSetting placementMode = new ModeSetting("PlacementMode", "Bridging trajectory mode", "Normal", "Normal", "Extend", "Diagonal");
    private final BooleanSetting safeWalkFallback = new BooleanSetting("SafeWalkFallback", "Prevent slipping off block edges while bridging", true);
    private final NumberSetting expandBlocks = new NumberSetting("ExpandBlocks", "Forward block lookahead extension", 1.0, 1.0, 4.0, 1.0);

    public ScaffoldAssist() {
        super("scaffold-assist", "Scaffold Assist", "Downward raycast anchor calculation for seamless bridging beneath player feet", Category.PLAYER);
        addSettings(placementMode, safeWalkFallback, expandBlocks);
    }

    public ModeSetting getPlacementMode() {
        return placementMode;
    }

    public BooleanSetting getSafeWalkFallback() {
        return safeWalkFallback;
    }

    public NumberSetting getExpandBlocks() {
        return expandBlocks;
    }

    public static double[] getAnchorOffset(double px, double py, double pz, double vx, double vz, String mode, int expand) {
        double targetY = py - 1.0;
        double speed = Math.sqrt(vx * vx + vz * vz);
        double dirX = speed > 1e-4 ? (vx / speed) : 0.0;
        double dirZ = speed > 1e-4 ? (vz / speed) : 0.0;

        if ("Extend".equalsIgnoreCase(mode)) {
            return new double[]{px + dirX * expand, targetY, pz + dirZ * expand};
        }
        if ("Diagonal".equalsIgnoreCase(mode)) {
            double snapX = Math.signum(vx);
            double snapZ = Math.signum(vz);
            return new double[]{px + snapX, targetY, pz + snapZ};
        }
        // Normal
        return new double[]{px, targetY, pz};
    }
}
