package com.ballsclient.core.module.movement.failsafe;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Periodically resets client fall accumulator via contact spoofing.
 */
public class AutoFallReset extends Module {
    private final NumberSetting intervalTicks = new NumberSetting("IntervalTicks", "Ticks between periodic fall distance resets", 20.0, 5.0, 60.0, 1.0);
    private final NumberSetting minFall = new NumberSetting("MinFall", "Minimum accumulated fall distance before reset triggers", 2.5, 1.0, 10.0, 0.5);

    public AutoFallReset() {
        super("auto-fall-reset", "Auto Fall Reset", "Periodically resets client fall accumulator via contact spoofing", Category.MOVEMENT);
        addSettings(intervalTicks, minFall);
    }

    public double getIntervalTicks() { return intervalTicks.getValue(); }
    public double getMinFall() { return minFall.getValue(); }

    public boolean shouldResetFall(int fallTicks, double fallDistance) {
        return isEnabled() && fallDistance >= getMinFall() && fallTicks > 0 && fallTicks % (int) getIntervalTicks() == 0;
    }
}
