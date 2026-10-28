package com.ballsclient.core.module.player.utility;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * AutoToolMender — Holds damaged mending gear in hand/offhand when absorbing experience orbs.
 */
public class AutoToolMender extends Module {
    private final NumberSetting targetRatio = new NumberSetting("TargetRatio", "Repair percentage threshold to consider restored", 0.95, 0.5, 1.0, 0.05);
    private final BooleanSetting swapToOffhand = new BooleanSetting("SwapToOffhand", "Prioritize offhand slot for mending repair", true);
    private final BooleanSetting autoSwapWhenFull = new BooleanSetting("AutoSwapWhenFull", "Swap to next damaged gear item when tool reaches full durability", true);

    public AutoToolMender() {
        super("auto-tool-mender", "Auto Tool Mender", "Holds damaged mending gear in hand/offhand when absorbing experience orbs", Category.PLAYER);
        addSettings(targetRatio, swapToOffhand, autoSwapWhenFull);
    }

    public NumberSetting getTargetRatio() {
        return targetRatio;
    }

    public BooleanSetting getSwapToOffhand() {
        return swapToOffhand;
    }

    public BooleanSetting getAutoSwapWhenFull() {
        return autoSwapWhenFull;
    }

    public static boolean needsRepair(int currentDurability, int maxDurability, double targetRatio) {
        if (maxDurability <= 0) return false;
        double ratio = (double) currentDurability / maxDurability;
        return ratio < targetRatio;
    }
}
