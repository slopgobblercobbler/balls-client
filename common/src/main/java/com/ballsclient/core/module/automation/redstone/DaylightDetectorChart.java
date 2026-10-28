package com.ballsclient.core.module.automation.redstone;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * DaylightDetectorChart — Graphs daylight sensor power curve across the 24000-tick day/night cycle.
 */
public class DaylightDetectorChart extends Module {
    private final BooleanSetting invertedMode = new BooleanSetting("InvertedMode", "Computes inverted moonlight sensor curve", false);

    public DaylightDetectorChart() {
        super("daylight-detector-chart", "Daylight Detector Chart", "Graphs daylight sensor power curve across the 24000-tick day/night cycle", Category.AUTOMATION);
        addSettings(invertedMode);
    }

    public BooleanSetting getInvertedMode() {
        return invertedMode;
    }

    public int getPowerLevel(long worldTimeTicks) {
        long time = worldTimeTicks % 24000L;
        // Sun is up from 0 to 12000 ticks, peaking around 6000
        double solarAngle = (time / 24000.0) * 2.0 * Math.PI;
        double irradiance = Math.sin(solarAngle);
        int dayPower = irradiance > 0.0 ? (int) Math.round(irradiance * 15.0) : 0;

        if (invertedMode.getValue()) {
            return 15 - dayPower;
        }
        return dayPower;
    }
}
