package com.ballsclient.core.module.telemetry.bandwidth;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * NetworkTrafficPeakDetector — Detects and logs anomalous network traffic surges,
 * identifying timestamp, peak rate, and dominant packet contributors.
 */
public class NetworkTrafficPeakDetector extends Module {
    private final NumberSetting peakThresholdKbPerSec = new NumberSetting("PeakThresholdKbPerSec", "Throughput surge threshold in kilobytes per second", 250.0, 50.0, 2000.0, 50.0);
    private final BooleanSetting logPeakDetails = new BooleanSetting("LogPeakDetails", "Outputs top contributing packet types to console during peak", true);
    private final NumberSetting peakDurationMs = new NumberSetting("PeakDurationMs", "Minimum duration required to classify surge as sustained peak in ms", 500.0, 100.0, 2000.0, 100.0);

    public NetworkTrafficPeakDetector() {
        super("network-traffic-peak-detector", "Network Traffic Peak Detector", "Detects and logs anomalous network traffic surges, identifying timestamp, peak rate, and dominant packet contributors.", Category.NETWORK);
        addSettings(peakThresholdKbPerSec, logPeakDetails, peakDurationMs);
    }

    public NumberSetting getPeakThresholdKbPerSec() {
        return peakThresholdKbPerSec;
    }

    public BooleanSetting getLogPeakDetails() {
        return logPeakDetails;
    }

    public NumberSetting getPeakDurationMs() {
        return peakDurationMs;
    }
}
