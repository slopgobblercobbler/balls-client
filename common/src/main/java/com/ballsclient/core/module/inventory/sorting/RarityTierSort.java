package com.ballsclient.core.module.inventory.sorting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.ModeSetting;

import java.util.Comparator;

/**
 * RarityTierSort — Partitions items into Minecraft rarity tiers (Common, Uncommon, Rare, Epic).
 */
public class RarityTierSort extends Module {
    private final ModeSetting rarityOrder = new ModeSetting("RarityOrder", "Sort order for rarity tiers", "EpicFirst", "EpicFirst", "CommonFirst");
    private final ModeSetting secondarySort = new ModeSetting("SecondarySort", "Secondary tie-breaker sorting", "Name", "Name", "Quantity", "None");

    public RarityTierSort() {
        super("rarity-tier-sort", "Rarity Tier Sort", "Partitions items into Minecraft rarity tiers (Common, Uncommon, Rare, Epic)", Category.INVENTORY);
        addSettings(rarityOrder, secondarySort);
    }

    public ModeSetting getRarityOrder() {
        return rarityOrder;
    }

    public ModeSetting getSecondarySort() {
        return secondarySort;
    }

    public static int getRarityTier(ItemSlot item) {
        if (item == null || item.isEmpty()) return -1;
        String id = item.itemId().toLowerCase();
        if (id.contains("enchanted_golden_apple") || id.contains("nether_star") || id.contains("dragon_egg") || id.contains("elytra")) {
            return 3; // Epic
        }
        if (id.contains("trident") || id.contains("beacon") || id.contains("conduit") || id.contains("netherite")) {
            return 2; // Rare
        }
        if (id.contains("golden_apple") || id.contains("totem") || id.contains("diamond") || id.contains("heart_of_the_sea")) {
            return 1; // Uncommon
        }
        return 0; // Common
    }

    public static Comparator<ItemSlot> createRarityComparator(boolean epicFirst) {
        return (a, b) -> {
            if (a == null && b == null) return 0;
            if (a == null || a.isEmpty()) return 1;
            if (b == null || b.isEmpty()) return -1;
            int rA = getRarityTier(a);
            int rB = getRarityTier(b);
            int cmp = Integer.compare(rA, rB);
            return epicFirst ? -cmp : cmp;
        };
    }
}
