package com.clientmod.core.telemetry;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TelemetryMathTest {

    @Test
    void testFpsCalculationWithKnownDurations() {
        TelemetryTracker tracker = new TelemetryTracker();

        // 60 FPS = 16,666,666 ns per frame
        long frameDuration60Fps = 16_666_666L;
        for (int i = 0; i < 60; i++) {
            tracker.recordFrame(frameDuration60Fps);
        }

        double fps = tracker.getAverageFps();
        assertThat(fps).isBetween(59.0, 61.0);
    }

    @Test
    void testOnePercentLowDetection() {
        TelemetryTracker tracker = new TelemetryTracker();

        // 99 normal frames (60 FPS = 16.6ms)
        for (int i = 0; i < 99; i++) {
            tracker.recordFrame(16_666_666L);
        }
        // 1 heavy stutter frame (100ms = 10 FPS)
        tracker.recordFrame(100_000_000L);

        double avgFps = tracker.getAverageFps();
        double lowFps = tracker.getOnePercentLowFps();

        assertThat(avgFps).isGreaterThan(55.0);
        assertThat(lowFps).isLessThan(15.0);
    }

    @Test
    void testTpsEstimationWithTimePackets() {
        TelemetryTracker tracker = new TelemetryTracker();

        // Simulate 20 ticks in 1000ms
        tracker.recordTimePacket(1000L, 10_000L);
        tracker.recordTimePacket(1020L, 11_000L);

        double tps = tracker.getEstimatedTps();
        assertThat(tps).isBetween(19.0, 20.0);
    }
}
