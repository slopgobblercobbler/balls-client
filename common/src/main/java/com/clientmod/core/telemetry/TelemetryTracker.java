package com.clientmod.core.telemetry;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Headless Telemetry Tracker computing rolling frametimes, 1% lows, and EMA server TPS.
 */
public class TelemetryTracker {
    private static final int MAX_FRAME_SAMPLES = 120;
    private final ArrayDeque<Long> frameDurationsNanos = new ArrayDeque<>(MAX_FRAME_SAMPLES);

    private long lastWorldTime = -1;
    private long lastTimePacketMillis = -1;
    private double currentTps = 20.0;
    private static final double EMA_ALPHA = 0.2; // Smoothing factor for TPS

    public synchronized void recordFrame(long durationNanos) {
        if (durationNanos <= 0) {
            return;
        }
        if (frameDurationsNanos.size() >= MAX_FRAME_SAMPLES) {
            frameDurationsNanos.pollFirst();
        }
        frameDurationsNanos.addLast(durationNanos);
    }

    public synchronized double getAverageFps() {
        if (frameDurationsNanos.isEmpty()) {
            return 0.0;
        }
        long sum = 0;
        for (long d : frameDurationsNanos) {
            sum += d;
        }
        double avgNanos = (double) sum / frameDurationsNanos.size();
        return avgNanos > 0 ? 1_000_000_000.0 / avgNanos : 0.0;
    }

    public synchronized double getOnePercentLowFps() {
        if (frameDurationsNanos.size() < 10) {
            return getAverageFps();
        }
        List<Long> sorted = new ArrayList<>(frameDurationsNanos);
        Collections.sort(sorted);
        // The worst 1% frames are the slowest frames (highest durations at the tail)
        int worstCount = Math.max(1, (int) Math.ceil(sorted.size() * 0.01));
        long sumWorstNanos = 0;
        for (int i = sorted.size() - worstCount; i < sorted.size(); i++) {
            sumWorstNanos += sorted.get(i);
        }
        double avgWorstNanos = (double) sumWorstNanos / worstCount;
        return avgWorstNanos > 0 ? 1_000_000_000.0 / avgWorstNanos : 0.0;
    }

    public synchronized void recordTimePacket(long worldTime, long nowMillis) {
        if (lastWorldTime >= 0 && lastTimePacketMillis > 0) {
            long timeDelta = worldTime - lastWorldTime;
            long millisDelta = nowMillis - lastTimePacketMillis;
            if (millisDelta > 0 && timeDelta >= 0) {
                double instantTps = (timeDelta * 1000.0) / millisDelta;
                instantTps = Math.min(20.0, Math.max(0.0, instantTps));
                currentTps = (EMA_ALPHA * instantTps) + ((1.0 - EMA_ALPHA) * currentTps);
            }
        }
        lastWorldTime = worldTime;
        lastTimePacketMillis = nowMillis;
    }

    public synchronized double getEstimatedTps() {
        return Math.min(20.0, Math.max(0.0, currentTps));
    }

    public static long getAllocatedMemoryMb() {
        Runtime rt = Runtime.getRuntime();
        return (rt.totalMemory() - rt.freeMemory()) / (1024 * 1024);
    }

    public static long getMaxMemoryMb() {
        return Runtime.getRuntime().maxMemory() / (1024 * 1024);
    }
}
