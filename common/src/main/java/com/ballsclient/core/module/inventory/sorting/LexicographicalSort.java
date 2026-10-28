package com.ballsclient.core.module.inventory.sorting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.Comparator;

/**
 * LexicographicalSort — Sorts inventory slots alphabetically by raw item namespace and registry ID.
 */
public class LexicographicalSort extends Module {
    private final BooleanSetting ignoreNamespace = new BooleanSetting("IgnoreNamespace", "Sort by path rather than full namespace", false);
    private final BooleanSetting ascending = new BooleanSetting("Ascending", "Sort in ascending alphabetical order", true);

    public LexicographicalSort() {
        super("lexicographical-sort", "Lexicographical Sort", "Sorts inventory slots alphabetically by raw item namespace and registry ID", Category.INVENTORY);
        addSettings(ignoreNamespace, ascending);
    }

    public BooleanSetting getIgnoreNamespace() {
        return ignoreNamespace;
    }

    public BooleanSetting getAscending() {
        return ascending;
    }

    public static int compareItemIds(String idA, String idB, boolean ignoreNamespace) {
        if (idA == null && idB == null) return 0;
        if (idA == null) return 1;
        if (idB == null) return -1;

        String keyA = idA;
        String keyB = idB;
        if (ignoreNamespace) {
            int idxA = keyA.indexOf(':');
            if (idxA >= 0) keyA = keyA.substring(idxA + 1);
            int idxB = keyB.indexOf(':');
            if (idxB >= 0) keyB = keyB.substring(idxB + 1);
        }
        return keyA.compareToIgnoreCase(keyB);
    }

    public static Comparator<ItemSlot> createComparator(boolean ignoreNamespace, boolean ascending) {
        return (a, b) -> {
            if (a == null && b == null) return 0;
            if (a == null || a.isEmpty()) return 1;
            if (b == null || b.isEmpty()) return -1;
            int cmp = compareItemIds(a.itemId(), b.itemId(), ignoreNamespace);
            return ascending ? cmp : -cmp;
        };
    }
}
