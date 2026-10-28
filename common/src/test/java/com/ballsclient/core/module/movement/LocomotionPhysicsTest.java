package com.ballsclient.core.module.movement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DisplayName("Locomotion Kinematic Physics Test")
class LocomotionPhysicsTest {

    @Test
    @DisplayName("Kinematics: Jump impulse, gravity decay, and peak elevation calculation")
    void testJumpKinematics() {
        double vanillaImpulse = 0.42;
        double highJumpMultiplier = 1.8;
        double boostedImpulse = vanillaImpulse * highJumpMultiplier;

        // Simulate 10 ticks of vertical trajectory
        double yVanilla = 0.0, vyVanilla = vanillaImpulse;
        double yBoosted = 0.0, vyBoosted = boostedImpulse;

        for (int t = 0; t < 10; t++) {
            if (vyVanilla > 0) yVanilla += vyVanilla;
            if (vyBoosted > 0) yBoosted += vyBoosted;

            vyVanilla = (vyVanilla - 0.08) * 0.98;
            vyBoosted = (vyBoosted - 0.08) * 0.98;
        }

        // Vanilla jump clears 1 block (peak ~1.25m), boosted clears 3 blocks
        assertThat(yVanilla).isCloseTo(1.25, within(0.05));
        assertThat(yBoosted).isGreaterThan(2.5).isLessThan(4.0);
    }

    @Test
    @DisplayName("Friction: Terminal velocity scaling across normal ground vs blue ice")
    void testFrictionTerminalVelocity() {
        double a = 0.1; // input acceleration per tick
        double fNormal = 0.6 * 0.91;
        double fBlueIce = 0.989 * 0.91;

        double vTermNormal = a / (1.0 - fNormal);
        double vTermIce = a / (1.0 - fBlueIce);

        assertThat(vTermNormal).isCloseTo(0.22, within(0.01));
        assertThat(vTermIce).isCloseTo(1.00, within(0.02));
        assertThat(vTermIce / vTermNormal).isGreaterThan(4.0);
    }

    @Test
    @DisplayName("FastLadder: Ascend displacement comparison over 20 ticks")
    void testFastLadderDisplacement() {
        double vanillaSpeed = 0.15;
        double fastLadderSpeed = 0.2872;

        double vanillaDist = vanillaSpeed * 20.0;
        double fastDist = fastLadderSpeed * 20.0;

        assertThat(fastDist).isCloseTo(5.744, within(0.001));
        assertThat(fastDist / vanillaDist).isCloseTo(1.914, within(0.01));
    }

    @Test
    @DisplayName("SafeWalk: Coordinate clamping at block edge threshold")
    void testSafeWalkClamping() {
        double currentX = 0.90;
        double deltaX = 0.20;
        double blockMaxX = 1.0;
        double edgeOffset = 0.05;

        double unclampedX = currentX + deltaX;
        double clampedX = Math.min(blockMaxX - edgeOffset, unclampedX);

        assertThat(unclampedX).isEqualTo(1.10);
        assertThat(clampedX).isEqualTo(0.95);
    }
}
