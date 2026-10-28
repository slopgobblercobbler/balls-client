package com.ballsclient.core.module.telemetry.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;

@DisplayName("EmaTpsCalculator Domain Model Test")
class EmaTpsCalculatorTest {

    @Test
    @DisplayName("Steady 20 TPS: Estimated TPS remains at 20.0 over uniform tick updates")
    void testSteadyTpsConvergence() {
        EmaTpsCalculator calc = new EmaTpsCalculator(9, 2000L);
        long worldTime = 1000L;
        long timeMillis = 100_000L;

        // Initial reference packet
        calc.recordTimePacket(worldTime, timeMillis);
        assertThat(calc.getTps(timeMillis)).isEqualTo(20.0);

        // 10 consecutive 1-second intervals advancing exactly 20 ticks
        for (int i = 0; i < 10; i++) {
            worldTime += 20L;
            timeMillis += 1000L;
            calc.recordTimePacket(worldTime, timeMillis);
            assertThat(calc.getInstantaneousTps()).isEqualTo(20.0);
            assertThat(calc.getTps(timeMillis)).isCloseTo(20.0, offset(0.001));
        }

        assertThat(calc.getCumulativeTickDrift()).isEqualTo(0L);
        assertThat(calc.isFrozen(timeMillis)).isFalse();
        assertThat(calc.getTotalTimePacketsReceived()).isEqualTo(11L);
    }

    @Test
    @DisplayName("Degraded 10 TPS: EMA smoothly converges towards 10.0")
    void testDegradedTpsSmoothing() {
        EmaTpsCalculator calc = new EmaTpsCalculator(9, 2000L); // alpha = 2 / (9 + 1) = 0.2
        long worldTime = 1000L;
        long timeMillis = 100_000L;

        calc.recordTimePacket(worldTime, timeMillis);

        // Advance at 10 TPS (10 ticks per 1000ms) for 20 intervals
        for (int i = 0; i < 20; i++) {
            worldTime += 10L;
            timeMillis += 1000L;
            calc.recordTimePacket(worldTime, timeMillis);
            assertThat(calc.getInstantaneousTps()).isEqualTo(10.0);
        }

        // With alpha=0.2 after 20 steps: (1 - 0.2)^20 = 0.0115, so TPS should be within 0.15 of 10.0
        assertThat(calc.getTps(timeMillis)).isCloseTo(10.0, offset(0.2));
        assertThat(calc.getCumulativeTickDrift()).isGreaterThan(0L);
    }

    @Test
    @DisplayName("Zero Tick Progression: Tick stall pulls TPS downward")
    void testZeroTickProgression() {
        EmaTpsCalculator calc = new EmaTpsCalculator(9, 3000L);
        long worldTime = 5000L;
        long timeMillis = 200_000L;

        calc.recordTimePacket(worldTime, timeMillis);

        // 1000ms elapse with 0 ticks advancing
        timeMillis += 1000L;
        calc.recordTimePacket(worldTime, timeMillis);

        assertThat(calc.getInstantaneousTps()).isEqualTo(0.0);
        // Initial was 20.0, after alpha=0.2 with instant=0: 0.2*0 + 0.8*20 = 16.0
        assertThat(calc.getTps(timeMillis)).isCloseTo(16.0, offset(0.01));
    }

    @Test
    @DisplayName("Time Rewind Discontinuity: Resets anchors cleanly without negative TPS or NaN")
    void testTimeRewindDiscontinuity() {
        EmaTpsCalculator calc = new EmaTpsCalculator(9, 2000L);
        long worldTime = 18000L;
        long timeMillis = 50_000L;

        calc.recordTimePacket(worldTime, timeMillis);

        // Advance normally 1 second
        worldTime += 20L;
        timeMillis += 1000L;
        calc.recordTimePacket(worldTime, timeMillis);
        assertThat(calc.getTps(timeMillis)).isEqualTo(20.0);

        // Command /time set 1000 executed -> worldTime decreases drastically
        worldTime = 1000L;
        timeMillis += 1000L;
        calc.recordTimePacket(worldTime, timeMillis);

        // Verify TPS remains stable at 20.0 and is not NaN/negative
        assertThat(calc.getTps(timeMillis)).isEqualTo(20.0);
        assertThat(Double.isNaN(calc.getTps(timeMillis))).isFalse();

        // Next normal packet advances from new anchor cleanly
        worldTime += 20L;
        timeMillis += 1000L;
        calc.recordTimePacket(worldTime, timeMillis);
        assertThat(calc.getTps(timeMillis)).isEqualTo(20.0);
    }

    @Test
    @DisplayName("Freeze Detection & Decay: Inactivity past threshold flags frozen and decays TPS")
    void testFreezeDetectionAndDecay() {
        EmaTpsCalculator calc = new EmaTpsCalculator(9, 2000L);
        long worldTime = 5000L;
        long timeMillis = 100_000L;

        calc.recordTimePacket(worldTime, timeMillis);

        // 1500ms later: not frozen yet
        assertThat(calc.isFrozen(timeMillis + 1500L)).isFalse();

        // 2500ms later: exceeds 2000ms threshold -> frozen
        long frozenTime = timeMillis + 2500L;
        assertThat(calc.isFrozen(frozenTime)).isTrue();

        // Decayed TPS should be strictly less than 20.0
        double decayedTps = calc.getTps(frozenTime);
        assertThat(decayedTps).isLessThan(20.0);
        assertThat(decayedTps).isGreaterThanOrEqualTo(0.0);

        // 10 seconds later: heavily decayed towards 0
        long longFrozenTime = timeMillis + 10_000L;
        assertThat(calc.getTps(longFrozenTime)).isLessThan(2.0);
    }

    @Test
    @DisplayName("Reset & Boundary Validations: Clears state and enforces constructor invariants")
    void testResetAndInvariants() {
        EmaTpsCalculator calc = new EmaTpsCalculator(9, 2000L);
        calc.recordTimePacket(100L, 1000L);
        calc.recordTimePacket(120L, 2000L);

        calc.reset();
        assertThat(calc.getTotalTimePacketsReceived()).isEqualTo(0L);
        assertThat(calc.getCumulativeTickDrift()).isEqualTo(0L);
        assertThat(calc.getTps()).isEqualTo(20.0);
        assertThat(calc.isFrozen()).isFalse();

        assertThatThrownBy(() -> new EmaTpsCalculator(0, 2000L))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new EmaTpsCalculator(9, 0L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
