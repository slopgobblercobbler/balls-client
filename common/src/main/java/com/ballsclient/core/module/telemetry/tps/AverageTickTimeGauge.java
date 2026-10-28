package com.ballsclient.core.module.telemetry.tps;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * AverageTickTimeGauge — Estimates Milliseconds Per Tick (MSPT) from incoming packet intervals,
 * indicating server headroom below the 50ms budget.
 */
public class AverageTickTimeGauge extends Module {
    private final NumberSetting targetBudgetMs = new NumberSetting("TargetBudgetMs", "Target server tick budget in milliseconds", 50.0, 20.0, 100.0, 5.0);
    private final BooleanSetting showHeadroomPercent = new BooleanSetting("ShowHeadroomPercent", "Displays remaining server capacity headroom percentage", true);
    private final NumberSetting smoothingWindow = new NumberSetting("SmoothingWindow", "Smoothing window sample count for tick duration estimation", 20.0, 5.0, 100.0, 5.0);

    public AverageTickTimeGauge() {
        super("average-tick-time-gauge", "Average Tick Time Gauge", "Estimates Milliseconds Per Tick (MSPT) from incoming packet intervals, indicating server headroom below the 50ms budget.", Category.NETWORK);
        addSettings(targetBudgetMs, showHeadroomPercent, smoothingWindow);
    }

    public NumberSetting getTargetBudgetMs() {
        return targetBudgetMs;
    }

    public BooleanSetting getShowHeadroomPercent() {
        return showHeadroomPercent;
    }

    public NumberSetting getSmoothingWindow() {
        return smoothingWindow;
    }
}
