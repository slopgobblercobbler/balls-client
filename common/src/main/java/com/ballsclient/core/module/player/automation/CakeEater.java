package com.ballsclient.core.module.player.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * CakeEater — Consumes slices from nearby placed cake blocks when hunger drops below maximum.
 */
public class CakeEater extends Module {
    private final NumberSetting hungerThreshold = new NumberSetting("HungerThreshold", "Hunger level to trigger eating cake slice", 18.0, 1.0, 20.0, 1.0);
    private final NumberSetting maxReach = new NumberSetting("MaxReach", "Maximum block distance to cake", 4.0, 2.0, 5.0, 0.5);

    public CakeEater() {
        super("cake-eater", "Cake Eater", "Consumes slices from nearby placed cake blocks when hunger drops below maximum", Category.PLAYER);
        addSettings(hungerThreshold, maxReach);
    }

    public NumberSetting getHungerThreshold() {
        return hungerThreshold;
    }

    public NumberSetting getMaxReach() {
        return maxReach;
    }

    public static boolean shouldEatCake(int hunger, int bitesRemaining, double hungerThreshold) {
        return hunger <= hungerThreshold && bitesRemaining > 0;
    }
}
