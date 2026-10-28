package com.ballsclient.core.module.telemetry;

import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.telemetry.model.*;
import com.ballsclient.core.setting.*;
import com.google.gson.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

@DisplayName("Track 8 Telemetry Empirical Stress Harness")
class TelemetryStressHarnessTest {

    // =========================================================================
    // 1. EmaTpsCalculator Stress
    // =========================================================================

    @Test
    @DisplayName("EmaTpsCalculator: Concurrent time packet updates across 50 threads with readers")
    void testEmaTpsCalculatorConcurrencyAcross50Threads() throws InterruptedException {
        EmaTpsCalculator calc = new EmaTpsCalculator(9, 2000L);
        int threadCount = 50;
        int packetsPerThread = 500;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount + 5);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);
        AtomicBoolean running = new AtomicBoolean(true);

        AtomicLong sharedMillis = new AtomicLong(100_000L);
        AtomicLong sharedWorldTime = new AtomicLong(10_000L);

        // 5 concurrent reader threads querying status while updates occur
        for (int r = 0; r < 5; r++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    while (running.get()) {
                        double tps = calc.getTps();
                        double instant = calc.getInstantaneousTps();
                        boolean frozen = calc.isFrozen();
                        long drift = calc.getCumulativeTickDrift();

                        assertThat(Double.isNaN(tps)).isFalse();
                        assertThat(Double.isInfinite(tps)).isFalse();
                        assertThat(tps).isGreaterThanOrEqualTo(0.0).isLessThanOrEqualTo(20.0);
                        assertThat(Double.isNaN(instant)).isFalse();
                        assertThat(instant).isGreaterThanOrEqualTo(0.0).isLessThanOrEqualTo(20.0);
                        Thread.yield();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // 50 concurrent worker threads updating time packets
        for (int t = 0; t < threadCount; t++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    for (int i = 0; i < packetsPerThread; i++) {
                        long now = sharedMillis.addAndGet(50L);
                        long wt = sharedWorldTime.addAndGet(1L);
                        calc.recordTimePacket(wt, now);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        boolean finished = doneLatch.await(15, TimeUnit.SECONDS);
        running.set(false);
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        assertThat(finished).as("50 concurrent worker threads finished in time").isTrue();
        assertThat(calc.getTotalTimePacketsReceived()).isGreaterThan(0L);
        double finalTps = calc.getTps();
        assertThat(Double.isNaN(finalTps)).isFalse();
        assertThat(Double.isInfinite(finalTps)).isFalse();
        assertThat(finalTps).isGreaterThanOrEqualTo(0.0).isLessThanOrEqualTo(20.0);
    }

    @Test
    @DisplayName("EmaTpsCalculator: Large negative discontinuities (/time set leaps) cleanly reset anchor without NaN")
    void testEmaTpsCalculatorNegativeDiscontinuityResets() {
        EmaTpsCalculator calc = new EmaTpsCalculator(9, 2000L);
        long worldTime = 18_000L;
        long timeMillis = 100_000L;

        // Initial anchor and normal steady ticks
        calc.recordTimePacket(worldTime, timeMillis);
        worldTime += 20L;
        timeMillis += 1000L;
        calc.recordTimePacket(worldTime, timeMillis);
        assertThat(calc.getTps(timeMillis)).isEqualTo(20.0);

        // Discontinuity 1: /time set 0 (leap from 18,020 to 0 -> deltaTicks = -18,020)
        worldTime = 0L;
        timeMillis += 1000L;
        calc.recordTimePacket(worldTime, timeMillis);

        double tpsAfterLeap = calc.getTps(timeMillis);
        assertThat(Double.isNaN(tpsAfterLeap)).isFalse();
        assertThat(Double.isInfinite(tpsAfterLeap)).isFalse();
        assertThat(tpsAfterLeap).isEqualTo(20.0);

        // Resume progression from new anchor 0 -> 20 ticks in 1000ms
        worldTime += 20L;
        timeMillis += 1000L;
        calc.recordTimePacket(worldTime, timeMillis);
        assertThat(calc.getInstantaneousTps()).isEqualTo(20.0);
        assertThat(calc.getTps(timeMillis)).isCloseTo(20.0, offset(0.001));

        // Discontinuity 2: Large backwards leap from 24,000 to 6,000
        worldTime = 24_000L;
        timeMillis += 1000L;
        calc.recordTimePacket(worldTime, timeMillis);

        worldTime = 6_000L;
        timeMillis += 1000L;
        calc.recordTimePacket(worldTime, timeMillis);
        assertThat(Double.isNaN(calc.getTps(timeMillis))).isFalse();
        assertThat(calc.getTps(timeMillis)).isGreaterThanOrEqualTo(0.0).isLessThanOrEqualTo(20.0);

        // Discontinuity 3: Zero or negative elapsed wall clock time (deltaMillis <= 0)
        calc.recordTimePacket(6_020L, timeMillis); // deltaMillis = 0
        assertThat(Double.isNaN(calc.getTps(timeMillis))).isFalse();
        assertThat(calc.getTps(timeMillis)).isGreaterThanOrEqualTo(0.0).isLessThanOrEqualTo(20.0);
    }

    @Test
    @DisplayName("EmaTpsCalculator: Severe lag spikes (5,000ms+ pauses) follow exponential freeze decay")
    void testEmaTpsCalculatorSevereLagSpikesAndDecay() {
        EmaTpsCalculator calc = new EmaTpsCalculator(9, 2000L); // Freeze threshold: 2000ms
        long worldTime = 5_000L;
        long timeMillis = 50_000L;

        calc.recordTimePacket(worldTime, timeMillis);
        worldTime += 20L;
        timeMillis += 1000L;
        calc.recordTimePacket(worldTime, timeMillis);
        assertThat(calc.getTps(timeMillis)).isEqualTo(20.0);

        // Check 1500ms after last packet: not frozen
        assertThat(calc.isFrozen(timeMillis + 1500L)).isFalse();
        assertThat(calc.getTps(timeMillis + 1500L)).isEqualTo(20.0);

        // Check 5,000ms lag spike (elapsed = 5,000ms, elapsedPastFreeze = 5,000 - 2,000 = 3,000ms)
        long pause5000 = timeMillis + 5000L;
        assertThat(calc.isFrozen(pause5000)).isTrue();
        double expectedDecay3s = 20.0 * Math.exp(-3.0); // ~0.9957
        assertThat(calc.getTps(pause5000)).isCloseTo(expectedDecay3s, offset(0.05));

        // Check 10,000ms lag spike (elapsedPastFreeze = 8,000ms)
        long pause10000 = timeMillis + 10_000L;
        assertThat(calc.isFrozen(pause10000)).isTrue();
        double expectedDecay8s = 20.0 * Math.exp(-8.0); // ~0.0067
        assertThat(calc.getTps(pause10000)).isCloseTo(expectedDecay8s, offset(0.01));

        // Server resumes after lag spike: packet arrives after 12,000ms
        worldTime += 20L;
        timeMillis += 12_000L;
        calc.recordTimePacket(worldTime, timeMillis);
        assertThat(calc.isFrozen(timeMillis)).isFalse();
        assertThat(calc.getInstantaneousTps()).isCloseTo((20.0 * 1000.0) / 12_000.0, offset(0.01));
    }

    // =========================================================================
    // 2. FrametimeRingBuffer Stress
    // =========================================================================

    @Test
    @DisplayName("FrametimeRingBuffer: 250,000 high-volume frame pushes verify O(1) wrap and sum precision")
    void testFrametimeRingBufferHighVolumePushes() {
        int capacity = 240;
        FrametimeRingBuffer buffer = new FrametimeRingBuffer(capacity);
        int totalFrames = 250_000;
        long[] lastFrames = new long[capacity];

        for (int i = 0; i < totalFrames; i++) {
            // Frame duration between 8ms and 32ms
            long frameDurationNanos = 8_000_000L + ((i * 31L) % 24_000_000L);
            buffer.recordFrame(frameDurationNanos);
            lastFrames[i % capacity] = frameDurationNanos;
        }

        assertThat(buffer.getSize()).isEqualTo(capacity);

        // Independent running sum calculation from valid samples
        long[] validSamples = buffer.getValidSamples();
        assertThat(validSamples).hasSize(capacity);
        long expectedSum = 0L;
        for (long s : validSamples) {
            expectedSum += s;
        }
        assertThat(buffer.getRunningSumNanos()).isEqualTo(expectedSum);

        double expectedAvgNanos = (double) expectedSum / capacity;
        assertThat(buffer.getAverageFrametimeNanos()).isCloseTo(expectedAvgNanos, offset(0.0001));
        assertThat(buffer.getAverageFps()).isCloseTo(1_000_000_000.0 / expectedAvgNanos, offset(0.0001));

        // Independent population standard deviation calculation
        double sumSqDiff = 0.0;
        for (long s : validSamples) {
            double diff = (double) s - expectedAvgNanos;
            sumSqDiff += diff * diff;
        }
        double expectedStdDev = Math.sqrt(sumSqDiff / capacity);
        assertThat(buffer.getStandardDeviationNanos()).isCloseTo(expectedStdDev, offset(0.0001));
    }

    @Test
    @DisplayName("FrametimeRingBuffer: Severe stutter spike injections match brute-force 1% and 0.1% low quantiles")
    void testFrametimeRingBufferStutterSpikesAndQuantiles() {
        int capacity = 1000;
        FrametimeRingBuffer buffer = new FrametimeRingBuffer(capacity);
        long smoothFrameNanos = 16_666_666L; // ~60 FPS

        // 990 smooth frames
        for (int i = 0; i < 990; i++) {
            buffer.recordFrame(smoothFrameNanos);
        }

        // 10 severe stutter hitches:
        // 5 of 100ms, 3 of 250ms, 2 of 500ms
        for (int i = 0; i < 5; i++) {
            buffer.recordFrame(100_000_000L); // 100ms
        }
        for (int i = 0; i < 3; i++) {
            buffer.recordFrame(250_000_000L); // 250ms
        }
        for (int i = 0; i < 2; i++) {
            buffer.recordFrame(500_000_000L); // 500ms
        }

        assertThat(buffer.getSize()).isEqualTo(1000);

        // Independent brute-force quantile verification
        long[] samples = buffer.getValidSamples();
        Arrays.sort(samples);

        // 1% worst frames: k = ceil(1000 * 0.01) = 10 frames
        int k1 = 10;
        long worst1Sum = 0L;
        for (int i = 1000 - k1; i < 1000; i++) {
            worst1Sum += samples[i];
        }
        double expected1PercentAvgNanos = (double) worst1Sum / (double) k1;
        double expected1PercentLowFps = 1_000_000_000.0 / expected1PercentAvgNanos;
        assertThat(buffer.getOnePercentLowFps()).isCloseTo(expected1PercentLowFps, offset(0.0001));

        // 0.1% worst frames: k = ceil(1000 * 0.001) = 1 frame (500ms)
        int k01 = 1;
        long worst01Sum = samples[1000 - k01];
        double expected01PercentAvgNanos = (double) worst01Sum / (double) k01;
        double expected01PercentLowFps = 1_000_000_000.0 / expected01PercentAvgNanos;
        assertThat(buffer.getPointOnePercentLowFps()).isCloseTo(expected01PercentLowFps, offset(0.0001));
        assertThat(buffer.getPointOnePercentLowFps()).isCloseTo(2.0, offset(0.0001)); // 500ms = 2 FPS
    }

    // =========================================================================
    // 3. PacketTelemetryTracker Stress
    // =========================================================================

    @Test
    @DisplayName("PacketTelemetryTracker: 100,000 packets across 16 threads with concurrent readers verify zero lost counts")
    void testPacketTelemetryTrackerExtremeConcurrency() throws InterruptedException {
        PacketTelemetryTracker tracker = new PacketTelemetryTracker();
        int threadCount = 16;
        int packetsPerThread = 6250; // 16 * 6250 = 100,000 packets
        ExecutorService executor = Executors.newFixedThreadPool(threadCount + 4);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);
        AtomicBoolean running = new AtomicBoolean(true);

        // 4 concurrent readers
        for (int r = 0; r < 4; r++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    while (running.get()) {
                        double inRate = tracker.getInboundPacketRate();
                        double outByteRate = tracker.getOutboundByteRate();
                        long inBytes = tracker.getTotalInboundBytes();
                        List<?> top = tracker.getTopInboundPacketsByCount(5);

                        assertThat(inRate).isGreaterThanOrEqualTo(0.0);
                        assertThat(outByteRate).isGreaterThanOrEqualTo(0.0);
                        assertThat(inBytes).isGreaterThanOrEqualTo(0L);
                        assertThat(top).isNotNull();
                        Thread.yield();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // 16 writers
        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            executor.submit(() -> {
                try {
                    startLatch.await();
                    for (int i = 0; i < packetsPerThread; i++) {
                        String packetType = "Type_" + (threadId % 8);
                        long inLen = (threadId + 1) * 16L;
                        long outLen = (threadId + 1) * 8L;
                        tracker.recordInbound(packetType, inLen);
                        tracker.recordOutbound(packetType, outLen);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        boolean finished = doneLatch.await(20, TimeUnit.SECONDS);
        running.set(false);
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        assertThat(finished).as("16 worker threads finished cleanly").isTrue();
        assertThat(tracker.getTotalInboundPackets()).isEqualTo(100_000L);
        assertThat(tracker.getTotalOutboundPackets()).isEqualTo(100_000L);

        // Validate exact arithmetic bytes
        long expectedInboundBytes = 0L;
        long expectedOutboundBytes = 0L;
        for (int t = 0; t < threadCount; t++) {
            expectedInboundBytes += (long) packetsPerThread * ((t + 1) * 16L);
            expectedOutboundBytes += (long) packetsPerThread * ((t + 1) * 8L);
        }
        assertThat(tracker.getTotalInboundBytes()).isEqualTo(expectedInboundBytes);
        assertThat(tracker.getTotalOutboundBytes()).isEqualTo(expectedOutboundBytes);
        assertThat(tracker.getInboundStats()).hasSize(8);
    }

    @Test
    @DisplayName("PacketTelemetryTracker: Top-N ranking stability under identical frequencies and edge limits")
    void testPacketTelemetryTrackerTopNStability() {
        PacketTelemetryTracker tracker = new PacketTelemetryTracker();

        // 50 packet types each recorded exactly 100 times
        for (int i = 0; i < 50; i++) {
            String type = "Packet_" + i;
            for (int j = 0; j < 100; j++) {
                tracker.recordInbound(type, 128L);
            }
        }

        List<Map.Entry<String, PacketTelemetryTracker.PacketTypeStats>> top10 =
                tracker.getTopInboundPacketsByCount(10);
        assertThat(top10).hasSize(10);
        Set<String> uniqueKeys = new HashSet<>();
        for (Map.Entry<String, PacketTelemetryTracker.PacketTypeStats> entry : top10) {
            assertThat(entry.getValue().getCount()).isEqualTo(100L);
            assertThat(uniqueKeys.add(entry.getKey())).as("Packet type must be unique").isTrue();
        }

        // Limit larger than total types
        List<Map.Entry<String, PacketTelemetryTracker.PacketTypeStats>> top100 =
                tracker.getTopInboundPacketsByCount(100);
        assertThat(top100).hasSize(50);

        // Edge case limits: 0 and negative
        assertThat(tracker.getTopInboundPacketsByCount(0)).hasSize(1);
        assertThat(tracker.getTopInboundPacketsByCount(-5)).hasSize(1);
    }

    // =========================================================================
    // 4. BandwidthAccumulator Stress
    // =========================================================================

    @Test
    @DisplayName("BandwidthAccumulator: Large-scale data accumulation (>10 GB) preserves 64-bit integer integrity")
    void testBandwidthAccumulatorLargeScale64BitIntegrity() {
        double budgetLimit = 20.0 * 1024.0 * 1024.0 * 1024.0; // 20 GB
        BandwidthAccumulator acc = new BandwidthAccumulator(budgetLimit);

        // 15 GB inbound wire bytes (15 * 1,073,741,824 = 16,106,127,360 bytes)
        // Exceeds 32-bit signed Integer.MAX_VALUE (2,147,483,647)
        long oneGb = 1024L * 1024L * 1024L;
        for (int i = 0; i < 15; i++) {
            acc.recordInbound(oneGb, oneGb * 2);
        }

        // 12 GB outbound wire bytes
        for (int i = 0; i < 12; i++) {
            acc.recordOutbound(oneGb, oneGb);
        }

        long expectedInbound = 15L * oneGb;
        long expectedOutbound = 12L * oneGb;

        assertThat(acc.getTotalInboundBytes()).isEqualTo(expectedInbound);
        assertThat(acc.getTotalOutboundBytes()).isEqualTo(expectedOutbound);
        assertThat(acc.getTotalInboundBytes()).isGreaterThan(Integer.MAX_VALUE);
        assertThat(acc.getTotalOutboundBytes()).isGreaterThan(Integer.MAX_VALUE);

        // Total 27 GB consumed out of 20 GB budget -> 135%
        assertThat(acc.isBudgetExceeded()).isTrue();
        assertThat(acc.getBudgetConsumedPercent()).isCloseTo(135.0, offset(0.01));
    }

    @Test
    @DisplayName("BandwidthAccumulator: Extreme compression ratio (>100x), zero bytes, and budget triggers")
    void testBandwidthAccumulatorExtremeRatiosAndBudgetTriggers() {
        BandwidthAccumulator acc = new BandwidthAccumulator(50.0 * 1024.0 * 1024.0); // 50 MB budget

        // 1000 wire bytes vs 150,000 uncompressed bytes (150x compression ratio, 99.333% savings)
        acc.recordInbound(1000L, 150_000L);
        assertThat(acc.getInboundCompressionRatio()).isCloseTo(150.0, offset(0.01));
        assertThat(acc.getInboundSavingsPercent()).isCloseTo(99.3333, offset(0.01));

        // Zero-byte packets handled gracefully
        acc.recordInbound(0L, 0L);
        acc.recordOutbound(0L, 0L);
        assertThat(acc.getTotalCompressionRatio()).isGreaterThan(1.0);

        // Budget triggers
        acc.reset();
        assertThat(acc.isBudgetExceeded()).isFalse();
        assertThat(acc.getBudgetConsumedPercent()).isEqualTo(0.0);

        long fiveMb = 5L * 1024L * 1024L;
        // Feed 9 bursts of 5 MB = 45 MB (90% of 50 MB)
        for (int i = 0; i < 9; i++) {
            acc.recordInbound(fiveMb, fiveMb);
        }
        assertThat(acc.isBudgetExceeded()).isFalse();
        assertThat(acc.getBudgetConsumedPercent()).isCloseTo(90.0, offset(0.01));

        // 10th burst reaches 50 MB (100%) -> trigger!
        acc.recordInbound(fiveMb, fiveMb);
        assertThat(acc.isBudgetExceeded()).isTrue();
        assertThat(acc.getBudgetConsumedPercent()).isCloseTo(100.0, offset(0.01));

        // 11th burst reaches 55 MB (110%)
        acc.recordInbound(fiveMb, fiveMb);
        assertThat(acc.isBudgetExceeded()).isTrue();
        assertThat(acc.getBudgetConsumedPercent()).isCloseTo(110.0, offset(0.01));
    }

    // =========================================================================
    // 5. PingStatistics Stress
    // =========================================================================

    @Test
    @DisplayName("PingStatistics: RFC 3550 jitter calculation matches independent reference across 5,000 samples")
    void testPingStatisticsRfc3550JitterAcross5000Samples() {
        PingStatistics stats = new PingStatistics(100);
        int sampleCount = 5000;

        double refJitter = 0.0;
        double lastSample = -1.0;

        for (int i = 0; i < sampleCount; i++) {
            double sample = 30.0 + 20.0 * (i % 3) + 5.0 * Math.sin(i);

            if (lastSample >= 0.0) {
                double delta = Math.abs(sample - lastSample);
                refJitter += (delta - refJitter) / 16.0;
            }
            lastSample = sample;

            stats.recordSample(sample);
        }

        assertThat(stats.getRfc3550Jitter()).isCloseTo(refJitter, offset(0.0001));
        assertThat(stats.getRfc3550Jitter()).isGreaterThan(0.0);
    }

    @Test
    @DisplayName("PingStatistics: Median accuracy verified against sorted references across 2,000 sliding samples")
    void testPingStatisticsMedianAccuracy() {
        PingStatistics stats = new PingStatistics(100);

        for (int i = 0; i < 2000; i++) {
            double latency = 15.0 + ((i * 17) % 180) + (i % 7) * 0.25;
            stats.recordSample(latency);

            // Periodically check median against independently sorted valid samples
            if (i % 50 == 0 || i < 15) {
                double[] valid = stats.getValidSamples();
                Arrays.sort(valid);
                double refMedian;
                int n = valid.length;
                if (n % 2 == 1) {
                    refMedian = valid[n / 2];
                } else {
                    refMedian = (valid[(n / 2) - 1] + valid[n / 2]) / 2.0;
                }
                assertThat(stats.getMedian()).isCloseTo(refMedian, offset(0.0001));
            }
        }
    }

    @Test
    @DisplayName("PingStatistics: Stability index clamped to [0, 100] under extreme and adversarial variance")
    void testPingStatisticsStabilityIndexClamping() {
        // Zero variance case: exactly 100%
        PingStatistics stableStats = new PingStatistics(20);
        for (int i = 0; i < 20; i++) {
            stableStats.recordSample(42.0);
        }
        assertThat(stableStats.getStabilityIndex()).isEqualTo(100.0);

        // Extreme variance case: CV >> 1.0 (mean = ~500.95, SD = ~2179.23 -> CV ≈ 4.35)
        PingStatistics erraticStats = new PingStatistics(20);
        for (int i = 0; i < 19; i++) {
            erraticStats.recordSample(1.0);
        }
        erraticStats.recordSample(10_000.0);
        // CV is ~4.35 > 1.0 -> 100 * (1 - CV) is strongly negative (-335.0)
        // Stability index must be clamped to 0.0
        assertThat(erraticStats.getStabilityIndex()).isEqualTo(0.0);

        // Moderate variance case: CV = 0.2 -> score = 80.0%
        PingStatistics modStats = new PingStatistics(2);
        modStats.recordSample(40.0);
        modStats.recordSample(60.0);
        // Mean = 50.0, SD = 10.0, CV = 0.2, Score = 100 * (1 - 0.2) = 80.0
        assertThat(modStats.getStabilityIndex()).isCloseTo(80.0, offset(0.001));

        // Invariant: score is always in [0.0, 100.0]
        assertThat(stableStats.getStabilityIndex()).isBetween(0.0, 100.0);
        assertThat(erraticStats.getStabilityIndex()).isBetween(0.0, 100.0);
        assertThat(modStats.getStabilityIndex()).isBetween(0.0, 100.0);
    }

    // =========================================================================
    // 6. Parameter Boundary Fuzzing
    // =========================================================================

    @Test
    @DisplayName("Parameter Boundary Fuzzing: Fuzz all settings of all 50 modules with NaN, Inf, null, corrupted data")
    void testParameterBoundaryFuzzingAll50Modules() {
        List<Module> allModules = TelemetryModuleCatalog.createAllInstances();
        assertThat(allModules).hasSize(50);

        Double[] extremeNumbers = new Double[]{
                Double.NaN,
                Double.POSITIVE_INFINITY,
                Double.NEGATIVE_INFINITY,
                Double.MAX_VALUE,
                -Double.MAX_VALUE,
                Double.MIN_VALUE,
                -Double.MIN_VALUE,
                0.0,
                -0.0,
                null
        };

        String[] extremeStrings = new String[]{
                null,
                "",
                "   ",
                "CORRUPTED_NON_EXISTENT_MODE_XYZ",
                "\u0000\uFFFF\uD83D\uDE00",
                "A".repeat(1000)
        };

        Integer[] extremeInts = new Integer[]{
                null,
                Integer.MAX_VALUE,
                Integer.MIN_VALUE,
                0,
                -1,
                -999
        };

        JsonElement[] corruptedJson = new JsonElement[]{
                JsonNull.INSTANCE,
                new JsonPrimitive("not_a_valid_number"),
                new JsonPrimitive(""),
                new JsonPrimitive(Double.MAX_VALUE),
                new JsonObject(),
                new JsonArray(),
                new JsonPrimitive(true),
                new JsonPrimitive(false)
        };

        for (Module module : allModules) {
            assertThat(module).isNotNull();
            assertThat(module.getId()).isNotEmpty();
            assertThat(module.getName()).isNotEmpty();

            for (Setting<?> setting : module.getSettings()) {
                assertThat(setting).isNotNull();

                if (setting instanceof NumberSetting numSetting) {
                    for (Double val : extremeNumbers) {
                        numSetting.setValue(val);
                        assertThat(numSetting.getValue()).as("Value must remain non-null").isNotNull();
                        assertThat(numSetting.getValue()).isGreaterThanOrEqualTo(numSetting.getMin());
                        assertThat(numSetting.getValue()).isLessThanOrEqualTo(numSetting.getMax());
                    }
                    for (JsonElement json : corruptedJson) {
                        numSetting.fromJson(json);
                        assertThat(numSetting.getValue()).isNotNull();
                    }
                    numSetting.reset();
                } else if (setting instanceof ModeSetting modeSetting) {
                    for (String str : extremeStrings) {
                        modeSetting.setValue(str);
                        assertThat(modeSetting.getValue()).as("Mode value must be non-null and valid").isNotNull();
                        assertThat(modeSetting.getModes()).contains(modeSetting.getValue());
                    }
                    modeSetting.cycle();
                    for (JsonElement json : corruptedJson) {
                        modeSetting.fromJson(json);
                        assertThat(modeSetting.getValue()).isNotNull();
                    }
                    modeSetting.reset();
                } else if (setting instanceof BooleanSetting boolSetting) {
                    boolSetting.setValue(null);
                    assertThat(boolSetting.getValue()).isNotNull();
                    boolSetting.setValue(true);
                    assertThat(boolSetting.getValue()).isTrue();
                    boolSetting.toggle();
                    assertThat(boolSetting.getValue()).isFalse();
                    for (JsonElement json : corruptedJson) {
                        boolSetting.fromJson(json);
                        assertThat(boolSetting.getValue()).isNotNull();
                    }
                    boolSetting.reset();
                } else if (setting instanceof KeybindSetting kbSetting) {
                    for (Integer val : extremeInts) {
                        kbSetting.setValue(val);
                    }
                    for (JsonElement json : corruptedJson) {
                        kbSetting.fromJson(json);
                    }
                    kbSetting.reset();
                } else if (setting instanceof ColorSetting colorSetting) {
                    for (Integer val : extremeInts) {
                        colorSetting.setValue(val);
                    }
                    for (JsonElement json : corruptedJson) {
                        colorSetting.fromJson(json);
                    }
                    colorSetting.reset();
                }
            }

            // Lifecycle toggling and ticks
            module.setEnabled(true);
            module.onTick();
            module.setEnabled(false);
            module.toggle();
            module.toggle();
        }
    }
}
