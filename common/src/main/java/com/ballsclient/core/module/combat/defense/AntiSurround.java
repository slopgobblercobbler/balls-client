package com.ballsclient.core.module.combat.defense;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Anti Surround — Detects hostile obsidian/bedrock trap placement and coordinates breakout mining
 */
public class AntiSurround extends Module {
    private final NumberSetting scanRadius = new NumberSetting("ScanRadius", "Trap perimeter scanning radius", 2.0, 1.0, 3.0, 0.5);
    private final BooleanSetting autoMineObstacle = new BooleanSetting("AutoMineObstacle", "Focus pickaxe mining on weakest block", true);

    public AntiSurround() {
        super("anti-surround", "Anti Surround", "Detects hostile obsidian/bedrock trap placement and coordinates breakout mining", Category.COMBAT);
        addSettings(scanRadius, autoMineObstacle);
    }

    public double getScanRadius() {
        return scanRadius.getValue();
    }

    public boolean isAutoMineObstacle() {
        return autoMineObstacle.getValue();
    }

    public boolean isPlayerTrapped(boolean northSolid, boolean southSolid, boolean eastSolid, boolean westSolid) {
        return northSolid && southSolid && eastSolid && westSolid;
    }
}
