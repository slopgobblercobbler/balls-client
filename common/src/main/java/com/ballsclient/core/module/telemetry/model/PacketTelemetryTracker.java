package com.ballsclient.core.module.telemetry.model;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * PacketTelemetryTracker — High-throughput thread-safe tracker for inbound and outbound packet metrics.
 * Uses lock-free ConcurrentHashMap and LongAdder counters, sliding time-window rate estimators,
 * and top-N packet ranking analysis.
 */
public class PacketTelemetryTracker {

    /**
     * Aggregated metrics for a single packet class type.
     */
    public static class PacketTypeStats {
        private final LongAdder count = new LongAdder();
        private final LongAdder bytes = new LongAdder();

        public void record(long byteLength) {
            count.increment();
            bytes.add(Math.max(0L, byteLength));
        }

        public long getCount() {
            return count.sum();
        }

        public long getBytes() {
            return bytes.sum();
        }

        public void reset() {
            count.reset();
            bytes.reset();
        }
    }

    /**
     * High-precision thread-safe sliding window rate estimator.
     */
    public static class SlidingRateCounter {
        private static class Slice {
            final AtomicLong windowStartMillis = new AtomicLong(-1L);
            final LongAdder packetCount = new LongAdder();
            final LongAdder byteCount = new LongAdder();
        }

        private final int numSlices;
        private final long sliceDurationMillis;
        private final long windowDurationMillis;
        private final Slice[] slices;

        public SlidingRateCounter(long windowDurationMillis, int numSlices) {
            this.windowDurationMillis = windowDurationMillis;
            this.numSlices = numSlices;
            this.sliceDurationMillis = Math.max(1L, windowDurationMillis / numSlices);
            this.slices = new Slice[numSlices];
            for (int i = 0; i < numSlices; i++) {
                this.slices[i] = new Slice();
            }
        }

        public void record(long byteLength, long timestampMillis) {
            int index = (int) ((timestampMillis / sliceDurationMillis) % numSlices);
            if (index < 0) {
                index += numSlices;
            }
            Slice slice = slices[index];
            long expectedStart = (timestampMillis / sliceDurationMillis) * sliceDurationMillis;
            long currentStart = slice.windowStartMillis.get();

            if (currentStart != expectedStart) {
                if (slice.windowStartMillis.compareAndSet(currentStart, expectedStart)) {
                    slice.packetCount.reset();
                    slice.byteCount.reset();
                }
            }

            slice.packetCount.increment();
            slice.byteCount.add(Math.max(0L, byteLength));
        }

        public double getPacketRate(long nowMillis) {
            RateSnapshot snapshot = computeSnapshot(nowMillis);
            return snapshot.packetsPerSecond;
        }

        public double getByteRate(long nowMillis) {
            RateSnapshot snapshot = computeSnapshot(nowMillis);
            return snapshot.bytesPerSecond;
        }

        private static class RateSnapshot {
            double packetsPerSecond = 0.0;
            double bytesPerSecond = 0.0;
        }

        private RateSnapshot computeSnapshot(long nowMillis) {
            RateSnapshot result = new RateSnapshot();
            long cutoff = nowMillis - windowDurationMillis;

            long totalPackets = 0L;
            long totalBytes = 0L;
            long minSliceStart = Long.MAX_VALUE;
            long maxSliceStart = Long.MIN_VALUE;

            for (Slice slice : slices) {
                long start = slice.windowStartMillis.get();
                if (start >= cutoff && start <= nowMillis) {
                    totalPackets += slice.packetCount.sum();
                    totalBytes += slice.byteCount.sum();
                    if (start < minSliceStart) {
                        minSliceStart = start;
                    }
                    if (start > maxSliceStart) {
                        maxSliceStart = start;
                    }
                }
            }

            if (totalPackets == 0 || minSliceStart == Long.MAX_VALUE) {
                return result;
            }

            long activeSpanMillis = (maxSliceStart - minSliceStart) + sliceDurationMillis;
            activeSpanMillis = Math.min(windowDurationMillis, Math.max(sliceDurationMillis, activeSpanMillis));
            double activeSeconds = (double) activeSpanMillis / 1000.0;

            result.packetsPerSecond = (double) totalPackets / activeSeconds;
            result.bytesPerSecond = (double) totalBytes / activeSeconds;
            return result;
        }

        public void reset() {
            for (Slice slice : slices) {
                slice.windowStartMillis.set(-1L);
                slice.packetCount.reset();
                slice.byteCount.reset();
            }
        }
    }

    private final ConcurrentHashMap<String, PacketTypeStats> inboundStats = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, PacketTypeStats> outboundStats = new ConcurrentHashMap<>();

