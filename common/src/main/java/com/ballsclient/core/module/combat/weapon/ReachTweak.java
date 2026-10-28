package com.ballsclient.core.module.combat.weapon;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Reach Tweak — Configurable reach bounds validation for melee interaction (3.0 - 6.0 blocks)
 */
public class ReachTweak extends Module {
    private final NumberSetting maxMeleeReach = new NumberSetting("MaxMeleeReach", "Configured melee reach threshold", 4.2, 3.0, 6.0, 0.1);
    private final BooleanSetting creativeReachCheck = new BooleanSetting("CreativeReachCheck", "Allow extended reach in creative mode", true);

    public ReachTweak() {
        super("reach-tweak", "Reach Tweak", "Configurable reach bounds validation for melee interaction (3.0 - 6.0 blocks)", Category.COMBAT);
        addSettings(maxMeleeReach, creativeReachCheck);
    }

    public double getMaxMeleeReach() {
        return maxMeleeReach.getValue();
    }

    public boolean isCreativeReachCheck() {
        return creativeReachCheck.getValue();
    }

    public boolean isWithinReach(double distance, double configuredReach, boolean isCreative) {
        double maxAllowed = isCreative ? 5.0 : configuredReach;
        return distance <= maxAllowed;
    }
}
