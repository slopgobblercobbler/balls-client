package com.ballsclient.core.module.telemetry.bandwidth;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * SocketQueueMonitor — Inspects Netty channel outbound buffer queue depth and socket pending bytes
 * to detect transport backpressure.
 */
public class SocketQueueMonitor extends Module {
    private final NumberSetting maxPendingBytesKb = new NumberSetting("MaxPendingBytesKb", "Channel pending outbound buffer alert threshold in kilobytes", 64.0, 16.0, 512.0, 16.0);
    private final BooleanSetting alertOnBackpressure = new BooleanSetting("AlertOnBackpressure", "Warns when socket write queue exceeds capacity threshold", true);
    private final BooleanSetting monitorChannelFlush = new BooleanSetting("MonitorChannelFlush", "Tracks time elapsed between consecutive channel flushes", false);

    public SocketQueueMonitor() {
        super("socket-queue-monitor", "Socket Queue Monitor", "Inspects Netty channel outbound buffer queue depth and socket pending bytes to detect transport backpressure.", Category.NETWORK);
        addSettings(maxPendingBytesKb, alertOnBackpressure, monitorChannelFlush);
    }

    public NumberSetting getMaxPendingBytesKb() {
        return maxPendingBytesKb;
    }

    public BooleanSetting getAlertOnBackpressure() {
        return alertOnBackpressure;
    }

    public BooleanSetting getMonitorChannelFlush() {
        return monitorChannelFlush;
    }
}
