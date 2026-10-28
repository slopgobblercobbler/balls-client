package com.ballsclient.core.module.telemetry.packet;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * PacketDelayEstimator — Measures inter-arrival delay variance between consecutive packets
 * to calculate instantaneous network delay and transmission stalls.
 */
public class PacketDelayEstimator extends Module {
    private final NumberSetting sampleWindow = new NumberSetting("SampleWindow", "Rolling sample window size for delay estimation", 50.0, 10.0, 200.0, 10.0);
    private final NumberSetting stallThresholdMs = new NumberSetting("StallThresholdMs", "Transmission stall threshold in milliseconds", 250.0, 50.0, 1000.0, 25.0);
    private final BooleanSetting showGraph = new BooleanSetting("ShowGraph", "Renders delay variance waveform graph on HUD", false);

    public PacketDelayEstimator() {
        super("packet-delay-estimator", "Packet Delay Estimator", "Measures inter-arrival delay variance between consecutive packets to calculate instantaneous network delay and transmission stalls.", Category.NETWORK);
        addSettings(sampleWindow, stallThresholdMs, showGraph);
    }

    public NumberSetting getSampleWindow() {
        return sampleWindow;
    }

    public NumberSetting getStallThresholdMs() {
        return stallThresholdMs;
    }

    public BooleanSetting getShowGraph() {
        return showGraph;
    }
}
