package com.ballsclient.core.module.movement.locomotion;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Mitigates cobweb drag and dampens movement reduction.
 */
public class CobwebWalk extends Module {
    private final NumberSetting multiplier = new NumberSetting("Multiplier", "Cobweb traversal speed multiplier", 2.5, 1.0, 8.0, 0.5);
    private final BooleanSetting verticalBoost = new BooleanSetting("VerticalBoost", "Allow normal vertical jump inside cobwebs", true);

    public CobwebWalk() {
        super("cobweb-walk", "Cobweb Walk", "Mitigates cobweb drag and dampens movement reduction", Category.MOVEMENT);
        addSettings(multiplier, verticalBoost);
    }

    public double getMultiplier() { return multiplier.getValue(); }
    public boolean isVerticalBoost() { return verticalBoost.getValue(); }

    public double modifyHorizontalVelocity(double vx) {
        if (!isEnabled()) return vx;
        return vx * getMultiplier();
    }

    public double modifyVerticalVelocity(double vy) {
        if (!isEnabled()) return vy;
        return (isVerticalBoost() && vy > 0.0) ? vy * 2.0 : vy;
    }
}
