package com.ballsclient.core.module.player.mining;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * BlockBreakTelemetry — Computes blocks-per-minute (BPM), net efficiency, and tool wear rates.
 */
public class BlockBreakTelemetry extends Module {
    private final BooleanSetting displayBpm = new BooleanSetting("DisplayBpm", "Show real-time BPM counter in HUD overlay", true);
    private final BooleanSetting trackDurabilityLoss = new BooleanSetting("TrackDurabilityLoss", "Monitor cumulative tool wear rate", true);
    private final NumberSetting rollingAverageSeconds = new NumberSetting("RollingAverageSeconds", "Time window for rolling rate smoothing", 30.0, 5.0, 120.0, 5.0);

    public BlockBreakTelemetry() {
        super("block-break-telemetry", "Block Break Telemetry", "Computes blocks-per-minute (BPM), net efficiency, and tool wear rates", Category.PLAYER);
        addSettings(displayBpm, trackDurabilityLoss, rollingAverageSeconds);
    }

    public BooleanSetting getDisplayBpm() {
        return displayBpm;
    }

    public BooleanSetting getTrackDurabilityLoss() {
        return trackDurabilityLoss;
    }

    public NumberSetting getRollingAverageSeconds() {
        return rollingAverageSeconds;
    }

    public static double calculateBpm(int totalBlocksMined, double elapsedSeconds) {
        if (elapsedSeconds <= 0.0) return 0.0;
        return (totalBlocksMined / elapsedSeconds) * 60.0;
    }
}
