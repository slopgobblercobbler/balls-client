package com.ballsclient.core.module.telemetry.ping;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * NetworkSmoothnessGauge — Visual dial or gauge indicator synthesizing ping stability, jitter,
 * and packet loss into an overall connection quality rating.
 */
public class NetworkSmoothnessGauge extends Module {
    private final ModeSetting gaugeStyle = new ModeSetting("GaugeStyle", "Visual render style of connection quality gauge", "RADIAL_ARC", "RADIAL_ARC", "HORIZONTAL_BAR", "COMPACT_PERCENT");
    private final NumberSetting weightJitter = new NumberSetting("WeightJitter", "Weight assigned to jitter variance in quality score", 0.4, 0.1, 0.8, 0.05);
    private final NumberSetting weightLoss = new NumberSetting("WeightLoss", "Weight assigned to packet loss in quality score", 0.6, 0.2, 0.9, 0.05);

    public NetworkSmoothnessGauge() {
        super("network-smoothness-gauge", "Network Smoothness Gauge", "Visual dial or gauge indicator synthesizing ping stability, jitter, and packet loss into an overall connection quality rating.", Category.NETWORK);
        addSettings(gaugeStyle, weightJitter, weightLoss);
    }

    public ModeSetting getGaugeStyle() {
        return gaugeStyle;
    }

    public NumberSetting getWeightJitter() {
        return weightJitter;
    }

    public NumberSetting getWeightLoss() {
        return weightLoss;
    }
}
