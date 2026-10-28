package com.ballsclient.core.module.inventory.sorting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.module.inventory.SlotSwap;
import com.ballsclient.core.setting.NumberSetting;

import java.util.*;

/**
 * CombatLoadoutSort — Arranges combat gear into standard battle hotbar columns (Sword, Bow, Pearls, Gapples).
 */
public class CombatLoadoutSort extends Module {
    private final NumberSetting swordSlot = new NumberSetting("SwordSlot", "Target hotbar slot index for primary weapon", 0.0, 0.0, 8.0, 1.0);
    private final NumberSetting bowSlot = new NumberSetting("BowSlot", "Target hotbar slot index for projectile weapon", 1.0, 0.0, 8.0, 1.0);
    private final NumberSetting gappleSlot = new NumberSetting("GappleSlot", "Target hotbar slot index for golden apples", 2.0, 0.0, 8.0, 1.0);
    private final NumberSetting pearlSlot = new NumberSetting("PearlSlot", "Target hotbar slot index for ender pearls", 3.0, 0.0, 8.0, 1.0);

    public CombatLoadoutSort() {
        super("combat-loadout-sort", "Combat Loadout Sort", "Arranges combat gear into standard battle hotbar columns", Category.INVENTORY);
        addSettings(swordSlot, bowSlot, gappleSlot, pearlSlot);
    }

    public NumberSetting getSwordSlot() {
        return swordSlot;
    }

    public NumberSetting getBowSlot() {
        return bowSlot;
    }

    public NumberSetting getGappleSlot() {
        return gappleSlot;
    }

    public NumberSetting getPearlSlot() {
        return pearlSlot;
    }

    public static Map<Integer, String> getTargetLayout(int sword, int bow, int gapple, int pearl) {
        Map<Integer, String> layout = new HashMap<>();
        layout.put(sword, "sword");
        layout.put(bow, "bow");
        layout.put(gapple, "golden_apple");
        layout.put(pearl, "ender_pearl");
        return layout;
    }

    public static List<SlotSwap> alignCombatHotbar(List<ItemSlot> inventory, Map<Integer, String> targetMap) {
        List<SlotSwap> swaps = new ArrayList<>();
        if (inventory == null || targetMap == null) return swaps;

        for (Map.Entry<Integer, String> entry : targetMap.entrySet()) {
            int targetSlot = entry.getKey();
            String itemSub = entry.getValue();

            // Check if slot already has matching item
            if (targetSlot < inventory.size()) {
                ItemSlot cur = inventory.get(targetSlot);
                if (cur != null && cur.itemId() != null && cur.itemId().contains(itemSub)) {
                    continue;
                }
            }

            // Find matching item in inventory
            for (int i = 0; i < inventory.size(); i++) {
                if (i == targetSlot) continue;
                ItemSlot candidate = inventory.get(i);
                if (candidate != null && candidate.itemId() != null && candidate.itemId().contains(itemSub)) {
                    swaps.add(new SlotSwap(i, targetSlot));
                    break;
                }
            }
        }
        return swaps;
    }
}
