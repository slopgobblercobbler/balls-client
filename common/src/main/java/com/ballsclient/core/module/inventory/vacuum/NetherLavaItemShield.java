package com.ballsclient.core.module.inventory.vacuum;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * NetherLavaItemShield — Identifies items drifting near lava/fire and accelerates them away from incineration.
 */
public class NetherLavaItemShield extends Module {
    private final NumberSetting safetyMarginBlocks = new NumberSetting("SafetyMarginBlocks", "Distance buffer above lava surface", 2.0, 1.0, 5.0, 0.5);

    public NetherLavaItemShield() {
        super("nether-lava-item-shield", "Nether Lava Item Shield", "Accelerates dropped items away from lava incineration", Category.INVENTORY);
        addSettings(safetyMarginBlocks);
    }

    public NumberSetting getSafetyMarginBlocks() {
        return safetyMarginBlocks;
    }

    public static boolean isHazardousProximity(double itemY, double lavaSurfaceY, double margin) {
        return itemY <= (lavaSurfaceY + margin);
    }
}
