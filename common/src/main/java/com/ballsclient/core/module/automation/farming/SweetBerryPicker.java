package com.ballsclient.core.module.automation.farming;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * SweetBerryPicker — Right-clicks mature sweet berry bushes from safe distance to gather berries without thorn damage.
 */
public class SweetBerryPicker extends Module {
    private final NumberSetting safeDistance = new NumberSetting("SafeDistance", "Minimum safe interact distance to evade thorn damage", 2.5, 1.5, 4.0, 0.5);

    public SweetBerryPicker() {
        super("sweet-berry-picker", "Sweet Berry Picker", "Right-clicks mature sweet berry bushes from safe distance to gather berries without thorn damage", Category.AUTOMATION);
        addSettings(safeDistance);
    }

    public NumberSetting getSafeDistance() {
        return safeDistance;
    }

    public boolean canPickSafely(double distance, int berryBushAge) {
        return berryBushAge >= 2 && distance >= safeDistance.getValue();
    }
}
