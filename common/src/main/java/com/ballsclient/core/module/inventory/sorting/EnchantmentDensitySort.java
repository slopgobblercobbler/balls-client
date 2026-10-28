package com.ballsclient.core.module.inventory.sorting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.Map;

/**
 * EnchantmentDensitySort — Sorts gear and enchanted books by total enchantment levels and enchantment count.
 */
public class EnchantmentDensitySort extends Module {
    private final BooleanSetting weightLevels = new BooleanSetting("WeightLevels", "Weight enchantment levels rather than raw enchantment count", true);

    public EnchantmentDensitySort() {
        super("enchantment-density-sort", "Enchantment Density Sort", "Sorts gear and enchanted books by total enchantment levels and enchantment count", Category.INVENTORY);
        addSettings(weightLevels);
    }

    public BooleanSetting getWeightLevels() {
        return weightLevels;
    }

    public static int calculateDensityScore(Map<String, Integer> enchantments, boolean weightLevels) {
        if (enchantments == null || enchantments.isEmpty()) return 0;
        if (!weightLevels) return enchantments.size();
        int sum = 0;
        for (int lvl : enchantments.values()) {
            sum += Math.max(1, lvl);
        }
        return sum;
    }
}
