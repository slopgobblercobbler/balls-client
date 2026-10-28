package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.ModeSetting;

import java.util.List;

/**
 * ArmorSwapHotkeyBinder — Single-key instant toggle between Elytra and Diamond/Netherite Chestplate.
 */
public class ArmorSwapHotkeyBinder extends Module {
    private final ModeSetting swapTarget = new ModeSetting("SwapTarget", "Armor swap target profile", "ElytraChestplate", "ElytraChestplate", "FullSet");

    public ArmorSwapHotkeyBinder() {
        super("armor-swap-hotkey-binder", "Armor Swap Hotkey", "Single-key instant toggle between Elytra and Chestplate", Category.INVENTORY);
        addSettings(swapTarget);
    }

    public ModeSetting getSwapTarget() {
        return swapTarget;
    }

    public static int findSwapCandidate(List<ItemSlot> inventory, String targetType) {
        if (inventory == null || targetType == null) return -1;
        String t = targetType.toLowerCase();

        for (int i = 0; i < inventory.size(); i++) {
            ItemSlot s = inventory.get(i);
            if (s != null && !s.isEmpty() && s.itemId() != null) {
                String id = s.itemId().toLowerCase();
                if (t.contains("elytra") && id.contains("elytra")) {
                    return i;
                }
                if (t.contains("chestplate") && (id.contains("chestplate") || id.contains("tunic"))) {
                    return i;
                }
            }
        }
        return -1;
    }
}
