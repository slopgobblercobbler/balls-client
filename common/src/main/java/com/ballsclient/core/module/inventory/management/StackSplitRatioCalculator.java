package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * StackSplitRatioCalculator — Computes integer split fractions (1/2, 1/3, 1/4) for balanced crafting and smelting.
 */
public class StackSplitRatioCalculator extends Module {
    private final NumberSetting divisions = new NumberSetting("Divisions", "Number of portions to divide item stack into", 2.0, 2.0, 8.0, 1.0);

    public StackSplitRatioCalculator() {
        super("stack-split-ratio-calculator", "Stack Split Ratio Calculator", "Computes integer split fractions for balanced crafting", Category.INVENTORY);
        addSettings(divisions);
    }

    public NumberSetting getDivisions() {
        return divisions;
    }

    public static int[] splitStack(int totalCount, int parts) {
        if (parts <= 0) return new int[0];
        int[] result = new int[parts];
        int base = totalCount / parts;
        int rem = totalCount % parts;
        for (int i = 0; i < parts; i++) {
            result[i] = base + (i < rem ? 1 : 0);
        }
        return result;
    }
}
