package com.ballsclient.core.module.player.interaction;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * VillagerTradeOptimizer — Computes cured discount ratios and optimal emerald exchange paths.
 */
public class VillagerTradeOptimizer extends Module {
    private final NumberSetting maxEmeraldCost = new NumberSetting("MaxEmeraldCost", "Maximum emerald cost filter for target trades", 1.0, 1.0, 64.0, 1.0);
    private final BooleanSetting highlightCured = new BooleanSetting("HighlightCured", "Highlight cured zombie villagers with 1-emerald trades", true);
    private final BooleanSetting autoSelectCheapest = new BooleanSetting("AutoSelectCheapest", "Automatically select trade offer with highest discount", true);

    public VillagerTradeOptimizer() {
        super("villager-trade-optimizer", "Villager Trade Optimizer", "Computes cured discount ratios and optimal emerald exchange paths", Category.PLAYER);
        addSettings(maxEmeraldCost, highlightCured, autoSelectCheapest);
    }

    public NumberSetting getMaxEmeraldCost() {
        return maxEmeraldCost;
    }

    public BooleanSetting getHighlightCured() {
        return highlightCured;
    }

    public BooleanSetting getAutoSelectCheapest() {
        return autoSelectCheapest;
    }

    public static int calculateDiscountedPrice(int basePrice, int discount, int demand) {
        int finalPrice = basePrice - discount + demand;
        return Math.max(1, Math.min(64, finalPrice));
    }
}
