package com.ballsclient.core.module.movement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DisplayName("Elytra Flight Trajectory & Aerodynamic Thrust Test")
class ElytraTrajectoryTest {

    @Test
    @DisplayName("Aerodynamics: Optimal glide pitch angle maximizes horizontal-vertical energy trade-off")
    void testOptimalPitchAngle() {
        double maxProduct = 0.0;
        double bestPitchDeg = 0.0;

        for (double deg = 10.0; deg <= 80.0; deg += 0.5) {
            double rad = Math.toRadians(deg);
            // Parasite drag factor (1 - 0.15 * sin^2(rad)) shifts theoretical 45 deg to ~40 deg
            double glideProduct = Math.cos(rad) * Math.sin(rad) * (1.0 - 0.15 * Math.sin(rad) * Math.sin(rad));
            if (glideProduct > maxProduct) {
                maxProduct = glideProduct;
                bestPitchDeg = deg;
            }
        }

        assertThat(bestPitchDeg).isCloseTo(40.0, within(3.0));
    }

    @Test
    @DisplayName("Rocket Vector: 40-degree pitch unit look vector computation")
    void testFireworkThrustLookVector() {
        double pitchDeg = -40.0;
        double yawDeg = 0.0;

        double pitchRad = Math.toRadians(pitchDeg);
        double yawRad = Math.toRadians(yawDeg);

        double ux = -Math.sin(yawRad) * Math.cos(pitchRad);
        double uy = -Math.sin(pitchRad);
        double uz = Math.cos(yawRad) * Math.cos(pitchRad);

        assertThat(ux).isCloseTo(0.0, within(1e-6));
        assertThat(uy).isCloseTo(0.642787, within(1e-4));
        assertThat(uz).isCloseTo(0.766044, within(1e-4));

        double magnitude = Math.sqrt(ux * ux + uy * uy + uz * uz);
        assertThat(magnitude).isCloseTo(1.0, within(1e-6));
    }

    @Test
    @DisplayName("Elytra Drag: Speed accumulation and terminal velocity during rocket burn")
    void testElytraThrustTerminalVelocity() {
        double rocketThrust = 0.1;
        double drag = 0.99;
        double speed = 0.5;

        // Simulate 40 ticks of rocket burn
        for (int t = 0; t < 40; t++) {
            speed = (speed + rocketThrust) * drag;
        }

        assertThat(speed).isGreaterThan(2.5).isLessThan(9.9);
    }
}
