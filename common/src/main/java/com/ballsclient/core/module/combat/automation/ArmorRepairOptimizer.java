package com.ballsclient.core.module.combat.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Armor Repair Optimizer — Automatically equips damaged Mending gear when absorbing experience orbs
 */
public class ArmorRepairOptimizer extends Module {
    private final NumberSetting minDamageToRepair = new NumberSetting("MinDamageToRepair", "Minimum missing durability to prioritize mending piece", 20.0, 5.0, 100.0, 5.0);
    private final BooleanSetting equipDamagedOnly = new BooleanSetting("EquipDamagedOnly", "Only equip piece when experience orbs are in vacuum proximity", true);

    public ArmorRepairOptimizer() {
        super("armor-repair-optimizer", "Armor Repair Optimizer", "Automatically equips damaged Mending gear when absorbing experience orbs", Category.COMBAT);
        addSettings(minDamageToRepair, equipDamagedOnly);
    }

    public double getMinDamageToRepair() {
        return minDamageToRepair.getValue();
    }

    public boolean isEquipDamagedOnly() {
        return equipDamagedOnly.getValue();
    }

    public boolean shouldEquipForMending(int currentDurability, int maxDurability, int minDamage) {
        int missing = maxDurability - currentDurability;
        return missing >= minDamage;
    }
}
