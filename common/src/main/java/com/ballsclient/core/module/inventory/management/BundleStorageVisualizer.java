package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.List;

/**
 * BundleStorageVisualizer — Computes bundle fullness fraction (weight / 64) and displays item breakdown.
 */
public class BundleStorageVisualizer extends Module {
    private final BooleanSetting showWeightFraction = new BooleanSetting("ShowWeightFraction", "Display numerical fraction on bundle tooltip", true);

    public BundleStorageVisualizer() {
        super("bundle-storage-visualizer", "Bundle Visualizer", "Computes bundle fullness fraction and displays breakdown", Category.INVENTORY);
        addSettings(showWeightFraction);
    }

    public BooleanSetting getShowWeightFraction() {
        return showWeightFraction;
    }

    public static double calculateBundleFillRatio(List<ItemSlot> bundleContents) {
        if (bundleContents == null || bundleContents.isEmpty()) return 0.0;
        double totalWeight = 0.0;
        for (ItemSlot s : bundleContents) {
            if (s != null && !s.isEmpty()) {
                int maxStack = s.maxStackSize() > 0 ? s.maxStackSize() : 64;
                double itemWeight = 64.0 / maxStack;
                totalWeight += s.count() * itemWeight;
            }
        }
        return Math.min(1.0, totalWeight / 64.0);
    }
}
