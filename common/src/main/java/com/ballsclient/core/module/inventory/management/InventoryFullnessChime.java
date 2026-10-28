package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.List;

/**
 * InventoryFullnessChime — Auditory notification trigger when player inventory reaches 100% capacity.
 */
public class InventoryFullnessChime extends Module {
    private final BooleanSetting chimeOnFull = new BooleanSetting("ChimeOnFull", "Play chime when inventory reaches 100% capacity", true);

    public InventoryFullnessChime() {
        super("inventory-fullness-chime", "Inventory Fullness Chime", "Auditory notification trigger when player inventory reaches 100% capacity", Category.INVENTORY);
        addSettings(chimeOnFull);
    }

    public BooleanSetting getChimeOnFull() {
        return chimeOnFull;
    }

    public static boolean isInventoryFull(List<ItemSlot> slots, int totalSlots) {
        if (slots == null || totalSlots <= 0) return false;
        int occupied = 0;
        for (ItemSlot s : slots) {
            if (s != null && !s.isEmpty()) {
                occupied++;
            }
        }
        return occupied >= totalSlots;
    }
}
