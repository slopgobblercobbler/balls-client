package com.ballsclient.core.module.movement.failsafe;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Automatically steps back or phases out when suffocating in solid blocks.
 */
public class AntiSuffocation extends Module {
    private final NumberSetting stepOutForce = new NumberSetting("StepOutForce", "Impulse velocity pushing player out of suffocating block", 0.35, 0.1, 1.0, 0.05);
    private final BooleanSetting checkHead = new BooleanSetting("CheckHead", "Monitor player eye/head coordinate for solid block collision", true);

    public AntiSuffocation() {
        super("anti-suffocation", "Anti Suffocation", "Automatically steps back or phases out when suffocating in solid blocks", Category.MOVEMENT);
        addSettings(stepOutForce, checkHead);
    }

    public double getStepOutForce() { return stepOutForce.getValue(); }
    public boolean isCheckHead() { return checkHead.getValue(); }

    public double calculateEscapeVector(boolean headInsideSolid) {
        return (isEnabled() && headInsideSolid && isCheckHead()) ? getStepOutForce() : 0.0;
    }
}
