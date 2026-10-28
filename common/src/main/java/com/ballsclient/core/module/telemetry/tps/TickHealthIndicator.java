package com.ballsclient.core.module.telemetry.tps;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * TickHealthIndicator — Color-coded health badge synthesizing server TPS, tick variance,
 * and packet gaps into a green/yellow/red server health metric.
 */
public class TickHealthIndicator extends Module {
    private final NumberSetting greenTpsThreshold = new NumberSetting("GreenTpsThreshold", "Minimum TPS required for green healthy status", 19.0, 15.0, 20.0, 0.5);
    private final NumberSetting yellowTpsThreshold = new NumberSetting("YellowTpsThreshold", "Minimum TPS required for yellow caution status", 15.0, 10.0, 18.0, 0.5);
    private final BooleanSetting compactBadge = new BooleanSetting("CompactBadge", "Renders compact icon badge instead of full text", false);

    public TickHealthIndicator() {
        super("tick-health-indicator", "Tick Health Indicator", "Color-coded health badge synthesizing server TPS, tick variance, and packet gaps into a green/yellow/red server health metric.", Category.NETWORK);
        addSettings(greenTpsThreshold, yellowTpsThreshold, compactBadge);
    }

    public NumberSetting getGreenTpsThreshold() {
        return greenTpsThreshold;
    }

    public NumberSetting getYellowTpsThreshold() {
        return yellowTpsThreshold;
    }

    public BooleanSetting getCompactBadge() {
        return compactBadge;
    }
}
