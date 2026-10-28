package com.ballsclient.core.module.movement.locomotion;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Multiplies vertical trampoline rebound kinetic velocity.
 */
public class SlimeBounce extends Module {
    private final NumberSetting bounceFactor = new NumberSetting("BounceFactor", "Vertical bounce velocity coefficient", 1.5, 0.8, 3.0, 0.1);
    private final BooleanSetting cancelDamage = new BooleanSetting("CancelDamage", "Cancel fall damage when landing on slime blocks", true);

    public SlimeBounce() {
        super("slime-bounce", "Slime Bounce", "Multiplies vertical trampoline rebound kinetic velocity", Category.MOVEMENT);
        addSettings(bounceFactor, cancelDamage);
    }

    public double getBounceFactor() { return bounceFactor.getValue(); }
    public boolean isCancelDamage() { return cancelDamage.getValue(); }

    public double calculateBounceVelocity(double incomingVy) {
        if (!isEnabled()) return -incomingVy;
        return -incomingVy * getBounceFactor();
    }
}
