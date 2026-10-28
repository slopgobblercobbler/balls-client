package com.ballsclient.core.module.inventory.sorting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.ModeSetting;

import java.util.Comparator;

/**
 * BlockTypeCategorizer — Groups blocks into functional classes (Building, Redstone, Functional, Minerals).
 */
public class BlockTypeCategorizer extends Module {
    private final ModeSetting primaryCategory = new ModeSetting("PrimaryCategory", "Primary priority category", "Building", "Building", "Redstone", "Tools", "Combat", "Food");

    public BlockTypeCategorizer() {
        super("block-type-categorizer", "Block Type Categorizer", "Groups blocks into functional classes (Building, Redstone, Functional, Minerals)", Category.INVENTORY);
        addSettings(primaryCategory);
    }

    public ModeSetting getPrimaryCategory() {
        return primaryCategory;
    }

    public static int getCategoryOrdinal(String itemId) {
        if (itemId == null) return 99;
        String id = itemId.toLowerCase();
        if (id.contains("sword") || id.contains("bow") || id.contains("axe") || id.contains("armor") || id.contains("shield")) {
            return 1; // Combat
        }
        if (id.contains("pickaxe") || id.contains("shovel") || id.contains("hoe") || id.contains("fishing_rod")) {
            return 2; // Tools
        }
        if (id.contains("redstone") || id.contains("repeater") || id.contains("comparator") || id.contains("piston") || id.contains("observer")) {
            return 3; // Redstone
        }
        if (id.contains("apple") || id.contains("bread") || id.contains("beef") || id.contains("porkchop") || id.contains("golden_carrot") || id.contains("potion")) {
            return 4; // Food
        }
        if (id.contains("stone") || id.contains("planks") || id.contains("dirt") || id.contains("glass") || id.contains("concrete") || id.contains("brick")) {
            return 0; // Building
        }
        return 5; // Misc / Other
    }

    public static Comparator<ItemSlot> createCategoryComparator() {
        return (a, b) -> {
            if (a == null && b == null) return 0;
            if (a == null || a.isEmpty()) return 1;
            if (b == null || b.isEmpty()) return -1;
            int ordA = getCategoryOrdinal(a.itemId());
            int ordB = getCategoryOrdinal(b.itemId());
            return Integer.compare(ordA, ordB);
        };
    }
}
