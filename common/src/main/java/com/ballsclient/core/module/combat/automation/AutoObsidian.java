package com.ballsclient.core.module.combat.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Auto Obsidian — Places obsidian blocks at target positions for defensive anchors or blast mitigation
 */
public class AutoObsidian extends Module {
    private final NumberSetting placeRange = new NumberSetting("PlaceRange", "Maximum obsidian placement interaction distance", 4.0, 2.0, 5.0, 0.1);
    private final BooleanSetting raytraceCheck = new BooleanSetting("RaytraceCheck", "Validate block line of sight", true);

    public AutoObsidian() {
        super("auto-obsidian", "Auto Obsidian", "Places obsidian blocks at target positions for defensive anchors or blast mitigation", Category.COMBAT);
        addSettings(placeRange, raytraceCheck);
    }

    public double getPlaceRange() {
        return placeRange.getValue();
    }

    public boolean isRaytraceCheck() {
        return raytraceCheck.getValue();
    }

    public boolean canPlaceObsidian(int obsidianCount, double distance, double maxRange) {
        return obsidianCount > 0 && distance <= maxRange;
    }
}
