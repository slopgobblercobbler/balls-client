package com.ballsclient.core.module.inventory.sorting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * OreGemCompactSort — Groups raw ores, ingots, gems, and nuggets consecutively in mining grids.
 */
public class OreGemCompactSort extends Module {
    private final BooleanSetting groupBlocksWithItems = new BooleanSetting("GroupBlocksWithItems", "Place ore block variants adjacent to refined ingots", true);

    public OreGemCompactSort() {
        super("ore-gem-compact-sort", "Ore Gem Compact Sort", "Groups raw ores, ingots, gems, and nuggets consecutively in mining grids", Category.INVENTORY);
        addSettings(groupBlocksWithItems);
    }

    public BooleanSetting getGroupBlocksWithItems() {
        return groupBlocksWithItems;
    }

    public static int getMineralRank(String itemId) {
        if (itemId == null) return 99;
        String id = itemId.toLowerCase();
        if (id.contains("ancient_debris")) return 1;
        if (id.contains("netherite")) return 2;
        if (id.contains("diamond")) return 3;
        if (id.contains("emerald")) return 4;
        if (id.contains("gold")) return 5;
        if (id.contains("iron")) return 6;
        if (id.contains("copper")) return 7;
        if (id.contains("lapis")) return 8;
        if (id.contains("redstone")) return 9;
        if (id.contains("coal")) return 10;
        if (id.contains("quartz")) return 11;
        return 99;
    }
}
