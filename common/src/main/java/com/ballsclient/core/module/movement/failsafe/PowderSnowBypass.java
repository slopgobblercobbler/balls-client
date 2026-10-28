package com.ballsclient.core.module.movement.failsafe;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * Solidifies powder snow surfaces without requiring leather boots.
 */
public class PowderSnowBypass extends Module {
    private final BooleanSetting solidify = new BooleanSetting("Solidify", "Treat powder snow block as solid collision box", true);
    private final BooleanSetting cancelFreezingEffect = new BooleanSetting("CancelFreezingEffect", "Suppress freeze damage tick accumulator", true);

    public PowderSnowBypass() {
        super("powder-snow-bypass", "Powder Snow Bypass", "Solidifies powder snow surfaces without requiring leather boots", Category.MOVEMENT);
        addSettings(solidify, cancelFreezingEffect);
    }

    public boolean isSolidify() { return solidify.getValue(); }
    public boolean isCancelFreezingEffect() { return cancelFreezingEffect.getValue(); }

    public boolean shouldSolidifySnow() { return isEnabled() && isSolidify(); }
    public boolean shouldCancelFreeze() { return isEnabled() && isCancelFreezingEffect(); }
}
