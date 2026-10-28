package com.ballsclient.core.module.combat.tactical;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Bow Spam Helper — Optimizes bowstring draw duration for short-range rapid arrow spamming
 */
public class BowSpamHelper extends Module {
    private final NumberSetting minDrawTicks = new NumberSetting("MinDrawTicks", "Minimum draw ticks before auto-release in spam mode", 4.0, 3.0, 10.0, 1.0);
    private final NumberSetting maxDistance = new NumberSetting("MaxDistance", "Maximum target distance to allow spam firing", 5.0, 2.0, 8.0, 0.5);

    public BowSpamHelper() {
        super("bow-spam-helper", "Bow Spam Helper", "Optimizes bowstring draw duration for short-range rapid arrow spamming", Category.COMBAT);
        addSettings(minDrawTicks, maxDistance);
    }

    public double getMinDrawTicks() {
        return minDrawTicks.getValue();
    }

    public double getMaxDistance() {
        return maxDistance.getValue();
    }

    public boolean shouldReleaseBowEarly(int currentDrawTicks, int targetDrawTicks, double targetDist, double maxSpamDist) {
        return targetDist <= maxSpamDist && currentDrawTicks >= targetDrawTicks;
    }
}
