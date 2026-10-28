package com.ballsclient.core.module.telemetry.tps;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * TpsHistoryChart — Graphs historical server TPS trends over time in an interactive HUD graph
 * with customizable horizontal time scale.
 */
public class TpsHistoryChart extends Module {
    private final NumberSetting chartWidth = new NumberSetting("ChartWidth", "HUD graph display width in pixels", 120.0, 60.0, 300.0, 10.0);
    private final NumberSetting chartHeight = new NumberSetting("ChartHeight", "HUD graph display height in pixels", 45.0, 20.0, 120.0, 5.0);
    private final NumberSetting timeWindowMinutes = new NumberSetting("TimeWindowMinutes", "Historical timespan represented across graph in minutes", 5.0, 1.0, 30.0, 1.0);

    public TpsHistoryChart() {
        super("tps-history-chart", "TPS History Chart", "Graphs historical server TPS trends over time in an interactive HUD graph with customizable horizontal time scale.", Category.NETWORK);
        addSettings(chartWidth, chartHeight, timeWindowMinutes);
    }

    public NumberSetting getChartWidth() {
        return chartWidth;
    }

    public NumberSetting getChartHeight() {
        return chartHeight;
    }

    public NumberSetting getTimeWindowMinutes() {
        return timeWindowMinutes;
    }
}
