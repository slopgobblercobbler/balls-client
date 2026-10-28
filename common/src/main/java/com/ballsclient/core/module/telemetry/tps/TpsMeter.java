package com.ballsclient.core.module.telemetry.tps;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * TpsMeter — Calculates and displays real-time server Ticks Per Second (TPS) derived
 * from world time packet updates.
 */
public class TpsMeter extends Module {
    private final NumberSetting samplingPeriodSeconds = new NumberSetting("SamplingPeriodSeconds", "Measurement sampling window in seconds", 5.0, 1.0, 20.0, 1.0);
    private final NumberSetting maxDisplayTps = new NumberSetting("MaxDisplayTps", "Upper ceiling clamp for displayed server TPS", 20.0, 20.0, 100.0, 5.0);
    private final NumberSetting showDecimalPlaces = new NumberSetting("ShowDecimalPlaces", "Decimal precision for displayed TPS value", 2.0, 0.0, 3.0, 1.0);

    public TpsMeter() {
        super("tps-meter", "TPS Meter", "Calculates and displays real-time server Ticks Per Second (TPS) derived from world time packet updates.", Category.NETWORK);
        addSettings(samplingPeriodSeconds, maxDisplayTps, showDecimalPlaces);
    }

    public NumberSetting getSamplingPeriodSeconds() {
        return samplingPeriodSeconds;
    }

    public NumberSetting getMaxDisplayTps() {
        return maxDisplayTps;
    }

    public NumberSetting getShowDecimalPlaces() {
        return showDecimalPlaces;
    }
}
