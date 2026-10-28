package com.ballsclient.core.module.telemetry.diagnostics;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * FrametimeProfiler — High-resolution nano-second frametime telemetry recording
 * and computing real-time rolling average FPS and standard deviation.
 */
public class FrametimeProfiler extends Module {
    private final NumberSetting bufferSize = new NumberSetting("BufferSize", "Frametime ring buffer sample capacity", 240.0, 60.0, 600.0, 30.0);
    private final BooleanSetting showStandardDeviation = new BooleanSetting("ShowStandardDeviation", "Displays frametime jitter standard deviation on HUD", true);
    private final NumberSetting spikeThresholdMs = new NumberSetting("SpikeThresholdMs", "Frametime spike alert threshold in milliseconds", 33.3, 16.6, 100.0, 5.0);

    public FrametimeProfiler() {
        super("frametime-profiler", "Frametime Profiler", "High-resolution nano-second frametime telemetry recording and computing real-time rolling average FPS and standard deviation.", Category.NETWORK);
        addSettings(bufferSize, showStandardDeviation, spikeThresholdMs);
    }

    public NumberSetting getBufferSize() {
        return bufferSize;
    }

    public BooleanSetting getShowStandardDeviation() {
        return showStandardDeviation;
    }

    public NumberSetting getSpikeThresholdMs() {
        return spikeThresholdMs;
    }
}
