package com.ballsclient.core.module.movement.creative;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Glides on a frictionless micro-cushion 0.15 blocks above terrain.
 */
public class HoverboardDrift extends Module {
    private final NumberSetting hoverHeight = new NumberSetting("HoverHeight", "Air cushion height above solid ground in blocks", 0.15, 0.05, 0.5, 0.01);
    private final NumberSetting driftFactor = new NumberSetting("DriftFactor", "Lateral momentum retention factor while hovering", 0.98, 0.80, 0.999, 0.005);

    public HoverboardDrift() {
        super("hoverboard-drift", "Hoverboard Drift", "Glides on a frictionless micro-cushion 0.15 blocks above terrain", Category.MOVEMENT);
        addSettings(hoverHeight, driftFactor);
    }

    public double getHoverHeight() { return hoverHeight.getValue(); }
    public double getDriftFactor() { return driftFactor.getValue(); }

    public double calculateHoverY(double groundY) {
        return isEnabled() ? groundY + getHoverHeight() : groundY;
    }

    public double applyDrift(double speed) {
        return isEnabled() ? speed * getDriftFactor() : speed;
    }
}
