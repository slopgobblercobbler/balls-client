package com.ballsclient.core.module.movement.locomotion;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Modifies friction on ice, packed ice, and blue ice for acceleration.
 */
public class IceSpeed extends Module {
    private final NumberSetting frictionFactor = new NumberSetting("FrictionFactor", "Slipperiness factor applied to ice blocks", 0.98, 0.60, 1.05, 0.01);
    private final NumberSetting turnStability = new NumberSetting("TurnStability", "Steering preservation factor while sliding", 0.85, 0.10, 1.0, 0.05);
    private final BooleanSetting blueIceBoost = new BooleanSetting("BlueIceBoost", "Apply additional acceleration on blue ice", true);

    public IceSpeed() {
        super("ice-speed", "Ice Speed", "Modifies friction on ice, packed ice, and blue ice for acceleration", Category.MOVEMENT);
        addSettings(frictionFactor, turnStability, blueIceBoost);
    }

    public double getFrictionFactor() { return frictionFactor.getValue(); }
    public double getTurnStability() { return turnStability.getValue(); }
    public boolean isBlueIceBoost() { return blueIceBoost.getValue(); }

    public double calculateSlipperiness(double baseSlipperiness, boolean isBlueIce) {
        if (!isEnabled()) return baseSlipperiness;
        double s = baseSlipperiness * getFrictionFactor();
        if (isBlueIce && isBlueIceBoost()) s *= 1.02;
        return Math.min(1.05, s);
    }
}
