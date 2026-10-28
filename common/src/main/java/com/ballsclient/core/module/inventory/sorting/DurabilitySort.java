package com.ballsclient.core.module.inventory.sorting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;

import java.util.Comparator;

/**
 * DurabilitySort — Sorts tools, weapons, and armor by current durability percentage (Ascending/Descending).
 */
public class DurabilitySort extends Module {
    private final ModeSetting sortMode = new ModeSetting("SortMode", "Durability sort strategy", "DamagePercentageDesc", "DamagePercentageDesc", "DamagePercentageAsc", "AbsoluteDurability");
    private final BooleanSetting ignoreUnbreakable = new BooleanSetting("IgnoreUnbreakable", "Ignore unbreakable or zero-damage items", true);

    public DurabilitySort() {
        super("durability-sort", "Durability Sort", "Sorts tools, weapons, and armor by current durability percentage (Ascending/Descending)", Category.INVENTORY);
        addSettings(sortMode, ignoreUnbreakable);
    }

    public ModeSetting getSortMode() {
        return sortMode;
    }

    public BooleanSetting getIgnoreUnbreakable() {
        return ignoreUnbreakable;
    }

    public static double calculateDamageRatio(int durability, int maxDurability) {
        if (maxDurability <= 0) return 0.0;
        int remaining = Math.max(0, Math.min(durability, maxDurability));
        return (maxDurability - remaining) / (double) maxDurability;
    }

    public static Comparator<ItemSlot> createDurabilityComparator(boolean desc) {
        return (a, b) -> {
            if (a == null && b == null) return 0;
            if (a == null || a.isEmpty()) return 1;
            if (b == null || b.isEmpty()) return -1;
            double rA = calculateDamageRatio(a.durability(), a.maxDurability());
            double rB = calculateDamageRatio(b.durability(), b.maxDurability());
            int cmp = Double.compare(rA, rB);
            return desc ? -cmp : cmp;
        };
    }
}
