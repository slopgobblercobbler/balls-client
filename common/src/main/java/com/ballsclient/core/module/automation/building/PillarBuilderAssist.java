package com.ballsclient.core.module.automation.building;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * PillarBuilderAssist — Automatically builds columns straight up or down with jump-placement pacing.
 */
public class PillarBuilderAssist extends Module {
    private final ModeSetting pillarDirection = new ModeSetting("PillarDirection", "Construction direction", "Upwards", "Upwards", "Downwards");
    private final NumberSetting targetHeight = new NumberSetting("TargetHeight", "Total vertical pillar length limit in blocks", 10.0, 2.0, 64.0, 1.0);

    private int placedCount = 0;

    public PillarBuilderAssist() {
        super("pillar-builder-assist", "Pillar Builder Assist", "Automatically builds columns straight up or down with jump-placement pacing", Category.AUTOMATION);
        addSettings(pillarDirection, targetHeight);
    }

    public ModeSetting getPillarDirection() {
        return pillarDirection;
    }

    public NumberSetting getTargetHeight() {
        return targetHeight;
    }

    public boolean canPlaceNextBlock() {
        return placedCount < targetHeight.getValue();
    }

    public void incrementPlaced() {
        placedCount++;
    }

    public void resetPlaced() {
        placedCount = 0;
    }

    public int getPlacedCount() {
        return placedCount;
    }
}