    private final SlidingRateCounter inboundRate = new SlidingRateCounter(1000L, 10);
    private final SlidingRateCounter outboundRate = new SlidingRateCounter(1000L, 10);

    private final LongAdder totalInboundPackets = new LongAdder();
    private final LongAdder totalInboundBytes = new LongAdder();
    private final LongAdder totalOutboundPackets = new LongAdder();
    private final LongAdder totalOutboundBytes = new LongAdder();

    public void recordInbound(String packetType, long byteLength) {
        recordInbound(packetType, byteLength, System.currentTimeMillis());
    }

    public void recordInbound(String packetType, long byteLength, long timestampMillis) {
        inboundStats.computeIfAbsent(packetType, k -> new PacketTypeStats()).record(byteLength);
        inboundRate.record(byteLength, timestampMillis);
        totalInboundPackets.increment();
        totalInboundBytes.add(Math.max(0L, byteLength));
    }

    public void recordOutbound(String packetType, long byteLength) {
        recordOutbound(packetType, byteLength, System.currentTimeMillis());
    }

    public void recordOutbound(String packetType, long byteLength, long timestampMillis) {
        outboundStats.computeIfAbsent(packetType, k -> new PacketTypeStats()).record(byteLength);
        outboundRate.record(byteLength, timestampMillis);
        totalOutboundPackets.increment();
        totalOutboundBytes.add(Math.max(0L, byteLength));
    }

    public double getInboundPacketRate() {
        return inboundRate.getPacketRate(System.currentTimeMillis());
    }

    public double getInboundPacketRate(long nowMillis) {
        return inboundRate.getPacketRate(nowMillis);
    }

    public double getInboundByteRate() {
        return inboundRate.getByteRate(System.currentTimeMillis());
    }

    public double getInboundByteRate(long nowMillis) {
        return inboundRate.getByteRate(nowMillis);
    }

    public double getOutboundPacketRate() {
        return outboundRate.getPacketRate(System.currentTimeMillis());
    }

    public double getOutboundPacketRate(long nowMillis) {
        return outboundRate.getPacketRate(nowMillis);
    }

    public double getOutboundByteRate() {
        return outboundRate.getByteRate(System.currentTimeMillis());
    }

    public double getOutboundByteRate(long nowMillis) {
        return outboundRate.getByteRate(nowMillis);
    }

    public long getTotalInboundPackets() {
        return totalInboundPackets.sum();
    }

    public long getTotalInboundBytes() {
        return totalInboundBytes.sum();
    }

    public long getTotalOutboundPackets() {
        return totalOutboundPackets.sum();
    }

    public long getTotalOutboundBytes() {
        return totalOutboundBytes.sum();
    }

    public Map<String, PacketTypeStats> getInboundStats() {
        return Collections.unmodifiableMap(inboundStats);
    }

    public Map<String, PacketTypeStats> getOutboundStats() {
        return Collections.unmodifiableMap(outboundStats);
    }

    public List<Map.Entry<String, PacketTypeStats>> getTopInboundPacketsByCount(int limit) {
        return getTopPackets(inboundStats, Comparator.comparingLong(e -> e.getValue().getCount()), limit);
    }

    public List<Map.Entry<String, PacketTypeStats>> getTopInboundPacketsByBytes(int limit) {
        return getTopPackets(inboundStats, Comparator.comparingLong(e -> e.getValue().getBytes()), limit);
    }

    public List<Map.Entry<String, PacketTypeStats>> getTopOutboundPacketsByCount(int limit) {
        return getTopPackets(outboundStats, Comparator.comparingLong(e -> e.getValue().getCount()), limit);
    }

    public List<Map.Entry<String, PacketTypeStats>> getTopOutboundPacketsByBytes(int limit) {
        return getTopPackets(outboundStats, Comparator.comparingLong(e -> e.getValue().getBytes()), limit);
    }

    private List<Map.Entry<String, PacketTypeStats>> getTopPackets(
            Map<String, PacketTypeStats> map,
            Comparator<Map.Entry<String, PacketTypeStats>> comparator,
            int limit) {
        return map.entrySet().stream()
                .sorted(comparator.reversed())
                .limit(Math.max(1, limit))
                .collect(Collectors.toList());
    }

    public void reset() {
        inboundStats.clear();
        outboundStats.clear();
        inboundRate.reset();
        outboundRate.reset();
        totalInboundPackets.reset();
        totalInboundBytes.reset();
        totalOutboundPackets.reset();
        totalOutboundBytes.reset();
    }
}
