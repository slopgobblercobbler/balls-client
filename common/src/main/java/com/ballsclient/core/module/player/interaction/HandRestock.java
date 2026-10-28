package com.ballsclient.core.module.player.interaction;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * HandRestock — Scans inventory to replace depleted hand stacks immediately upon reaching zero.
 */
public class HandRestock extends Module {
    private final BooleanSetting restockOffhand = new BooleanSetting("RestockOffhand", "Restock offhand items (totems, shields, rockets)", true);
    private final NumberSetting lowItemThreshold = new NumberSetting("LowItemThreshold", "Count threshold to trigger restock swap", 0.0, 0.0, 16.0, 1.0);
    private final BooleanSetting matchNbt = new BooleanSetting("MatchNbt", "Strict match for item NBT tags and components", false);

    public HandRestock() {
        super("hand-restock", "Hand Restock", "Scans inventory to replace depleted hand stacks immediately upon reaching zero", Category.PLAYER);
        addSettings(restockOffhand, lowItemThreshold, matchNbt);
    }

    public BooleanSetting getRestockOffhand() {
        return restockOffhand;
    }

    public NumberSetting getLowItemThreshold() {
        return lowItemThreshold;
    }

    public BooleanSetting getMatchNbt() {
        return matchNbt;
    }

    public static boolean needsRestock(int currentCount, double threshold) {
        return currentCount <= threshold;
    }
}
