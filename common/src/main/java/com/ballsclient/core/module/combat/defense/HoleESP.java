package com.ballsclient.core.module.combat.defense;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Hole ESP — Identifies and highlights safe 1x1 obsidian and bedrock blast-mitigating holes
 */
public class HoleESP extends Module {
    private final ModeSetting holeType = new ModeSetting("HoleType", "Types of protective blast holes to render", "All", "All", "BedrockOnly", "ObsidianOnly");
    private final NumberSetting scanRadius = new NumberSetting("ScanRadius", "Search radius for protective holes", 8.0, 3.0, 16.0, 1.0);

    public HoleESP() {
        super("hole-esp", "Hole ESP", "Identifies and highlights safe 1x1 obsidian and bedrock blast-mitigating holes", Category.COMBAT);
        addSettings(holeType, scanRadius);
    }

    public String getHoleType() {
        return holeType.getValue();
    }

    public double getScanRadius() {
        return scanRadius.getValue();
    }

    public boolean isSafeHole(boolean northHard, boolean southHard, boolean eastHard, boolean westHard, boolean bottomHard) {
        return northHard && southHard && eastHard && westHard && bottomHard;
    }
}
