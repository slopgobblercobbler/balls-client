package com.ballsclient.core.module.movement.speed;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Increases step-up height to climb full blocks without jumping.
 */
public class Step extends Module {
    private final NumberSetting height = new NumberSetting("Height", "Maximum step-up height in blocks", 1.0, 0.6, 3.0, 0.5);
    private final BooleanSetting smooth = new BooleanSetting("Smooth", "Smooth step-up camera transition easing", true);
    private final BooleanSetting reverseSafety = new BooleanSetting("ReverseSafety", "Prevent accidental reverse stepping over cliffs", true);

    public Step() {
        super("step", "Step", "Increases step-up height to climb full blocks without jumping", Category.MOVEMENT);
        addSettings(height, smooth, reverseSafety);
    }

    public double getHeight() { return height.getValue(); }
    public boolean isSmooth() { return smooth.getValue(); }
    public boolean isReverseSafety() { return reverseSafety.getValue(); }

    public double getStepHeight() {
        return isEnabled() ? getHeight() : 0.6;
    }
}
