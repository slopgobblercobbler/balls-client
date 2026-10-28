package com.ballsclient.core.module.telemetry.ping;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * MultiServerPingComparator — Concurrently queries and ranks latency across configured alternate server nodes
 * or regions to identify the lowest latency route.
 */
public class MultiServerPingComparator extends Module {
    private final NumberSetting pollIntervalSec = new NumberSetting("PollIntervalSec", "Background query polling interval in seconds", 60.0, 10.0, 300.0, 10.0);
    private final ModeSetting sortOrder = new ModeSetting("SortOrder", "Server ranking sorting order", "LOWEST_PING", "LOWEST_PING", "ALPHABETICAL", "PACKET_LOSS");
    private final BooleanSetting autoSuggestSwitch = new BooleanSetting("AutoSuggestSwitch", "Suggests optimal server routing node upon ping degradation", false);

    public MultiServerPingComparator() {
        super("multi-server-ping-comparator", "Multi-Server Ping Comparator", "Concurrently queries and ranks latency across configured alternate server nodes or regions to identify the lowest latency route.", Category.NETWORK);
        addSettings(pollIntervalSec, sortOrder, autoSuggestSwitch);
    }

    public NumberSetting getPollIntervalSec() {
        return pollIntervalSec;
    }

    public ModeSetting getSortOrder() {
        return sortOrder;
    }

    public BooleanSetting getAutoSuggestSwitch() {
        return autoSuggestSwitch;
    }
}
