package com.ballsclient.core.module.telemetry.ping;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * PingHistoryGraph — Renders a dynamic scrolling oscilloscope chart of round-trip latency over time
 * on the client HUD.
 */
public class PingHistoryGraph extends Module {
    private final NumberSetting historyPoints = new NumberSetting("HistoryPoints", "Total historical data points rendered on graph", 60.0, 20.0, 240.0, 10.0);
    private final NumberSetting graphHeight = new NumberSetting("GraphHeight", "Graph height in on-screen UI pixels", 40.0, 20.0, 120.0, 5.0);
    private final BooleanSetting fillUnderline = new BooleanSetting("FillUnderline", "Fills area under the latency line with semi-transparent gradient", true);

    public PingHistoryGraph() {
        super("ping-history-graph", "Ping History Graph", "Renders a dynamic scrolling oscilloscope chart of round-trip latency over time on the client HUD.", Category.NETWORK);
        addSettings(historyPoints, graphHeight, fillUnderline);
    }

    public NumberSetting getHistoryPoints() {
        return historyPoints;
    }

    public NumberSetting getGraphHeight() {
        return graphHeight;
    }

    public BooleanSetting getFillUnderline() {
        return fillUnderline;
    }
}
