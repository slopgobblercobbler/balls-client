package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.NumberSetting;

import java.util.*;

/**
 * DuplicateToolCleaner — Detects redundant identical tool tiers and flags lower-durability duplicates for storage.
 */
public class DuplicateToolCleaner extends Module {
    private final NumberSetting maxKeepDuplicates = new NumberSetting("MaxKeepDuplicates", "Maximum identical tools to retain in inventory", 1.0, 1.0, 5.0, 1.0);

    public DuplicateToolCleaner() {
        super("duplicate-tool-cleaner", "Duplicate Tool Cleaner", "Detects redundant identical tool tiers and flags lower-durability duplicates", Category.INVENTORY);
        addSettings(maxKeepDuplicates);
    }

    public NumberSetting getMaxKeepDuplicates() {
        return maxKeepDuplicates;
    }

    public static List<Integer> findRedundantToolSlots(List<ItemSlot> inventory, int keepMax) {
        List<Integer> redundant = new ArrayList<>();
        if (inventory == null) return redundant;

        Map<String, List<Integer>> byType = new HashMap<>();
        for (int i = 0; i < inventory.size(); i++) {
            ItemSlot s = inventory.get(i);
            if (s != null && !s.isEmpty() && s.itemId() != null) {
                String id = s.itemId().toLowerCase();
                if (id.contains("pickaxe") || id.contains("sword") || id.contains("axe") || id.contains("shovel") || id.contains("hoe")) {
                    byType.computeIfAbsent(s.itemId(), k -> new ArrayList<>()).add(i);
                }
            }
        }

        for (List<Integer> slots : byType.values()) {
            if (slots.size() > keepMax) {
                // Sort by durability ascending (lowest durability first)
                slots.sort(Comparator.comparingInt(idx -> {
                    ItemSlot slot = inventory.get(idx);
                    return slot != null ? slot.durability() : 0;
                }));
                int toFlag = slots.size() - keepMax;
                for (int i = 0; i < toFlag; i++) {
                    redundant.add(slots.get(i));
                }
            }
        }
        return redundant;
    }
}
