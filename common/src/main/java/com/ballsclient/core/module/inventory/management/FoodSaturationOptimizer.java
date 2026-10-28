package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.List;

/**
 * FoodSaturationOptimizer — Evaluates hunger and saturation deficits to pick food minimizing wasted saturation.
 */
public class FoodSaturationOptimizer extends Module {
    private final BooleanSetting preventOversaturation = new BooleanSetting("PreventOversaturation", "Avoid eating high-saturation foods when hunger deficit is low", true);

    public FoodSaturationOptimizer() {
        super("food-saturation-optimizer", "Food Saturation Optimizer", "Evaluates hunger and saturation deficits to pick optimal food", Category.INVENTORY);
        addSettings(preventOversaturation);
    }

    public BooleanSetting getPreventOversaturation() {
        return preventOversaturation;
    }

    public static int findOptimalFoodSlot(List<ItemSlot> inventory, int hungerDeficit, float saturationDeficit) {
        if (inventory == null || hungerDeficit <= 0) return -1;
        int bestSlot = -1;
        double bestDiff = Double.MAX_VALUE;

        for (int i = 0; i < inventory.size(); i++) {
            ItemSlot s = inventory.get(i);
            if (s != null && !s.isEmpty() && s.itemId() != null) {
                String id = s.itemId().toLowerCase();
                int restored = 0;
                if (id.contains("golden_carrot") || id.contains("cooked_beef") || id.contains("cooked_porkchop")) restored = 8;
                else if (id.contains("bread") || id.contains("baked_potato")) restored = 5;
                else if (id.contains("apple")) restored = 4;
                else if (id.contains("cookie") || id.contains("sweet_berries")) restored = 2;

                if (restored > 0) {
                    double diff = Math.abs(restored - hungerDeficit);
                    if (diff < bestDiff) {
                        bestDiff = diff;
                        bestSlot = i;
                    }
                }
            }
        }
        return bestSlot;
    }
}
