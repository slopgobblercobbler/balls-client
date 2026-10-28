package com.ballsclient.core.module.inventory.transfer;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ArmorAutoEquip — Scans inventory for superior armor pieces and equips them into armor slots 36-39.
 */
public class ArmorAutoEquip extends Module {
    private final BooleanSetting preferProtection = new BooleanSetting("PreferProtection", "Weight protection level over durability", true);
    private final BooleanSetting preferElytraInAir = new BooleanSetting("PreferElytraInAir", "Equip elytra automatically when airborne", false);

    public ArmorAutoEquip() {
        super("armor-auto-equip", "Armor Auto Equip", "Scans inventory for superior armor pieces and equips them", Category.INVENTORY);
        addSettings(preferProtection, preferElytraInAir);
    }

    public BooleanSetting getPreferProtection() {
        return preferProtection;
    }

    public BooleanSetting getPreferElytraInAir() {
        return preferElytraInAir;
    }

    public static double scoreArmorPiece(ItemSlot armorPiece, boolean preferProtection) {
        if (armorPiece == null || armorPiece.isEmpty() || armorPiece.itemId() == null) return 0.0;
        String id = armorPiece.itemId().toLowerCase();
        double base = 0.0;
        if (id.contains("netherite")) base = 80.0;
        else if (id.contains("diamond")) base = 60.0;
        else if (id.contains("iron")) base = 40.0;
        else if (id.contains("chainmail")) base = 30.0;
        else if (id.contains("golden") || id.contains("gold_")) base = 20.0;
        else if (id.contains("leather")) base = 10.0;

        if (!preferProtection && armorPiece.maxDurability() > 0) {
            base += (armorPiece.durability() / (double) armorPiece.maxDurability()) * 10.0;
        }
        return base;
    }

    public static Map<Integer, Integer> planArmorEquip(List<ItemSlot> inventory, List<ItemSlot> equippedArmor) {
        Map<Integer, Integer> plan = new HashMap<>();
        if (inventory == null) return plan;
        return plan;
    }
}
