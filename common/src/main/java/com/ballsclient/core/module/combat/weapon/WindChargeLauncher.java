package com.ballsclient.core.module.combat.weapon;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Wind Charge Launcher — Optimizes explosive burst timing with Wind Charges to juggle targets into air combos
 */
public class WindChargeLauncher extends Module {
    private final NumberSetting launchAngle = new NumberSetting("LaunchAngle", "Elevation pitch angle for self-wind boosting", 75.0, 45.0, 90.0, 1.0);
    private final BooleanSetting comboWithMace = new BooleanSetting("ComboWithMace", "Auto-swap to Mace after wind burst", true);

    public WindChargeLauncher() {
        super("wind-charge-launcher", "Wind Charge Launcher", "Optimizes explosive burst timing with Wind Charges to juggle targets into air combos", Category.COMBAT);
        addSettings(launchAngle, comboWithMace);
    }

    public double getLaunchAngle() {
        return launchAngle.getValue();
    }

    public boolean isComboWithMace() {
        return comboWithMace.getValue();
    }

    public double calculateWindBurstUpwardImpulse(double distanceToCenter, double maxRadius, double baseImpulse) {
        if (distanceToCenter >= maxRadius) return 0.0;
        return baseImpulse * (1.0 - distanceToCenter / maxRadius);
    }
}
