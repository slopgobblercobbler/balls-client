package com.ballsclient.core.module.telemetry.bandwidth;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * OutboundThroughputGraph — Renders a dedicated HUD graph tracking client upload bandwidth consumption
 * and outbound packet data bursts.
 */
public class OutboundThroughputGraph extends Module {
    private final NumberSetting historySamples = new NumberSetting("HistorySamples", "Number of historical data samples tracked in graph", 60.0, 20.0, 180.0, 10.0);
    private final NumberSetting scaleCeilingKb = new NumberSetting("ScaleCeilingKb", "Fixed maximum scale ceiling in kilobytes per second", 100.0, 10.0, 1000.0, 10.0);
    private final BooleanSetting autoScaling = new BooleanSetting("AutoScaling", "Dynamically expands scale ceiling during upload spikes", true);

    public OutboundThroughputGraph() {
        super("outbound-throughput-graph", "Outbound Throughput Graph", "Renders a dedicated HUD graph tracking client upload bandwidth consumption and outbound packet data bursts.", Category.NETWORK);
        addSettings(historySamples, scaleCeilingKb, autoScaling);
    }

    public NumberSetting getHistorySamples() {
        return historySamples;
    }

    public NumberSetting getScaleCeilingKb() {
        return scaleCeilingKb;
    }

    public BooleanSetting getAutoScaling() {
        return autoScaling;
    }
}
