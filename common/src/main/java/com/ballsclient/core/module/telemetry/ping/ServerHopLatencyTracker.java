package com.ballsclient.core.module.telemetry.ping;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ServerHopLatencyTracker — Automatically profiles latency differences before and after proxy server transfers,
 * lobby transitions, or dimension hops.
 */
public class ServerHopLatencyTracker extends Module {
    private final NumberSetting compareWindowSamples = new NumberSetting("CompareWindowSamples", "Number of baseline samples recorded before and after hop", 10.0, 3.0, 30.0, 1.0);
    private final BooleanSetting logTransitions = new BooleanSetting("LogTransitions", "Logs latency delta for every server hop transition", true);
    private final BooleanSetting flagDegradation = new BooleanSetting("FlagDegradation", "Highlights hops resulting in significant latency degradation", true);

    public ServerHopLatencyTracker() {
        super("server-hop-latency-tracker", "Server Hop Latency Tracker", "Automatically profiles latency differences before and after proxy server transfers, lobby transitions, or dimension hops.", Category.NETWORK);
        addSettings(compareWindowSamples, logTransitions, flagDegradation);
    }

    public NumberSetting getCompareWindowSamples() {
        return compareWindowSamples;
    }

    public BooleanSetting getLogTransitions() {
        return logTransitions;
    }

    public BooleanSetting getFlagDegradation() {
        return flagDegradation;
    }
}
