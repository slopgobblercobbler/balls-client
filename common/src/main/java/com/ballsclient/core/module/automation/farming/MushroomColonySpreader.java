package com.ballsclient.core.module.automation.farming;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * MushroomColonySpreader — Projects valid growth zones on mycelium/podzol with light level <= 12 for mushrooms.
 */
public class MushroomColonySpreader extends Module {
    private final NumberSetting maxColonySize = new NumberSetting("MaxColonySize", "Maximum neighboring mushrooms allowed in 9x9x3 region", 5.0, 1.0, 5.0, 1.0);

    public MushroomColonySpreader() {
        super("mushroom-colony-spreader", "Mushroom Colony Spreader", "Projects valid growth zones on mycelium/podzol with light level <= 12 for mushrooms", Category.AUTOMATION);
        addSettings(maxColonySize);
    }

    public NumberSetting getMaxColonySize() {
        return maxColonySize;
    }

    public boolean isValidSpreadLocation(int lightLevel, int nearbyMushroomCount) {
        return lightLevel <= 12 && nearbyMushroomCount < maxColonySize.getValue();
    }
}
