package com.ballsclient.core.module.player.interaction;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * LiquidInteract — Allows placing blocks directly against still and flowing water/lava surfaces.
 */
public class LiquidInteract extends Module {
    private final BooleanSetting waterPlacement = new BooleanSetting("WaterPlacement", "Allow placement against water surfaces", true);
    private final BooleanSetting lavaPlacement = new BooleanSetting("LavaPlacement", "Allow placement against lava surfaces", true);
    private final BooleanSetting stillOnly = new BooleanSetting("StillOnly", "Interact only with still liquid source blocks", false);

    public LiquidInteract() {
        super("liquid-interact", "Liquid Interact", "Allows placing blocks directly against still and flowing water/lava surfaces", Category.PLAYER);
        addSettings(waterPlacement, lavaPlacement, stillOnly);
    }

    public BooleanSetting getWaterPlacement() {
        return waterPlacement;
    }

    public BooleanSetting getLavaPlacement() {
        return lavaPlacement;
    }

    public BooleanSetting getStillOnly() {
        return stillOnly;
    }

    public static boolean canInteractWithFluid(boolean isWater, boolean isLava, boolean isStill, boolean allowWater, boolean allowLava, boolean stillOnly) {
        if (stillOnly && !isStill) return false;
        if (isWater && allowWater) return true;
        if (isLava && allowLava) return true;
        return false;
    }
}
