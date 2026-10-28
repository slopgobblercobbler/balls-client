package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * AutoRestockBuildingBlocks — Replenishes construction block stacks in hotbar to 64 from main inventory reserves.
 */
public class AutoRestockBuildingBlocks extends Module {
    private final NumberSetting restockThreshold = new NumberSetting("RestockThreshold", "Stack count threshold triggering restock", 8.0, 1.0, 32.0, 1.0);

    public AutoRestockBuildingBlocks() {
        super("auto-restock-building-blocks", "Restock Building Blocks", "Replenishes construction block stacks in hotbar from reserves", Category.INVENTORY);
        addSettings(restockThreshold);
    }

    public NumberSetting getRestockThreshold() {
        return restockThreshold;
    }

    public static boolean needsRestock(int currentCount, int threshold) {
        return currentCount <= threshold;
    }
}
