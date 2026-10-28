package com.ballsclient.core.module.telemetry.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

@DisplayName("BandwidthAccumulator Domain Model Test")
class BandwidthAccumulatorTest {

    @Test
    @DisplayName("Throughput & Peak: Records wire throughput and tracks peak rates")
    void testThroughputAndPeak() {
        BandwidthAccumulator acc = new BandwidthAccumulator(1024.0 * 1024.0); // 1 MB budget
        long baseTime = 1_000_000L;

        // Record 100 KB over 1 second (10 KB per 100ms)
        for (int i = 0; i < 10; i++) {
            acc.recordInbound(10 * 1024L, 20 * 1024L, baseTime + (i * 100L));
            acc.recordOutbound(5 * 1024L, 5 * 1024L, baseTime + (i * 100L));
        }

        assertThat(acc.getTotalInboundBytes()).isEqualTo(100 * 1024L);
        assertThat(acc.getTotalOutboundBytes()).isEqualTo(50 * 1024L);
        assertThat(acc.getPeakInboundThroughputKbps()).isGreaterThan(50.0);
        assertThat(acc.getPeakOutboundThroughputKbps()).isGreaterThan(20.0);
    }

    @Test
    @DisplayName("Compression Ratio: Accurately calculates wire savings and expansion ratios")
    void testCompressionRatios() {
        BandwidthAccumulator acc = new BandwidthAccumulator(1000L);

        // 1000 uncompressed bytes compressed into 250 wire bytes -> 4.0x ratio, 75% savings
        acc.recordInbound(250L, 1000L);
        acc.recordOutbound(250L, 1000L);

        assertThat(acc.getInboundCompressionRatio()).isCloseTo(4.0, offset(0.001));
        assertThat(acc.getOutboundCompressionRatio()).isCloseTo(4.0, offset(0.001));
        assertThat(acc.getTotalCompressionRatio()).isCloseTo(4.0, offset(0.001));

        assertThat(acc.getInboundSavingsPercent()).isCloseTo(75.0, offset(0.001));
        assertThat(acc.getOutboundSavingsPercent()).isCloseTo(75.0, offset(0.001));
        assertThat(acc.getTotalSavingsPercent()).isCloseTo(75.0, offset(0.001));
    }

    @Test
    @DisplayName("Budget Quotas: Computes consumed percentage and alert triggers")
    void testBudgetQuotas() {
        double budget = 1000.0;
        BandwidthAccumulator acc = new BandwidthAccumulator(budget);

        acc.recordInbound(400L, 400L);
        acc.recordOutbound(300L, 300L);

        // 700 / 1000 = 70%
        assertThat(acc.getBudgetConsumedPercent()).isCloseTo(70.0, offset(0.01));
        assertThat(acc.isBudgetExceeded()).isFalse();

        // Add 400 bytes -> 1100 / 1000 = 110%
        acc.recordInbound(400L, 400L);
        assertThat(acc.getBudgetConsumedPercent()).isCloseTo(110.0, offset(0.01));
        assertThat(acc.isBudgetExceeded()).isTrue();

        acc.reset();
        assertThat(acc.getTotalInboundBytes()).isEqualTo(0L);
        assertThat(acc.getTotalOutboundBytes()).isEqualTo(0L);
        assertThat(acc.getBudgetConsumedPercent()).isEqualTo(0.0);
        assertThat(acc.isBudgetExceeded()).isFalse();
    }
}
