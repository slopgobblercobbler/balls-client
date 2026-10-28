package com.ballsclient.core.module.combat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DisplayName("Projectile Ballistic Trajectory & Lead-Aim Math Test")
class ProjectileTrajectoryMathTest {

    @Test
    @DisplayName("Ballistics: Arrow parabolic trajectory simulation with gravity (0.05) and drag (0.99)")
    void testArrowTrajectorySimulation() {
        double v0 = 3.0; // Fully drawn bow initial velocity (blocks/tick)
        double pitchDeg = -45.0; // Launch upward at 45 degrees
        double yawDeg = 0.0;
        double gravity = 0.05;
        double drag = 0.99;

        double pitchRad = Math.toRadians(pitchDeg);
        double yawRad = Math.toRadians(yawDeg);

        double vx = -Math.sin(yawRad) * Math.cos(pitchRad) * v0;
        double vy = -Math.sin(pitchRad) * v0;
        double vz = Math.cos(yawRad) * Math.cos(pitchRad) * v0;

        double x = 0.0, y = 1.62, z = 0.0; // Eye level start
        double maxY = y;

        // Simulate 40 ticks
        for (int t = 0; t < 40; t++) {
            x += vx;
            y += vy;
            z += vz;
            if (y > maxY) maxY = y;

            vx *= drag;
            vy = (vy - gravity) * drag;
            vz *= drag;
        }

        // Apex should exceed 20 blocks elevation
        assertThat(maxY).isGreaterThan(20.0);
        // Arrow horizontal velocity decelerates continuously due to 0.99 drag
        assertThat(vz).isLessThan(v0 * Math.cos(pitchRad));
        assertThat(x).isCloseTo(0.0, within(1e-5));
    }

    @Test
    @DisplayName("Ballistics: Snowball/Pearl gravity (0.03) vs Arrow gravity (0.05) drop comparison")
    void testSnowballVsArrowGravityDrop() {
        double v0 = 1.5;
        double drag = 0.99;
        double arrowGrav = 0.05;
        double snowballGrav = 0.03;

        double yArrow = 100.0, vyArrow = 0.0;
        double ySnowball = 100.0, vySnowball = 0.0;

        for (int t = 0; t < 20; t++) {
            yArrow += vyArrow;
            ySnowball += vySnowball;

            vyArrow = (vyArrow - arrowGrav) * drag;
            vySnowball = (vySnowball - snowballGrav) * drag;
        }

        double arrowDrop = 100.0 - yArrow;
        double snowballDrop = 100.0 - ySnowball;

        assertThat(arrowDrop).isGreaterThan(snowballDrop);
        assertThat(arrowDrop / snowballDrop).isCloseTo(arrowGrav / snowballGrav, within(0.15));
    }

    @Test
    @DisplayName("Lead-Aim: Moving target future position extrapolation and flight time intercept")
    void testMovingTargetLeadIntercept() {
        double targetStartX = 20.0, targetStartY = 0.0, targetStartZ = 0.0;
        double targetVx = 0.0, targetVy = 0.0, targetVz = 0.25; // Target sprinting north
        double arrowSpeed = 3.0; // Blocks per tick

        double dx = targetStartX - 0.0;
        double dz = targetStartZ - 0.0;
        double dist = Math.sqrt(dx * dx + dz * dz);
        double estTicks = dist / arrowSpeed;

        double futureTargetZ = targetStartZ + targetVz * estTicks;
        assertThat(futureTargetZ).isCloseTo(1.666, within(0.05));

        // Intercept angle yaw computation
        double interceptYaw = Math.toDegrees(Math.atan2(-targetStartX, futureTargetZ));
        assertThat(interceptYaw).isLessThan(-80.0).isGreaterThan(-95.0);
    }

    @Test
    @DisplayName("Ballistics: Optimal launch pitch angle under air drag (0.99) is slightly below 45 degrees")
    void testOptimalRangeAngleUnderDrag() {
        double v0 = 3.0;
        double gravity = 0.05;
        double drag = 0.99;

        double maxHorizontalDist = 0.0;
        double bestPitchDeg = 0.0;

        for (double pitch = 25.0; pitch <= 55.0; pitch += 1.0) {
            double pitchRad = Math.toRadians(-pitch);
            double vy = -Math.sin(pitchRad) * v0;
            double vz = Math.cos(pitchRad) * v0;
            double y = 0.0, z = 0.0;

            for (int t = 0; t < 200; t++) {
                y += vy;
                z += vz;
                vy = (vy - gravity) * drag;
                vz *= drag;
                if (y < 0.0) break;
            }

            if (z > maxHorizontalDist) {
                maxHorizontalDist = z;
                bestPitchDeg = pitch;
            }
        }

        // Under 0.99 air resistance, optimal ballistic angle shifts from 45.0 to ~38-42 degrees
        assertThat(bestPitchDeg).isGreaterThanOrEqualTo(37.0).isLessThanOrEqualTo(43.0);
    }
}
