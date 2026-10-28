package com.ballsclient.core.module.telemetry.ping;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * EchoPacketTimer — Dispatches timestamped custom query/echo packets to measure precise sub-millisecond
 * network transport latency independent of server ticks.
 */
public class EchoPacketTimer extends Module {
    private final NumberSetting echoFrequencyMs = new NumberSetting("EchoFrequencyMs", "Echo packet transmission frequency in milliseconds", 1000.0, 250.0, 5000.0, 250.0);
    private final ModeSetting precision = new ModeSetting("Precision", "Displayed measurement time precision unit", "MILLISECONDS", "MILLISECONDS", "MICROSECONDS", "NANOSECONDS");
    private final BooleanSetting verboseMetrics = new BooleanSetting("VerboseMetrics", "Logs round-trip timestamp details to debug log", false);

    public EchoPacketTimer() {
        super("echo-packet-timer", "Echo Packet Timer", "Dispatches timestamped custom query/echo packets to measure precise sub-millisecond network transport latency independent of server ticks.", Category.NETWORK);
        addSettings(echoFrequencyMs, precision, verboseMetrics);
    }

    public NumberSetting getEchoFrequencyMs() {
        return echoFrequencyMs;
    }

    public ModeSetting getPrecision() {
        return precision;
    }

    public BooleanSetting getVerboseMetrics() {
        return verboseMetrics;
    }
}
