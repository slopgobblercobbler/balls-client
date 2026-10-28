package com.ballsclient.core.module.inventory.sorting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.List;

/**
 * ChestBalanceSort — Distributes items uniformly across chest columns or double chest halves.
 */
public class ChestBalanceSort extends Module {
    private final BooleanSetting balanceColumns = new BooleanSetting("BalanceColumns", "Distribute items evenly across container columns", true);
    private final BooleanSetting doubleChestSplit = new BooleanSetting("DoubleChestSplit", "Balance top chest (0-26) with bottom chest (27-53)", true);

    public ChestBalanceSort() {
        super("chest-balance-sort", "Chest Balance Sort", "Distributes items uniformly across chest columns or double chest halves", Category.INVENTORY);
        addSettings(balanceColumns, doubleChestSplit);
    }

    public BooleanSetting getBalanceColumns() {
        return balanceColumns;
    }

    public BooleanSetting getDoubleChestSplit() {
        return doubleChestSplit;
    }

    public static int[] calculateColumnDistribution(List<ItemSlot> slots, int columns) {
        if (columns <= 0) columns = 9;
        int[] counts = new int[columns];
        if (slots == null) return counts;
        for (int i = 0; i < slots.size(); i++) {
            ItemSlot s = slots.get(i);
            if (s != null && !s.isEmpty()) {
                counts[i % columns] += s.count();
            }
        }
        return counts;
    }

    public static double calculateImbalanceScore(int[] columnCounts) {
        if (columnCounts == null || columnCounts.length == 0) return 0.0;
        double sum = 0.0;
        for (int c : columnCounts) sum += c;
        double mean = sum / columnCounts.length;
        double varianceSum = 0.0;
        for (int c : columnCounts) {
            varianceSum += Math.pow(c - mean, 2);
        }
        return Math.sqrt(varianceSum / columnCounts.length);
    }
}
