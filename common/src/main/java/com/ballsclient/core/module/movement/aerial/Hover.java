package com.ballsclient.core.module.movement.aerial;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Cancels vertical gravity and locks player elevation mid-air.
 */
public class Hover extends Module {
    private final BooleanSetting driftCompensation = new BooleanSetting("DriftCompensation", "Actively cancel lateral air drift while hovering", true);
    private final NumberSetting verticalStep = new NumberSetting("VerticalStep", "Vertical adjustment increment per key press", 0.20, 0.05, 1.0, 0.05);

    public Hover() {
        super("hover", "Hover", "Cancels vertical gravity and locks player elevation mid-air", Category.MOVEMENT);
        addSettings(driftCompensation, verticalStep);
    }

    public boolean isDriftCompensation() { return driftCompensation.getValue(); }
    public double getVerticalStep() { return verticalStep.getValue(); }

    public double calculateHoverVelocity(boolean up, boolean down) {
        if (!isEnabled()) return 0.0;
        if (up) return getVerticalStep();
        if (down) return -getVerticalStep();
        return 0.0;
    }
}
