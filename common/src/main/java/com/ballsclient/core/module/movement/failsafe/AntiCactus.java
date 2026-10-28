package com.ballsclient.core.module.movement.failsafe;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Expands cactus bounding box client-side to prevent prick damage.
 */
public class AntiCactus extends Module {
    private final NumberSetting expansionDistance = new NumberSetting("ExpansionDistance", "Bounding box expansion margin in blocks", 0.10, 0.01, 0.5, 0.01);

    public AntiCactus() {
        super("anti-cactus", "Anti Cactus", "Expands cactus bounding box client-side to prevent prick damage", Category.MOVEMENT);
        addSettings(expansionDistance);
    }

    public double getExpansionDistance() { return expansionDistance.getValue(); }

    public double getExpandedBoundingBoxMin(double origMin) {
        return isEnabled() ? origMin - getExpansionDistance() : origMin;
    }

    public double getExpandedBoundingBoxMax(double origMax) {
        return isEnabled() ? origMax + getExpansionDistance() : origMax;
    }
}
