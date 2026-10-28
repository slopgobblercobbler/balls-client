package com.ballsclient.core.module.telemetry.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;

@DisplayName("FrametimeRingBuffer Domain Model Test")
class FrametimeRingBufferTest {

    @Test
    @DisplayName("O(1) Wrap-Around: Circular buffer overwrites oldest entries and maintains running sum")
    void testWrapAroundAndRunningSum() {
        FrametimeRingBuffer buffer = new FrametimeRingBuffer(5);

        // Insert 10 elements: 1L, 2L, ..., 10L
        for (long i = 1L; i <= 10L; i++) {
            buffer.recordFrame(i);
        }

        assertThat(buffer.getSize()).isEqualTo(5);
        // Valid samples should be the last 5: 6L, 7L, 8L, 9L, 10L
        assertThat(buffer.getValidSamples()).containsExactly(6L, 7L, 8L, 9L, 10L);
        // Running sum: 6 + 7 + 8 + 9 + 10 = 40
        assertThat(buffer.getRunningSumNanos()).isEqualTo(40L);
        assertThat(buffer.getAverageFrametimeNanos()).isEqualTo(8.0);
    }

    @Test
    @DisplayName("Rolling Average FPS: Uniform 60 FPS frame times yield 60.0 FPS")
    void testRollingAverageFps() {
        FrametimeRingBuffer buffer = new FrametimeRingBuffer(60);
        long targetFrameNanos = 16_666_666L; // ~60 FPS

        for (int i = 0; i < 60; i++) {
            buffer.recordFrame(targetFrameNanos);
        }

        assertThat(buffer.getSize()).isEqualTo(60);
        assertThat(buffer.getAverageFps()).isCloseTo(60.0, offset(0.01));
        assertThat(buffer.getStandardDeviationNanos()).isCloseTo(0.0, offset(0.001));
    }

    @Test
    @DisplayName("1% Low Calculation: Accurately isolates worst 1% frame times")
    void testOnePercentLowQuantile() {
        FrametimeRingBuffer buffer = new FrametimeRingBuffer(100);
        long normalFrameNanos = 16_666_667L; // ~60 FPS
        long stutterFrameNanos = 100_000_000L; // 100ms stutter = 10 FPS

        // 99 normal frames + 1 stutter frame
        for (int i = 0; i < 99; i++) {
            buffer.recordFrame(normalFrameNanos);
        }
        buffer.recordFrame(stutterFrameNanos);

        assertThat(buffer.getSize()).isEqualTo(100);
        // 1% of 100 = 1 frame (the 100ms stutter)
        // 100ms -> 10.0 FPS
        assertThat(buffer.getOnePercentLowFps()).isCloseTo(10.0, offset(0.01));
        // Average FPS should still be around ~57 FPS
        assertThat(buffer.getAverageFps()).isGreaterThan(50.0);
    }

    @Test
    @DisplayName("0.1% Low Calculation: Accurately isolates worst 0.1% across 1000 frames")
    void testPointOnePercentLowQuantile() {
        FrametimeRingBuffer buffer = new FrametimeRingBuffer(1000);
        long normalFrameNanos = 16_666_667L; // ~60 FPS
        long massiveStutterNanos = 200_000_000L; // 200ms stutter = 5 FPS

        for (int i = 0; i < 999; i++) {
            buffer.recordFrame(normalFrameNanos);
        }
        buffer.recordFrame(massiveStutterNanos);

        assertThat(buffer.getSize()).isEqualTo(1000);
        // 0.1% of 1000 = 1 frame
        assertThat(buffer.getPointOnePercentLowFps()).isCloseTo(5.0, offset(0.01));
    }

    @Test
    @DisplayName("Standard Deviation: Accurately computes population variance")
    void testStandardDeviation() {
        FrametimeRingBuffer buffer = new FrametimeRingBuffer(4);
        buffer.recordFrame(10L);
        buffer.recordFrame(20L);
        buffer.recordFrame(30L);
        buffer.recordFrame(40L);

        // Mean = 25.0
        // Variance = ((10-25)^2 + (20-25)^2 + (30-25)^2 + (40-25)^2) / 4 = (225 + 25 + 25 + 225) / 4 = 125.0
        // SD = sqrt(125.0) ≈ 11.1803
        assertThat(buffer.getStandardDeviationNanos()).isCloseTo(11.1803, offset(0.001));
    }

    @Test
    @DisplayName("Boundary & Empty Handling: Zero/negative inputs ignored and empty buffer safe")
    void testBoundaryAndEmpty() {
        FrametimeRingBuffer buffer = new FrametimeRingBuffer(10);
        assertThat(buffer.getSize()).isEqualTo(0);
        assertThat(buffer.getAverageFps()).isEqualTo(0.0);
        assertThat(buffer.getOnePercentLowFps()).isEqualTo(0.0);
        assertThat(buffer.getPointOnePercentLowFps()).isEqualTo(0.0);
        assertThat(buffer.getStandardDeviationNanos()).isEqualTo(0.0);
        assertThat(buffer.getValidSamples()).isEmpty();

        // Non-positive inputs should be completely ignored
        buffer.recordFrame(0L);
        buffer.recordFrame(-100L);
        assertThat(buffer.getSize()).isEqualTo(0);

        buffer.recordFrame(16_000_000L);
        assertThat(buffer.getSize()).isEqualTo(1);
        buffer.clear();
        assertThat(buffer.getSize()).isEqualTo(0);
        assertThat(buffer.getRunningSumNanos()).isEqualTo(0L);

        assertThatThrownBy(() -> new FrametimeRingBuffer(0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
