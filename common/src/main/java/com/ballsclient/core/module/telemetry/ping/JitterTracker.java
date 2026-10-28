package com.ballsclient.core.module.telemetry.ping;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * JitterTracker — Calculates rolling network jitter according to RFC 3550 standard statistical variance
 * between successive latency samples.
 */
public class JitterTracker extends Module {
    private final ModeSetting calculationMethod = new ModeSetting("CalculationMethod", "Statistical method for jitter calculation", "RFC3550", "RFC3550", "MEAN_ABS_DIFF", "PEAK_TO_PEAK");
    private final NumberSetting jitterAlertMs = new NumberSetting("JitterAlertMs", "Jitter warning threshold in milliseconds", 25.0, 5.0, 100.0, 5.0);
    private final BooleanSetting displayInHud = new BooleanSetting("DisplayInHud", "Displays jitter metrics on the primary client HUD", true);

    public JitterTracker() {
        super("jitter-tracker", "Jitter Tracker", "Calculates rolling network jitter according to RFC 3550 standard statistical variance between successive latency samples.", Category.NETWORK);
        addSettings(calculationMethod, jitterAlertMs, displayInHud);
    }

    public ModeSetting getCalculationMethod() {
        return calculationMethod;
    }

    public NumberSetting getJitterAlertMs() {
        return jitterAlertMs;
    }

    public BooleanSetting getDisplayInHud() {
        return displayInHud;
    }
}
