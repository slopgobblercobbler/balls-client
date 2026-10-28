package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ArmorDurabilitySwitcher — Auto-swaps armor pieces to backup sets when active durability drops below safety floor.
 */
public class ArmorDurabilitySwitcher extends Module {
    private final NumberSetting swapFloorPct = new NumberSetting("SwapFloorPct", "Durability percentage threshold triggering auto-swap", 10.0, 5.0, 30.0, 1.0);

    public ArmorDurabilitySwitcher() {
        super("armor-durability-switcher", "Armor Durability Switcher", "Auto-swaps armor pieces to backup sets when durability drops below safety floor", Category.INVENTORY);
        addSettings(swapFloorPct);
    }

    public NumberSetting getSwapFloorPct() {
        return swapFloorPct;
    }

    public static boolean isCriticalDurability(int durability, int maxDurability, double floorPct) {
        if (maxDurability <= 0) return false;
        double pct = (durability / (double) maxDurability) * 100.0;
        return pct <= floorPct;
    }
}
