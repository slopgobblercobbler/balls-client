package com.ballsclient.core.module.combat.defense;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Crystal Failsafe — Detects hostile End Crystals placed at foot level and places blast-blocking media
 */
public class CrystalFailsafe extends Module {
    private final NumberSetting crystalDangerDistance = new NumberSetting("CrystalDangerDistance", "Hazard proximity threshold for end crystals", 4.5, 2.0, 6.0, 0.1);
    private final BooleanSetting autoPlaceWater = new BooleanSetting("AutoPlaceWater", "Place water bucket to quench explosion damage", true);

    public CrystalFailsafe() {
        super("crystal-failsafe", "Crystal Failsafe", "Detects hostile End Crystals placed at foot level and places blast-blocking media", Category.COMBAT);
        addSettings(crystalDangerDistance, autoPlaceWater);
    }

    public double getCrystalDangerDistance() {
        return crystalDangerDistance.getValue();
    }

    public boolean isAutoPlaceWater() {
        return autoPlaceWater.getValue();
    }

    public boolean isCrystalLethal(double crystalDist, double playerHp, double effectiveArmor) {
        double approxDmg = Math.max(0.0, (1.0 - crystalDist / 12.0) * 140.0 * 0.2);
        return approxDmg >= playerHp;
    }
}
