package com.ballsclient.core.module.telemetry.diagnostics;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * OnePercentLowTracker — Computes 1% and 0.1% worst frametimes and equivalent low FPS,
 * measuring perceived gameplay stuttering and hitch severity.
 */
public class OnePercentLowTracker extends Module {
    private final BooleanSetting includePointOnePercent = new BooleanSetting("IncludePointOnePercent", "Calculates and displays 0.1% extreme low FPS", true);
    private final NumberSetting minSamplesForCalculation = new NumberSetting("MinSamplesForCalculation", "Minimum frames required before computing low metrics", 60.0, 30.0, 240.0, 10.0);
    private final BooleanSetting stutterWarning = new BooleanSetting("StutterWarning", "Alerts when 1% low drops below 30 FPS", true);

    public OnePercentLowTracker() {
        super("one-percent-low-tracker", "One Percent Low Tracker", "Computes 1% and 0.1% worst frametimes and equivalent low FPS, measuring perceived gameplay stuttering and hitch severity.", Category.NETWORK);
        addSettings(includePointOnePercent, minSamplesForCalculation, stutterWarning);
    }

    public BooleanSetting getIncludePointOnePercent() {
        return includePointOnePercent;
    }

    public NumberSetting getMinSamplesForCalculation() {
        return minSamplesForCalculation;
    }

    public BooleanSetting getStutterWarning() {
        return stutterWarning;
    }
}
