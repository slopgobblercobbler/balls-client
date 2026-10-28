package com.ballsclient.core.module.automation.farming;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * BonemealDispenserGuide — Computes optimal redstone clock tick rates for automated bonemeal dispensers.
 */
public class BonemealDispenserGuide extends Module {
    private final NumberSetting clockSpeedTicks = new NumberSetting("ClockSpeedTicks", "Redstone clock pulse interval in ticks", 2.0, 1.0, 8.0, 1.0);

    public BonemealDispenserGuide() {
        super("bonemeal-dispenser-guide", "Bonemeal Dispenser Guide", "Computes optimal redstone clock tick rates (1-4 ticks) for automated bonemeal dispensers", Category.AUTOMATION);
        addSettings(clockSpeedTicks);
    }

    public NumberSetting getClockSpeedTicks() {
        return clockSpeedTicks;
    }

    public double calculateDispatchesPerSecond() {
        double ticks = clockSpeedTicks.getValue();
        return 20.0 / Math.max(1.0, ticks);
    }
}
