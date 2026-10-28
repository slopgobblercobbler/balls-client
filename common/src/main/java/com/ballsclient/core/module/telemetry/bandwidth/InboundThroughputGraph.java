package com.ballsclient.core.module.telemetry.bandwidth;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * InboundThroughputGraph — Renders a real-time HUD waveform chart of incoming data transfer rate
 * over the last 60 seconds.
 */
public class InboundThroughputGraph extends Module {
    private final NumberSetting historySamples = new NumberSetting("HistorySamples", "Number of historical data samples tracked in graph", 60.0, 20.0, 180.0, 10.0);
    private final BooleanSetting autoScaling = new BooleanSetting("AutoScaling", "Automatically adjusts graph vertical axis to peak throughput", true);
    private final BooleanSetting colorGradient = new BooleanSetting("ColorGradient", "Applies dynamic bandwidth rate color gradient", true);

    public InboundThroughputGraph() {
        super("inbound-throughput-graph", "Inbound Throughput Graph", "Renders a real-time HUD waveform chart of incoming data transfer rate over the last 60 seconds.", Category.NETWORK);
        addSettings(historySamples, autoScaling, colorGradient);
    }

    public NumberSetting getHistorySamples() {
        return historySamples;
    }

    public BooleanSetting getAutoScaling() {
        return autoScaling;
    }

    public BooleanSetting getColorGradient() {
        return colorGradient;
    }
}
