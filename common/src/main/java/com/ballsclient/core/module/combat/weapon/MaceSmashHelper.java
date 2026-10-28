package com.ballsclient.core.module.combat.weapon;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Mace Smash Helper — Computes fall height kinetic accumulation for 1.21+ Mace Smash damage scaling
 */
public class MaceSmashHelper extends Module {
    private final NumberSetting minFallHeightForSmash = new NumberSetting("MinFallHeightForSmash", "Minimum fall distance to trigger Mace Smash calculation", 2.5, 1.5, 10.0, 0.5);
    private final BooleanSetting negateFallDamageVisual = new BooleanSetting("NegateFallDamageVisual", "Highlight guaranteed fall damage cancellation", true);

    public MaceSmashHelper() {
        super("mace-smash-helper", "Mace Smash Helper", "Computes fall height kinetic accumulation for 1.21+ Mace Smash damage scaling", Category.COMBAT);
        addSettings(minFallHeightForSmash, negateFallDamageVisual);
    }

    public double getMinFallHeightForSmash() {
        return minFallHeightForSmash.getValue();
    }

    public boolean isNegateFallDamageVisual() {
        return negateFallDamageVisual.getValue();
    }

    public double calculateMaceBonusDamage(double fallDistance) {
        if (fallDistance <= 0.0) return 0.0;
        double bonus = 0.0;
        double tier1 = Math.min(fallDistance, 3.0);
        bonus += tier1 * 3.0;
        if (fallDistance > 3.0) {
            double tier2 = Math.min(fallDistance - 3.0, 5.0);
            bonus += tier2 * 2.0;
        }
        if (fallDistance > 8.0) {
            bonus += (fallDistance - 8.0) * 1.0;
        }
        return bonus;
    }
}
