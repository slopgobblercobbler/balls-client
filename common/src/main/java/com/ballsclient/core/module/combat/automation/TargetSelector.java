package com.ballsclient.core.module.combat.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Target Selector — Advanced entity targeting priority engine
 */
public class TargetSelector extends Module {
    private final ModeSetting sortingAlgorithm = new ModeSetting("SortingAlgorithm", "Target priority sorting metric", "Distance", "Distance", "LowestHealth", "CrosshairFov", "ThreatLevel");
    private final BooleanSetting filterInvisibles = new BooleanSetting("FilterInvisibles", "Exclude invisible targets without armor", false);

    public TargetSelector() {
        super("target-selector", "Target Selector", "Advanced entity targeting priority engine", Category.COMBAT);
        addSettings(sortingAlgorithm, filterInvisibles);
    }

    public String getSortingAlgorithm() {
        return sortingAlgorithm.getValue();
    }

    public boolean isFilterInvisibles() {
        return filterInvisibles.getValue();
    }

    public int compareTargets(double distA, double hpA, double angleA, double distB, double hpB, double angleB, String mode) {
        if ("lowesthealth".equalsIgnoreCase(mode)) return Double.compare(hpA, hpB);
        if ("crosshairfov".equalsIgnoreCase(mode)) return Double.compare(angleA, angleB);
        return Double.compare(distA, distB);
    }
}
