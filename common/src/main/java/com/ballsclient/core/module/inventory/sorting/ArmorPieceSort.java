package com.ballsclient.core.module.inventory.sorting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;

/**
 * ArmorPieceSort — Groups and sorts armor sets in anatomical order (Helmet -> Chestplate -> Leggings -> Boots).
 */
public class ArmorPieceSort extends Module {
    private final ModeSetting order = new ModeSetting("Order", "Anatomical sorting order direction", "HelmetToBoots", "HelmetToBoots", "BootsToHelmet");

    public ArmorPieceSort() {
        super("armor-piece-sort", "Armor Piece Sort", "Groups and sorts armor sets in anatomical order (Helmet -> Chestplate -> Leggings -> Boots)", Category.INVENTORY);
        addSettings(order);
    }

    public ModeSetting getOrder() {
        return order;
    }

    public static int getArmorTypeRank(String itemId) {
        if (itemId == null) return 5;
        String id = itemId.toLowerCase();
        if (id.contains("helmet") || id.contains("cap")) return 1;
        if (id.contains("chestplate") || id.contains("tunic") || id.contains("elytra")) return 2;
        if (id.contains("leggings") || id.contains("pants")) return 3;
        if (id.contains("boots")) return 4;
        return 5;
    }
}
