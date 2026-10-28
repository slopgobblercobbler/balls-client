package com.ballsclient.core.module.telemetry.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;

@DisplayName("PingStatistics Domain Model Test")
class PingStatisticsTest {

    @Test
    @DisplayName("Basic Statistics: Computes Min, Max, Mean, and Median accurately")
    void testBasicStatistics() {
        PingStatistics stats = new PingStatistics(10);
        double[] latencies = {20.0, 30.0, 40.0, 50.0, 60.0};
        for (double lat : latencies) {
            stats.recordSample(lat);
        }

        assertThat(stats.getSize()).isEqualTo(5);
        assertThat(stats.getMin()).isEqualTo(20.0);
        assertThat(stats.getMax()).isEqualTo(60.0);
        assertThat(stats.getMean()).isEqualTo(40.0);
        assertThat(stats.getMedian()).isEqualTo(40.0);
    }

    @Test
    @DisplayName("RFC 3550 Jitter: Alternating latency sequence smoothly converges jitter metric")
    void testRfc3550Jitter() {
        PingStatistics stats = new PingStatistics(20);
        // Step delta between consecutive samples is 20.0ms
        double[] latencies = {30.0, 50.0, 30.0, 50.0, 30.0, 50.0, 30.0, 50.0};
        for (double lat : latencies) {
            stats.recordSample(lat);
        }

        // Jitter starts at 0 and approaches 20.0 according to J = J + (20 - J)/16
        double jitter = stats.getRfc3550Jitter();
        assertThat(jitter).isGreaterThan(5.0);
        assertThat(jitter).isLessThanOrEqualTo(20.0);

        // MACD jitter on this alternating sequence should be exactly 20.0
        assertThat(stats.getMeanAbsoluteConsecutiveDifference()).isCloseTo(20.0, offset(0.001));
    }

    @Test
    @DisplayName("Quantiles: P50, P90, P95, and P99 extraction on linear range [1..100]")
    void testQuantiles() {
        PingStatistics stats = new PingStatistics(100);
        for (int i = 1; i <= 100; i++) {
            stats.recordSample(i);
        }

        assertThat(stats.getSize()).isEqualTo(100);
        assertThat(stats.getP50()).isCloseTo(50.0, offset(1.0));
        assertThat(stats.getP90()).isCloseTo(90.0, offset(1.0));
        assertThat(stats.getP95()).isCloseTo(95.0, offset(1.0));
        assertThat(stats.getP99()).isCloseTo(99.0, offset(1.0));
    }

    @Test
    @DisplayName("Connection Stability Index: 100% on zero variance, drops sharply on erratic ping")
    void testStabilityIndex() {
        // Zero variance case: identical latencies
        PingStatistics stableStats = new PingStatistics(10);
        stableStats.recordSample(35.0);
        stableStats.recordSample(35.0);
        stableStats.recordSample(35.0);
        assertThat(stableStats.getStandardDeviation()).isCloseTo(0.0, offset(0.001));
        assertThat(stableStats.getStabilityIndex()).isEqualTo(100.0);

        // Erratic latency case: extreme swings
        PingStatistics erraticStats = new PingStatistics(10);
        erraticStats.recordSample(10.0);
        erraticStats.recordSample(200.0);
        erraticStats.recordSample(15.0);
        erraticStats.recordSample(300.0);
        assertThat(erraticStats.getStabilityIndex()).isLessThan(40.0);
    }

    @Test
    @DisplayName("Edge Cases: Empty and single-sample buffers operate safely")
    void testEdgeCases() {
        PingStatistics stats = new PingStatistics(5);
        assertThat(stats.getSize()).isEqualTo(0);
        assertThat(stats.getMean()).isEqualTo(0.0);
        assertThat(stats.getMin()).isEqualTo(0.0);
        assertThat(stats.getMax()).isEqualTo(0.0);
        assertThat(stats.getMedian()).isEqualTo(0.0);
        assertThat(stats.getP50()).isEqualTo(0.0);
        assertThat(stats.getStandardDeviation()).isEqualTo(0.0);
        assertThat(stats.getStabilityIndex()).isEqualTo(100.0);
        assertThat(stats.getMeanAbsoluteConsecutiveDifference()).isEqualTo(0.0);

        // Single sample
        stats.recordSample(45.0);
        assertThat(stats.getSize()).isEqualTo(1);
        assertThat(stats.getMean()).isEqualTo(45.0);
        assertThat(stats.getMedian()).isEqualTo(45.0);
        assertThat(stats.getStandardDeviation()).isEqualTo(0.0);
        assertThat(stats.getStabilityIndex()).isEqualTo(100.0);

        // Negative sample ignored
        stats.recordSample(-10.0);
        assertThat(stats.getSize()).isEqualTo(1);

        stats.reset();
        assertThat(stats.getSize()).isEqualTo(0);
        assertThat(stats.getRfc3550Jitter()).isEqualTo(0.0);

        assertThatThrownBy(() -> new PingStatistics(0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
