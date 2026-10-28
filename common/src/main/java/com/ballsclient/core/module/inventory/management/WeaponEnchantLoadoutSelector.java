package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * WeaponEnchantLoadoutSelector — Selects Smite vs Sharpness vs Bane sword based on raycast target entity type.
 */
public class WeaponEnchantLoadoutSelector extends Module {
    private final BooleanSetting prioritizeSmiteOnUndead = new BooleanSetting("PrioritizeSmiteOnUndead", "Prioritize Smite weapon on undead entities", true);

    public WeaponEnchantLoadoutSelector() {
        super("weapon-enchant-loadout-selector", "Weapon Enchant Selector", "Selects optimal enchanted weapon for target entity", Category.INVENTORY);
        addSettings(prioritizeSmiteOnUndead);
    }

    public BooleanSetting getPrioritizeSmiteOnUndead() {
        return prioritizeSmiteOnUndead;
    }

    public static String selectBestEnchantmentType(String targetEntityType) {
        if (targetEntityType == null) return "sharpness";
        String type = targetEntityType.toLowerCase();
        if (type.contains("zombie") || type.contains("skeleton") || type.contains("wither") || type.contains("drowned") || type.contains("phantom") || type.contains("zombified")) {
            return "smite";
        }
        if (type.contains("spider") || type.contains("cave_spider") || type.contains("bee") || type.contains("silverfish") || type.contains("endermite")) {
            return "bane_of_arthropods";
        }
        return "sharpness";
    }
}
