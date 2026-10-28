package com.ballsclient.core.module.player.utility;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * AutoEat — Hunger and saturation monitor with automatic optimal food item consumption.
 */
public class AutoEat extends Module {
    private final NumberSetting minHunger = new NumberSetting("MinHunger", "Hunger bar value to initiate eating", 14.0, 6.0, 19.0, 1.0);
    private final BooleanSetting preferSaturation = new BooleanSetting("PreferSaturation", "Prioritize golden carrots and high saturation food", true);
    private final BooleanSetting cancelCombat = new BooleanSetting("CancelCombat", "Pause eating during active player PvP combat", true);

    public AutoEat() {
        super("auto-eat", "Auto Eat", "Hunger and saturation monitor with automatic optimal food item consumption", Category.PLAYER);
        addSettings(minHunger, preferSaturation, cancelCombat);
    }

    public NumberSetting getMinHunger() {
        return minHunger;
    }

    public BooleanSetting getPreferSaturation() {
        return preferSaturation;
    }

    public BooleanSetting getCancelCombat() {
        return cancelCombat;
    }

    public static boolean shouldEat(int hungerLevel, double minHunger, boolean inCombat, boolean cancelCombat) {
        if (inCombat && cancelCombat) return false;
        return hungerLevel <= minHunger;
    }
}
