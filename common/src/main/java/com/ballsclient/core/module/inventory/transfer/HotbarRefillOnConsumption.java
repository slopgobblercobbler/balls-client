package com.ballsclient.core.module.inventory.transfer;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.NumberSetting;

import java.util.List;

/**
 * HotbarRefillOnConsumption — Automatically pulls replenishment stacks from inventory when hotbar slot empties.
 */
public class HotbarRefillOnConsumption extends Module {
    private final NumberSetting triggerThreshold = new NumberSetting("TriggerThreshold", "Item count threshold triggering replenishment", 0.0, 0.0, 4.0, 1.0);

    public HotbarRefillOnConsumption() {
        super("hotbar-refill-on-consumption", "Hotbar Refill", "Automatically pulls replenishment stacks from inventory", Category.INVENTORY);
        addSettings(triggerThreshold);
    }

    public NumberSetting getTriggerThreshold() {
        return triggerThreshold;
    }

    public static int findMatchingReplacementSlot(List<ItemSlot> mainInventory, String depletedItemId) {
        if (mainInventory == null || depletedItemId == null) return -1;
        // Search inventory rows 9-35
        for (int i = 9; i < mainInventory.size(); i++) {
            ItemSlot s = mainInventory.get(i);
            if (s != null && !s.isEmpty() && depletedItemId.equalsIgnoreCase(s.itemId())) {
                return i;
            }
        }
        return -1;
    }
}
