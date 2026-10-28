package com.ballsclient.core.module.telemetry.diagnostics;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ChunkLoadProfiler — Tracks chunk loading, unloading, and lighting updates per second
 * with average chunk arrival duration metrics.
 */
public class ChunkLoadProfiler extends Module {
    private final BooleanSetting trackUnloads = new BooleanSetting("TrackUnloads", "Records and displays chunk unload events per second", true);
    private final BooleanSetting showChunksPerSecond = new BooleanSetting("ShowChunksPerSecond", "Displays real-time chunk arrival throughput rate", true);
    private final NumberSetting slowChunkThresholdMs = new NumberSetting("SlowChunkThresholdMs", "Processing delay threshold flagging slow chunk arrival in ms", 50.0, 10.0, 200.0, 10.0);

    public ChunkLoadProfiler() {
        super("chunk-load-profiler", "Chunk Load Profiler", "Tracks chunk loading, unloading, and lighting updates per second with average chunk arrival duration metrics.", Category.NETWORK);
        addSettings(trackUnloads, showChunksPerSecond, slowChunkThresholdMs);
    }

    public BooleanSetting getTrackUnloads() {
        return trackUnloads;
    }

    public BooleanSetting getShowChunksPerSecond() {
        return showChunksPerSecond;
    }

    public NumberSetting getSlowChunkThresholdMs() {
        return slowChunkThresholdMs;
    }
}
