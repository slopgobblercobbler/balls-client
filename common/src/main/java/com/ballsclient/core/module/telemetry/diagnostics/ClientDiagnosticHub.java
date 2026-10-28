package com.ballsclient.core.module.telemetry.diagnostics;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ClientDiagnosticHub — Unified master diagnostic dashboard synthesizing JVM memory,
 * GC pauses, frametimes, server TPS, ping, and network throughput.
 */
public class ClientDiagnosticHub extends Module {
    private final ModeSetting dashboardLayout = new ModeSetting("DashboardLayout", "Diagnostic dashboard UI layout presentation", "COMPACT_HUD", "COMPACT_HUD", "FULL_OVERLAY", "SIDEBAR_PANEL");
    private final BooleanSetting showMemoryMetrics = new BooleanSetting("ShowMemoryMetrics", "Displays JVM allocated, used, and free heap memory metrics", true);
    private final NumberSetting refreshRateHz = new NumberSetting("RefreshRateHz", "Dashboard metrics refresh rate in updates per second", 10.0, 1.0, 60.0, 1.0);

    public ClientDiagnosticHub() {
        super("client-diagnostic-hub", "Client Diagnostic Hub", "Unified master diagnostic dashboard synthesizing JVM memory, GC pauses, frametimes, server TPS, ping, and network throughput.", Category.NETWORK);
        addSettings(dashboardLayout, showMemoryMetrics, refreshRateHz);
    }

    public ModeSetting getDashboardLayout() {
        return dashboardLayout;
    }

    public BooleanSetting getShowMemoryMetrics() {
        return showMemoryMetrics;
    }

    public NumberSetting getRefreshRateHz() {
        return refreshRateHz;
    }
}
