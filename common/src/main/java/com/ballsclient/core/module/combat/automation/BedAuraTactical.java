package com.ballsclient.core.module.combat.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Bed Aura Tactical — Coordinates explosive bed placement and right-click detonation in Nether/End
 */
public class BedAuraTactical extends Module {
    private final NumberSetting maxSelfDamage = new NumberSetting("MaxSelfDamage", "Maximum allowed self damage from bed blast", 6.0, 2.0, 12.0, 0.5);
    private final BooleanSetting requireShieldBlock = new BooleanSetting("RequireShieldBlock", "Automatically raise shield before detonating bed", true);

    public BedAuraTactical() {
        super("bed-aura-tactical", "Bed Aura Tactical", "Coordinates explosive bed placement and right-click detonation in Nether/End", Category.COMBAT);
        addSettings(maxSelfDamage, requireShieldBlock);
    }

    public double getMaxSelfDamage() {
        return maxSelfDamage.getValue();
    }

    public boolean isRequireShieldBlock() {
        return requireShieldBlock.getValue();
    }

    public boolean canDetonateBedExplosively(boolean isNetherOrEnd, double predictedSelfDmg, double maxAllowedSelfDmg) {
        return isNetherOrEnd && predictedSelfDmg <= maxAllowedSelfDmg;
    }
}
