package com.ballsclient.core.module.automation.redstone;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * HopperClockPeriodTimer — Computes exact period (T = 2 * N * 8 ticks) and duty cycle of hopper item clocks.
 */
public class HopperClockPeriodTimer extends Module {
    private final NumberSetting itemCount = new NumberSetting("ItemCount", "Number of circulating items in hopper pair", 5.0, 1.0, 320.0, 1.0);

    public HopperClockPeriodTimer() {
        super("hopper-clock-period-timer", "Hopper Clock Period Timer", "Computes exact period (T = 2 * N * 8 ticks) and duty cycle of hopper item clocks", Category.AUTOMATION);
        addSettings(itemCount);
    }

    public NumberSetting getItemCount() {
        return itemCount;
    }

    public static int calculatePeriodTicks(int items) {
        return 16 * items;
    }

    public static double calculatePeriodSeconds(int items) {
        return calculatePeriodTicks(items) / 20.0;
    }

    public double getClockPeriodSeconds() {
        return calculatePeriodSeconds((int) Math.round(itemCount.getValue()));
    }
}
