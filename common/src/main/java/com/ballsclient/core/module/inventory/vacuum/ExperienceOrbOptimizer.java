package com.ballsclient.core.module.inventory.vacuum;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ExperienceOrbOptimizer — Calculates optimal player approach pathing to maximize experience orb collection rate.
 */
public class ExperienceOrbOptimizer extends Module {
    private final NumberSetting gatherRadius = new NumberSetting("GatherRadius", "Orb gathering scan radius in blocks", 4.5, 1.0, 8.0, 0.5);

    public ExperienceOrbOptimizer() {
        super("experience-orb-optimizer", "XP Orb Optimizer", "Optimizes experience orb collection rate", Category.INVENTORY);
        addSettings(gatherRadius);
    }

    public NumberSetting getGatherRadius() {
        return gatherRadius;
    }

    public static double calculateOrbAttractionSpeed(double distance) {
        return Math.min(1.0, 0.5 / Math.max(0.1, distance));
    }
}
