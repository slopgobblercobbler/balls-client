package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.List;

/**
 * TotemReplenishmentQueue — Manages 1-tick automated queue to replace popped totems in offhand from inventory.
 */
public class TotemReplenishmentQueue extends Module {
    private final BooleanSetting oneTickRefill = new BooleanSetting("OneTickRefill", "Execute replenishment within single tick of totem popping", true);

    public TotemReplenishmentQueue() {
        super("totem-replenishment-queue", "Totem Replenish Queue", "Manages automated queue to replace popped totems in offhand", Category.INVENTORY);
        addSettings(oneTickRefill);
    }

    public BooleanSetting getOneTickRefill() {
        return oneTickRefill;
    }

    public static int findNextTotemSlot(List<ItemSlot> inventory) {
        if (inventory == null) return -1;
        for (int i = 0; i < inventory.size(); i++) {
            ItemSlot s = inventory.get(i);
            if (s != null && !s.isEmpty() && s.itemId() != null && s.itemId().toLowerCase().contains("totem_of_undying")) {
                return i;
            }
        }
        return -1;
    }
}
