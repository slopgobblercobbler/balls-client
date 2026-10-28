package com.ballsclient.core.module.inventory.transfer;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.ModeSetting;

import java.util.List;

/**
 * OffhandQuickSwap — Hotkey utility swapping mainhand item or specific utilities (Shield/Totem) to offhand.
 */
public class OffhandQuickSwap extends Module {
    private final ModeSetting swapTarget = new ModeSetting("SwapTarget", "Target utility for offhand swap", "Totem", "Totem", "Shield", "Gapple", "ActiveSlot");

    public OffhandQuickSwap() {
        super("offhand-quick-swap", "Offhand Quick Swap", "Hotkey utility swapping utilities to offhand", Category.INVENTORY);
        addSettings(swapTarget);
    }

    public ModeSetting getSwapTarget() {
        return swapTarget;
    }

    public static int findBestOffhandCandidate(List<ItemSlot> inventory, String targetMode) {
        if (inventory == null || targetMode == null) return -1;
        String mode = targetMode.toLowerCase();

        for (int i = 0; i < inventory.size(); i++) {
            ItemSlot s = inventory.get(i);
            if (s == null || s.isEmpty() || s.itemId() == null) continue;
            String id = s.itemId().toLowerCase();

            if (mode.contains("totem") && id.contains("totem_of_undying")) {
                return i;
            }
            if (mode.contains("shield") && id.contains("shield")) {
                return i;
            }
            if (mode.contains("gapple") && id.contains("golden_apple")) {
                return i;
            }
        }
        return -1;
    }
}
