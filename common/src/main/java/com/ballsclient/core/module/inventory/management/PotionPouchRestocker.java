package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.NumberSetting;

import java.util.List;

/**
 * PotionPouchRestocker — Monitors hotbar potion reserves and restocks matching types from inventory storage.
 */
public class PotionPouchRestocker extends Module {
    private final NumberSetting targetHotbarSlot = new NumberSetting("TargetHotbarSlot", "Target hotbar slot index for potion restocking", 2.0, 0.0, 8.0, 1.0);

    public PotionPouchRestocker() {
        super("potion-pouch-restocker", "Potion Pouch Restocker", "Restocks matching potion types from inventory storage", Category.INVENTORY);
        addSettings(targetHotbarSlot);
    }

    public NumberSetting getTargetHotbarSlot() {
        return targetHotbarSlot;
    }

    public static int findMatchingPotion(List<ItemSlot> inventory, String potionType) {
        if (inventory == null || potionType == null) return -1;
        String type = potionType.toLowerCase();
        for (int i = 9; i < inventory.size(); i++) {
            ItemSlot s = inventory.get(i);
            if (s != null && !s.isEmpty() && s.itemId() != null && s.itemId().toLowerCase().contains("potion")) {
                if (s.displayName() != null && s.displayName().toLowerCase().contains(type)) {
                    return i;
                }
            }
        }
        return -1;
    }
}
