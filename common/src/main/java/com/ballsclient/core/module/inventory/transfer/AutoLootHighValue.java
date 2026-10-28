package com.ballsclient.core.module.inventory.transfer;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * AutoLootHighValue — Instantly pulls diamonds, netherite, enchanted apples, and debris upon opening chest.
 */
public class AutoLootHighValue extends Module {
    private final BooleanSetting lootDiamonds = new BooleanSetting("LootDiamonds", "Loot diamonds and diamond gear", true);
    private final BooleanSetting lootNetherite = new BooleanSetting("LootNetherite", "Loot netherite and ancient debris", true);
    private final BooleanSetting lootGapples = new BooleanSetting("LootGapples", "Loot enchanted and regular golden apples", true);

    public AutoLootHighValue() {
        super("auto-loot-high-value", "Auto Loot High Value", "Instantly pulls diamonds, netherite, enchanted apples, and debris upon opening chest", Category.INVENTORY);
        addSettings(lootDiamonds, lootNetherite, lootGapples);
    }

    public BooleanSetting getLootDiamonds() {
        return lootDiamonds;
    }

    public BooleanSetting getLootNetherite() {
        return lootNetherite;
    }

    public BooleanSetting getLootGapples() {
        return lootGapples;
    }

    public static boolean isHighValue(String itemId) {
        if (itemId == null) return false;
        String id = itemId.toLowerCase();
        return id.contains("diamond") || id.contains("netherite") || id.contains("ancient_debris")
                || id.contains("golden_apple") || id.contains("totem") || id.contains("elytra")
                || id.contains("shulker_box") || id.contains("nether_star");
    }
}
