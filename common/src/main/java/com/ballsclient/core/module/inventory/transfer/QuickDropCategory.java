package com.ballsclient.core.module.inventory.transfer;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.ModeSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * QuickDropCategory — Rapidly drops all inventory items belonging to a selected category (e.g. junk mob drops).
 */
public class QuickDropCategory extends Module {
    private final ModeSetting targetCategory = new ModeSetting("TargetCategory", "Item category target for quick dropping", "MobDrops", "MobDrops", "Blocks", "Seeds", "Tools");

    public QuickDropCategory() {
        super("quick-drop-category", "Quick Drop Category", "Rapidly drops all inventory items belonging to a selected category", Category.INVENTORY);
        addSettings(targetCategory);
    }

    public ModeSetting getTargetCategory() {
        return targetCategory;
    }

    public static List<Integer> findSlotsToDrop(List<ItemSlot> inventory, String category) {
        List<Integer> slots = new ArrayList<>();
        if (inventory == null || category == null) return slots;
        String cat = category.toLowerCase();

        for (int i = 0; i < inventory.size(); i++) {
            ItemSlot s = inventory.get(i);
            if (s == null || s.isEmpty() || s.itemId() == null) continue;
            String id = s.itemId().toLowerCase();

            boolean match = false;
            if (cat.contains("mobdrops")) {
                match = id.contains("rotten_flesh") || id.contains("bone") || id.contains("string") || id.contains("spider_eye") || id.contains("gunpowder");
            } else if (cat.contains("blocks")) {
                match = id.contains("cobblestone") || id.contains("dirt") || id.contains("stone") || id.contains("gravel");
            } else if (cat.contains("seeds")) {
                match = id.contains("seed") || id.contains("sapling");
            } else if (cat.contains("tools")) {
                match = id.contains("sword") || id.contains("pickaxe") || id.contains("axe") || id.contains("shovel") || id.contains("hoe");
            }

            if (match) {
                slots.add(i);
            }
        }
        return slots;
    }
}
