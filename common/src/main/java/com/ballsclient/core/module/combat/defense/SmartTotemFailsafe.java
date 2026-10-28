package com.ballsclient.core.module.combat.defense;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Smart Totem Failsafe — Evaluates predicted incoming damage vs HP and equips totem before lethal blow
 */
public class SmartTotemFailsafe extends Module {
    private final NumberSetting healthThreshold = new NumberSetting("HealthThreshold", "Low health threshold triggering emergency totem", 6.0, 2.0, 12.0, 0.5);
    private final NumberSetting safetyMargin = new NumberSetting("SafetyMargin", "Safety margin buffer for predicted damage", 2.0, 0.5, 5.0, 0.1);
    private final BooleanSetting predictDamage = new BooleanSetting("PredictDamage", "Predictive incoming impact calculation", true);
    private final BooleanSetting hotbarOnly = new BooleanSetting("HotbarOnly", "Restrict totem retrieval to hotbar slots", false);

    public SmartTotemFailsafe() {
        super("smart-totem-failsafe", "Smart Totem Failsafe", "Evaluates predicted incoming damage vs HP and equips totem before lethal blow", Category.COMBAT);
        addSettings(healthThreshold, safetyMargin, predictDamage, hotbarOnly);
    }

    public double getHealthThreshold() {
        return healthThreshold.getValue();
    }

    public double getSafetyMargin() {
        return safetyMargin.getValue();
    }

    public boolean isPredictDamage() {
        return predictDamage.getValue();
    }

    public boolean isHotbarOnly() {
        return hotbarOnly.getValue();
    }

    public boolean shouldEquipTotem(double currentHealth, double absorption, double predictedNetDamage, double threshold, double safetyMargin, boolean offhandHasTotem) {
        if (offhandHasTotem) return false;
        double effectiveHp = currentHealth + absorption;
        if (currentHealth <= threshold) return true;
        if (predictedNetDamage >= (effectiveHp - safetyMargin)) return true;
        return false;
    }
}
