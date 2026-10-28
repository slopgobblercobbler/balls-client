package com.ballsclient.core.module.inventory.sorting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * ToolTierProgressionSort — Orders mining tools in material progression order (Netherite > Diamond > Iron > Stone).
 */
public class ToolTierProgressionSort extends Module {
    private final BooleanSetting highestTierFirst = new BooleanSetting("HighestTierFirst", "Place highest material tier tools first", true);

    public ToolTierProgressionSort() {
        super("tool-tier-progression-sort", "Tool Tier Sort", "Orders mining tools in material progression order (Netherite > Diamond > Iron > Stone)", Category.INVENTORY);
        addSettings(highestTierFirst);
    }

    public BooleanSetting getHighestTierFirst() {
        return highestTierFirst;
    }

    public static int getMaterialRank(String itemId) {
        if (itemId == null) return 0;
        String id = itemId.toLowerCase();
        if (id.contains("netherite")) return 6;
        if (id.contains("diamond")) return 5;
        if (id.contains("iron")) return 4;
        if (id.contains("golden") || id.contains("gold_")) return 3;
        if (id.contains("stone")) return 2;
        if (id.contains("wooden") || id.contains("wood_")) return 1;
        return 0;
    }
}
