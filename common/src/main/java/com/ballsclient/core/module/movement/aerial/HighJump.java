package com.ballsclient.core.module.movement.aerial;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Multiplies vertical jump impulse velocity to leap high barriers.
 */
public class HighJump extends Module {
    private final NumberSetting multiplier = new NumberSetting("Multiplier", "Jump impulse scaling factor", 1.8, 1.1, 5.0, 0.1);
    private final BooleanSetting potionScale = new BooleanSetting("PotionScale", "Stack with Jump Boost potion effect multiplier", true);

    public HighJump() {
        super("high-jump", "High Jump", "Multiplies vertical jump impulse velocity to leap high barriers", Category.MOVEMENT);
        addSettings(multiplier, potionScale);
    }

    public double getMultiplier() { return multiplier.getValue(); }
    public boolean isPotionScale() { return potionScale.getValue(); }

    public double calculateJumpVelocity(double baseJump) {
        if (!isEnabled()) return baseJump;
        return baseJump * getMultiplier();
    }
}
