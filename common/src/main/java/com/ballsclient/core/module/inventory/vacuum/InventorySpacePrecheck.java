package com.ballsclient.core.module.inventory.vacuum;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.List;

/**
 * InventorySpacePrecheck — Evaluates whether player inventory has capacity before moving to pick up ground items.
 */
public class InventorySpacePrecheck extends Module {
    private final BooleanSetting checkStackability = new BooleanSetting("CheckStackability", "Consider existing incomplete stacks when checking capacity", true);

    public InventorySpacePrecheck() {
        super("inventory-space-precheck", "Space Precheck", "Evaluates whether player inventory has capacity", Category.INVENTORY);
        addSettings(checkStackability);
    }

    public BooleanSetting getCheckStackability() {
        return checkStackability;
    }

    public static boolean canAcceptItem(List<ItemSlot> inventory, String itemId, int count, int maxStackSize) {
        if (inventory == null || count <= 0) return false;
        int remainingNeeded = count;

        for (ItemSlot s : inventory) {
            if (s == null || s.isEmpty()) {
                return true; // Empty slot can accept
            }
            if (s.itemId() != null && s.itemId().equalsIgnoreCase(itemId)) {
                int space = Math.max(0, maxStackSize - s.count());
                remainingNeeded -= space;
                if (remainingNeeded <= 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
