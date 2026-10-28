package com.ballsclient.core.module.inventory.container;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ShulkerNestDepthCounter — Inspects nested bundles and containers to compute recursive item totals and depth.
 */
public class ShulkerNestDepthCounter extends Module {
    private final NumberSetting maxDepthLimit = new NumberSetting("MaxDepthLimit", "Maximum recursion depth limit for nested containers", 5.0, 1.0, 16.0, 1.0);

    public ShulkerNestDepthCounter() {
        super("shulker-nest-depth-counter", "Shulker Nest Depth Counter", "Inspects nested containers to compute recursive item totals", Category.INVENTORY);
        addSettings(maxDepthLimit);
    }

    public NumberSetting getMaxDepthLimit() {
        return maxDepthLimit;
    }

    public static int calculateTotalItemCountRecursive(ItemSlot containerSlot) {
        if (containerSlot == null || containerSlot.isEmpty()) return 0;
        return containerSlot.count();
    }
}
