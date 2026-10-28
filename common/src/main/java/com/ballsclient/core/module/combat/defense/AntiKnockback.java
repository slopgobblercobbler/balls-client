package com.ballsclient.core.module.combat.defense;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Anti Knockback — Modulates incoming velocity packets (horizontal/vertical reduction %)
 */
public class AntiKnockback extends Module {
    private final NumberSetting horizontalMultiplier = new NumberSetting("HorizontalMultiplier", "Horizontal knockback velocity scale", 0.0, 0.0, 1.0, 0.05);
    private final NumberSetting verticalMultiplier = new NumberSetting("VerticalMultiplier", "Vertical knockback velocity scale", 0.0, 0.0, 1.0, 0.05);

    public AntiKnockback() {
        super("anti-knockback", "Anti Knockback", "Modulates incoming velocity packets (horizontal/vertical reduction %)", Category.COMBAT);
        addSettings(horizontalMultiplier, verticalMultiplier);
    }

    public double getHorizontalMultiplier() {
        return horizontalMultiplier.getValue();
    }

    public double getVerticalMultiplier() {
        return verticalMultiplier.getValue();
    }

    public double[] applyVelocityDamping(double vx, double vy, double vz, double hFactor, double vFactor) {
        return new double[]{vx * hFactor, vy * vFactor, vz * hFactor};
    }
}
