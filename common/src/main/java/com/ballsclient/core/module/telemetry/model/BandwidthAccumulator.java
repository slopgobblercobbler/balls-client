package com.ballsclient.core.module.telemetry.model;

import java.util.concurrent.atomic.LongAdder;

/**
 * BandwidthAccumulator — Tracks network wire throughput, uncompressed payload sizes,
 * compression efficiency ratios, peak transfer speeds, and bandwidth budget quotas.
 */
public class BandwidthAccumulator {
    public static final double DEFAULT_BUDGET_LIMIT_BYTES = 500.0 * 1024.0 * 1024.0; // 500 MB

    private final LongAdder totalInboundBytes = new LongAdder();
    private final LongAdder totalOutboundBytes = new LongAdder();
    private final LongAdder uncompressedBytesIn = new LongAdder();
    private final LongAdder compressedBytesIn = new LongAdder();
    private final LongAdder uncompressedBytesOut = new LongAdder();
    private final LongAdder compressedBytesOut = new LongAdder();

    private volatile double peakInboundBytesPerSec = 0.0;
    private volatile double peakOutboundBytesPerSec = 0.0;
    private final double budgetLimitBytes;

    private final PacketTelemetryTracker.SlidingRateCounter inboundThroughput =
            new PacketTelemetryTracker.SlidingRateCounter(1000L, 10);
    private final PacketTelemetryTracker.SlidingRateCounter outboundThroughput =
            new PacketTelemetryTracker.SlidingRateCounter(1000L, 10);

    public BandwidthAccumulator() {
        this(DEFAULT_BUDGET_LIMIT_BYTES);
    }

    public BandwidthAccumulator(double budgetLimitBytes) {
        this.budgetLimitBytes = Math.max(0.0, budgetLimitBytes);
    }

    public void recordInbound(long wireBytes, long uncompressedBytes) {
        recordInbound(wireBytes, uncompressedBytes, System.currentTimeMillis());
    }

    public synchronized void recordInbound(long wireBytes, long uncompressedBytes, long nowMillis) {
        long safeWire = Math.max(0L, wireBytes);
        long safeUncompressed = Math.max(0L, uncompressedBytes);

        totalInboundBytes.add(safeWire);
        compressedBytesIn.add(safeWire);
        uncompressedBytesIn.add(safeUncompressed);

        inboundThroughput.record(safeWire, nowMillis);
        double currentRate = inboundThroughput.getByteRate(nowMillis);
        if (currentRate > peakInboundBytesPerSec) {
            peakInboundBytesPerSec = currentRate;
        }
    }

    public void recordOutbound(long wireBytes, long uncompressedBytes) {
        recordOutbound(wireBytes, uncompressedBytes, System.currentTimeMillis());
    }

    public synchronized void recordOutbound(long wireBytes, long uncompressedBytes, long nowMillis) {
        long safeWire = Math.max(0L, wireBytes);
        long safeUncompressed = Math.max(0L, uncompressedBytes);

        totalOutboundBytes.add(safeWire);
        compressedBytesOut.add(safeWire);
        uncompressedBytesOut.add(safeUncompressed);

        outboundThroughput.record(safeWire, nowMillis);
        double currentRate = outboundThroughput.getByteRate(nowMillis);
        if (currentRate > peakOutboundBytesPerSec) {
            peakOutboundBytesPerSec = currentRate;
        }
    }

    public double getInboundThroughputKbps() {
        return getInboundThroughputKbps(System.currentTimeMillis());
    }

    public double getInboundThroughputKbps(long nowMillis) {
        return inboundThroughput.getByteRate(nowMillis) / 1024.0;
    }

    public double getOutboundThroughputKbps() {
        return getOutboundThroughputKbps(System.currentTimeMillis());
    }

    public double getOutboundThroughputKbps(long nowMillis) {
        return outboundThroughput.getByteRate(nowMillis) / 1024.0;
    }

    public double getPeakInboundThroughputKbps() {
        return peakInboundBytesPerSec / 1024.0;
    }

    public double getPeakOutboundThroughputKbps() {
        return peakOutboundBytesPerSec / 1024.0;
    }

    public double getInboundCompressionRatio() {
        long wire = compressedBytesIn.sum();
        long uncompressed = uncompressedBytesIn.sum();
        if (wire <= 0) {
            return 1.0;
        }
        return (double) uncompressed / (double) wire;
    }

    public double getOutboundCompressionRatio() {
        long wire = compressedBytesOut.sum();
        long uncompressed = uncompressedBytesOut.sum();
        if (wire <= 0) {
            return 1.0;
        }
        return (double) uncompressed / (double) wire;
    }

    public double getTotalCompressionRatio() {
        long wire = compressedBytesIn.sum() + compressedBytesOut.sum();
        long uncompressed = uncompressedBytesIn.sum() + uncompressedBytesOut.sum();
        if (wire <= 0) {
            return 1.0;
        }
        return (double) uncompressed / (double) wire;
    }

    public double getInboundSavingsPercent() {
        long wire = compressedBytesIn.sum();
        long uncompressed = uncompressedBytesIn.sum();
        if (uncompressed <= 0) {
            return 0.0;
        }
        double savings = (1.0 - ((double) wire / (double) uncompressed)) * 100.0;
        return Math.max(0.0, Math.min(100.0, savings));
    }

    public double getOutboundSavingsPercent() {
        long wire = compressedBytesOut.sum();
        long uncompressed = uncompressedBytesOut.sum();
        if (uncompressed <= 0) {
            return 0.0;
        }
        double savings = (1.0 - ((double) wire / (double) uncompressed)) * 100.0;
        return Math.max(0.0, Math.min(100.0, savings));
    }

    public double getTotalSavingsPercent() {
        long wire = compressedBytesIn.sum() + compressedBytesOut.sum();
        long uncompressed = uncompressedBytesIn.sum() + uncompressedBytesOut.sum();
        if (uncompressed <= 0) {
            return 0.0;
        }
        double savings = (1.0 - ((double) wire / (double) uncompressed)) * 100.0;
        return Math.max(0.0, Math.min(100.0, savings));
    }

    public double getBudgetConsumedPercent() {
        if (budgetLimitBytes <= 0.0) {
            return 0.0;
        }
        double totalBytes = (double) (totalInboundBytes.sum() + totalOutboundBytes.sum());
        return (totalBytes / budgetLimitBytes) * 100.0;
    }

    public boolean isBudgetExceeded() {
        if (budgetLimitBytes <= 0.0) {
            return false;
        }
        return (totalInboundBytes.sum() + totalOutboundBytes.sum()) >= budgetLimitBytes;
    }

    public long getTotalInboundBytes() {
        return totalInboundBytes.sum();
    }

    public long getTotalOutboundBytes() {
        return totalOutboundBytes.sum();
    }

    public double getBudgetLimitBytes() {
        return budgetLimitBytes;
    }

    public synchronized void reset() {
        totalInboundBytes.reset();
        totalOutboundBytes.reset();
        uncompressedBytesIn.reset();
        compressedBytesIn.reset();
        uncompressedBytesOut.reset();
        compressedBytesOut.reset();
        peakInboundBytesPerSec = 0.0;
        peakOutboundBytesPerSec = 0.0;
        inboundThroughput.reset();
        outboundThroughput.reset();
    }
}
