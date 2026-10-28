package com.ballsclient.core.module.movement.creative;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Automatically jumps at the exact edge of blocks for optimal parkour.
 */
public class ParkourHelper extends Module {
    private final NumberSetting edgeThreshold = new NumberSetting("EdgeThreshold", "Distance to block edge triggering jump", 0.001, 0.0001, 0.05, 0.001);
    private final BooleanSetting sprintPrerequisite = new BooleanSetting("SprintPrerequisite", "Only jump if player is sprinting", true);
    private final BooleanSetting gapPredictor = new BooleanSetting("GapPredictor", "Analyze gap width to calculate required trajectory", true);

    public ParkourHelper() {
        super("parkour-helper", "Parkour Helper", "Automatically jumps at the exact edge of blocks for optimal parkour", Category.MOVEMENT);
        addSettings(edgeThreshold, sprintPrerequisite, gapPredictor);
    }

    public double getEdgeThreshold() { return edgeThreshold.getValue(); }
    public boolean isSprintPrerequisite() { return sprintPrerequisite.getValue(); }
    public boolean isGapPredictor() { return gapPredictor.getValue(); }

    public boolean shouldJump(double distanceToEdge, boolean isSprinting) {
        if (!isEnabled()) return false;
        if (isSprintPrerequisite() && !isSprinting) return false;
        return distanceToEdge <= getEdgeThreshold();
    }
}
