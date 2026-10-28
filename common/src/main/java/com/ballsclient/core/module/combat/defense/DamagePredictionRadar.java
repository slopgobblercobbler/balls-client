package com.ballsclient.core.module.combat.defense;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Damage Prediction Radar — Pre-computes incoming strike damage before packet delivery based on weapon and swing
 */
public class DamagePredictionRadar extends Module {
    private final BooleanSetting showDamageFloats = new BooleanSetting("ShowDamageFloats", "Render floating predicted damage indicator", true);
    private final NumberSetting maxTrackingDistance = new NumberSetting("MaxTrackingDistance", "Maximum combatant range for damage calculation", 6.0, 3.0, 10.0, 0.5);

    public DamagePredictionRadar() {
        super("damage-prediction-radar", "Damage Prediction Radar", "Pre-computes incoming strike damage before packet delivery based on weapon and swing", Category.COMBAT);
        addSettings(showDamageFloats, maxTrackingDistance);
    }

    public boolean isShowDamageFloats() {
        return showDamageFloats.getValue();
    }

    public double getMaxTrackingDistance() {
        return maxTrackingDistance.getValue();
    }

    public double estimateMeleeDamage(double baseWeaponDamage, int sharpnessLevel, double strengthMultiplier, boolean isCrit) {
        double raw = baseWeaponDamage + (sharpnessLevel > 0 ? 0.5 * sharpnessLevel + 0.5 : 0.0);
        raw *= strengthMultiplier;
        if (isCrit) raw *= 1.5;
        return raw;
    }
}
