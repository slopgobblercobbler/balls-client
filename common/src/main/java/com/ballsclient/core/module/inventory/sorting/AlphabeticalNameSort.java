package com.ballsclient.core.module.inventory.sorting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.Comparator;

/**
 * AlphabeticalNameSort — Sorts items by localized display names and custom anvil item renames.
 */
public class AlphabeticalNameSort extends Module {
    private final BooleanSetting caseSensitive = new BooleanSetting("CaseSensitive", "Perform case-sensitive alphabetical comparison", false);
    private final BooleanSetting prioritizeCustomNames = new BooleanSetting("PrioritizeCustomNames", "Sort custom anvil renamed items first", true);

    public AlphabeticalNameSort() {
        super("alphabetical-name-sort", "Alphabetical Name Sort", "Sorts items by localized display names and custom anvil item renames", Category.INVENTORY);
        addSettings(caseSensitive, prioritizeCustomNames);
    }

    public BooleanSetting getCaseSensitive() {
        return caseSensitive;
    }

    public BooleanSetting getPrioritizeCustomNames() {
        return prioritizeCustomNames;
    }

    public static String getSortableName(ItemSlot item, boolean prioritizeCustom) {
        if (item == null || item.isEmpty()) return "";
        if (prioritizeCustom && item.displayName() != null && !item.displayName().isEmpty()) {
            return item.displayName();
        }
        return item.itemId() != null ? item.itemId() : "";
    }

    public static Comparator<ItemSlot> createAlphabeticalComparator(boolean caseSensitive, boolean prioritizeCustom) {
        return (a, b) -> {
            if (a == null && b == null) return 0;
            if (a == null || a.isEmpty()) return 1;
            if (b == null || b.isEmpty()) return -1;
            String nameA = getSortableName(a, prioritizeCustom);
            String nameB = getSortableName(b, prioritizeCustom);
            return caseSensitive ? nameA.compareTo(nameB) : nameA.compareToIgnoreCase(nameB);
        };
    }
}
