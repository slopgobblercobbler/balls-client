package com.ballsclient.core.module.telemetry.ping;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * PingQuantileHistogram — Aggregates latency distributions into statistical buckets to compute P50, P90, P95,
 * and P99 percentiles for latency SLA analysis.
 */
public class PingQuantileHistogram extends Module {
    private final NumberSetting bucketWidthMs = new NumberSetting("BucketWidthMs", "Latency histogram bucket width in milliseconds", 5.0, 1.0, 25.0, 1.0);
    private final NumberSetting sampleCapacity = new NumberSetting("SampleCapacity", "Maximum histogram sample capacity", 500.0, 100.0, 2000.0, 50.0);
    private final ModeSetting displayPercentiles = new ModeSetting("DisplayPercentiles", "Percentile selection displayed on HUD", "P50_P95_P99", "P50_P99", "P50_P95_P99", "ALL_PERCENTILES");

    public PingQuantileHistogram() {
        super("ping-quantile-histogram", "Ping Quantile Histogram", "Aggregates latency distributions into statistical buckets to compute P50, P90, P95, and P99 percentiles for latency SLA analysis.", Category.NETWORK);
        addSettings(bucketWidthMs, sampleCapacity, displayPercentiles);
    }

    public NumberSetting getBucketWidthMs() {
        return bucketWidthMs;
    }

    public NumberSetting getSampleCapacity() {
        return sampleCapacity;
    }

    public ModeSetting getDisplayPercentiles() {
        return displayPercentiles;
    }
}
