package com.ballsclient.core.module.combat.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Auto Gapple — Automatically equips and eats Golden Apples when health drops below threshold
 */
public class AutoGapple extends Module {
    private final NumberSetting healthThreshold = new NumberSetting("HealthThreshold", "Health threshold to consume golden apple", 14.0, 6.0, 18.0, 0.5);
    private final BooleanSetting preferEnchanted = new BooleanSetting("PreferEnchanted", "Prioritize Enchanted Golden Apples over regular", true);

    public AutoGapple() {
        super("auto-gapple", "Auto Gapple", "Automatically equips and eats Golden Apples when health drops below threshold", Category.COMBAT);
        addSettings(healthThreshold, preferEnchanted);
    }

    public double getHealthThreshold() {
        return healthThreshold.getValue();
    }

    public boolean isPreferEnchanted() {
        return preferEnchanted.getValue();
    }

    public boolean shouldEatGapple(double health, double threshold, boolean isAlreadyEating, boolean hasGapple) {
        return health <= threshold && hasGapple && !isAlreadyEating;
    }
}
