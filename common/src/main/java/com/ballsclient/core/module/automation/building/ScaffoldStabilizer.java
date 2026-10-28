package com.ballsclient.core.module.automation.building;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ScaffoldStabilizer — Automatically places scaffolding towers underneath player while looking down and jumping.
 */
public class ScaffoldStabilizer extends Module {
    private final NumberSetting maxHeight = new NumberSetting("MaxHeight", "Maximum tower height limit", 16.0, 2.0, 64.0, 2.0);

    public ScaffoldStabilizer() {
        super("scaffold-stabilizer", "Scaffold Stabilizer", "Automatically places scaffolding towers underneath player while looking down and jumping", Category.AUTOMATION);
        addSettings(maxHeight);
    }

    public NumberSetting getMaxHeight() {
        return maxHeight;
    }

    public boolean canPlaceNext(int currentTowerHeight, boolean standingOnScaffold, boolean isJumping) {
        return isJumping && standingOnScaffold && currentTowerHeight < maxHeight.getValue();
    }
}
