package com.ballsclient.core.module.combat.tactical;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Crystal Helper — Optimizes End Crystal placement and detonation sequence for maximal opponent damage
 */
public class CrystalHelper extends Module {
    private final NumberSetting maxSelfDamage = new NumberSetting("MaxSelfDamage", "Maximum tolerable self blast damage", 6.0, 2.0, 14.0, 0.5);
    private final NumberSetting minTargetDamage = new NumberSetting("MinTargetDamage", "Minimum inflicted target damage to validate placement", 10.0, 6.0, 20.0, 0.5);

    public CrystalHelper() {
        super("crystal-helper", "Crystal Helper", "Optimizes End Crystal placement and detonation sequence for maximal opponent damage", Category.COMBAT);
        addSettings(maxSelfDamage, minTargetDamage);
    }

    public double getMaxSelfDamage() {
        return maxSelfDamage.getValue();
    }

    public double getMinTargetDamage() {
        return minTargetDamage.getValue();
    }

    public boolean isCrystalDamageFavorable(double predictedTargetDamage, double predictedSelfDamage, double maxAllowedSelfDmg, double minRequiredTargetDmg) {
        return predictedSelfDamage <= maxAllowedSelfDmg && predictedTargetDamage >= minRequiredTargetDmg;
    }
}
