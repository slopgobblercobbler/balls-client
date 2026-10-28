package com.ballsclient.core.module.telemetry.diagnostics;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * TelemetryEventLogger — High-performance structured event logger streaming network disconnects,
 * latency spikes, and TPS drops to JSON lines.
 */
public class TelemetryEventLogger extends Module {
    private final NumberSetting maxLogFiles = new NumberSetting("MaxLogFiles", "Maximum rotated event log files retained on disk", 5.0, 1.0, 20.0, 1.0);
    private final ModeSetting logLevel = new ModeSetting("LogLevel", "Minimum event severity logged to disk", "WARNINGS_ONLY", "ALL_EVENTS", "WARNINGS_ONLY", "CRITICAL_ONLY");
    private final BooleanSetting asyncDiskWriter = new BooleanSetting("AsyncDiskWriter", "Writes event logs asynchronously on background IO worker", true);

    public TelemetryEventLogger() {
        super("telemetry-event-logger", "Telemetry Event Logger", "High-performance structured event logger streaming network disconnects, latency spikes, and TPS drops to JSON lines.", Category.NETWORK);
        addSettings(maxLogFiles, logLevel, asyncDiskWriter);
    }

    public NumberSetting getMaxLogFiles() {
        return maxLogFiles;
    }

    public ModeSetting getLogLevel() {
        return logLevel;
    }

    public BooleanSetting getAsyncDiskWriter() {
        return asyncDiskWriter;
    }
}
