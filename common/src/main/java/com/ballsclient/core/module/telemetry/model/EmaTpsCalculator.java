package com.ballsclient.core.module.telemetry.model;

/**
 * EmaTpsCalculator — Computes server Ticks Per Second (TPS) derived from world-time packets
 * using Exponential Moving Average (EMA) smoothing, discontinuity guards, and tick freeze detection.
 */
public class EmaTpsCalculator {
    public static final double DEFAULT_TARGET_TPS = 20.0;
    public static final int DEFAULT_WINDOW_PERIODS = 9;
    public static final long DEFAULT_FREEZE_THRESHOLD_MS = 2000L;

    private final double alpha;
    private final int windowPeriods;
    private final long freezeThresholdMillis;

    private long lastWorldTime = -1L;
    private long lastTimePacketMillis = -1L;
    private double currentTps = DEFAULT_TARGET_TPS;
    private double instantaneousTps = DEFAULT_TARGET_TPS;
    private long cumulativeTickDrift = 0L;
    private long totalTimePacketsReceived = 0L;
    private boolean frozen = false;

    public EmaTpsCalculator() {
        this(DEFAULT_WINDOW_PERIODS, DEFAULT_FREEZE_THRESHOLD_MS);
    }

    public EmaTpsCalculator(int windowPeriods, long freezeThresholdMillis) {
        if (windowPeriods < 1) {
            throw new IllegalArgumentException("windowPeriods must be at least 1");
        }
        if (freezeThresholdMillis <= 0) {
            throw new IllegalArgumentException("freezeThresholdMillis must be positive");
        }
        this.windowPeriods = windowPeriods;
        this.freezeThresholdMillis = freezeThresholdMillis;
        this.alpha = 2.0 / (windowPeriods + 1.0);
    }

    /**
     * Records an incoming world time packet at current wall-clock timestamp.
     */
    public synchronized void recordTimePacket(long worldTime) {
        recordTimePacket(worldTime, System.currentTimeMillis());
    }

    /**
     * Records an incoming world time packet with deterministic timestamp.
     *
     * @param worldTime server world time ticks
     * @param nowMillis current time in milliseconds
     */
    public synchronized void recordTimePacket(long worldTime, long nowMillis) {
        if (lastWorldTime == -1L || lastTimePacketMillis == -1L) {
            lastWorldTime = worldTime;
            lastTimePacketMillis = nowMillis;
            totalTimePacketsReceived = 1L;
            frozen = false;
            return;
        }

        long deltaTicks = worldTime - lastWorldTime;
        long deltaMillis = nowMillis - lastTimePacketMillis;

        // Discontinuity guard: negative ticks (/time set 0) or negative/zero elapsed time
        if (deltaTicks < 0 || deltaMillis <= 0) {
            lastWorldTime = worldTime;
            lastTimePacketMillis = nowMillis;
            return;
        }

        double rawInstantTps = (deltaTicks * 1000.0) / (double) deltaMillis;
        instantaneousTps = Math.min(DEFAULT_TARGET_TPS, Math.max(0.0, rawInstantTps));

        // Exponential moving average: TPS_k = alpha * TPS_instant + (1 - alpha) * TPS_{k-1}
        currentTps = (alpha * instantaneousTps) + ((1.0 - alpha) * currentTps);
        currentTps = Math.min(DEFAULT_TARGET_TPS, Math.max(0.0, currentTps));

        // Tick drift tracking: expected ticks = deltaMillis / 50.0 (20 ticks/sec = 50ms/tick)
        double expectedTicks = deltaMillis / 50.0;
        double driftDelta = expectedTicks - deltaTicks;
        cumulativeTickDrift += Math.round(driftDelta);

        lastWorldTime = worldTime;
        lastTimePacketMillis = nowMillis;
        totalTimePacketsReceived++;
        frozen = false;
    }

    /**
     * Checks if the server tick stream is frozen based on current system time.
     */
    public synchronized boolean isFrozen() {
        return isFrozen(System.currentTimeMillis());
    }

    /**
     * Checks if the server tick stream is frozen based on given timestamp.
     */
    public synchronized boolean isFrozen(long nowMillis) {
        if (lastTimePacketMillis <= 0) {
            return false;
        }
        return (nowMillis - lastTimePacketMillis) >= freezeThresholdMillis;
    }

    /**
     * Gets smoothed TPS, applying exponential decay if tick updates are frozen.
     */
    public synchronized double getTps() {
        return getTps(System.currentTimeMillis());
    }

    /**
     * Gets smoothed TPS at given timestamp with freeze decay.
     */
    public synchronized double getTps(long nowMillis) {
        if (isFrozen(nowMillis)) {
            long elapsedPastFreeze = (nowMillis - lastTimePacketMillis) - freezeThresholdMillis;
            if (elapsedPastFreeze > 0) {
                double decayed = currentTps * Math.exp(-elapsedPastFreeze / 1000.0);
                return Math.max(0.0, decayed);
            }
        }
        return currentTps;
    }

    public synchronized double getInstantaneousTps() {
        return instantaneousTps;
    }

    public synchronized long getCumulativeTickDrift() {
        return cumulativeTickDrift;
    }

    public synchronized long getTotalTimePacketsReceived() {
        return totalTimePacketsReceived;
    }

    public double getAlpha() {
        return alpha;
    }

    public int getWindowPeriods() {
        return windowPeriods;
    }

    public long getFreezeThresholdMillis() {
        return freezeThresholdMillis;
    }

    public synchronized void reset() {
        lastWorldTime = -1L;
        lastTimePacketMillis = -1L;
        currentTps = DEFAULT_TARGET_TPS;
        instantaneousTps = DEFAULT_TARGET_TPS;
        cumulativeTickDrift = 0L;
        totalTimePacketsReceived = 0L;
        frozen = false;
    }
}
