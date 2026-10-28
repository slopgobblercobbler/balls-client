package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.List;

/**
 * ItemCountHUDDisplay — Renders real-time counts for key consumables (Totems, Gapples, Pearls, Arrows, Rockets).
 */
public class ItemCountHUDDisplay extends Module {
    private final BooleanSetting countTotems = new BooleanSetting("CountTotems", "Track totem inventory count", true);
    private final BooleanSetting countGapples = new BooleanSetting("CountGapples", "Track golden apple inventory count", true);
    private final BooleanSetting countPearls = new BooleanSetting("CountPearls", "Track ender pearl inventory count", true);

    public ItemCountHUDDisplay() {
        super("item-count-hud-display", "Item Count HUD", "Renders real-time counts for key consumables", Category.INVENTORY);
        addSettings(countTotems, countGapples, countPearls);
    }

    public BooleanSetting getCountTotems() {
        return countTotems;
    }

    public BooleanSetting getCountGapples() {
        return countGapples;
    }

    public BooleanSetting getCountPearls() {
        return countPearls;
    }

    public static int tallyItemCount(List<ItemSlot> slots, String itemId) {
        if (slots == null || itemId == null) return 0;
        String id = itemId.toLowerCase();
        int total = 0;
        for (ItemSlot s : slots) {
            if (s != null && !s.isEmpty() && s.itemId() != null && s.itemId().toLowerCase().contains(id)) {
                total += s.count();
            }
        }
        return total;
    }
}
