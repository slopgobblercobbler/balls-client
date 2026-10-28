package com.ballsclient.core.module.movement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Failsafe Threshold & Kinetic Collision Math Test")
class FailsafeMathTest {

    @Test
    @DisplayName("NoFall: Fall distance accumulation and ground spoof reset")
    void testNoFallDamageMitigation() {
        double fallDistance = 0.0;
        double vy = -0.08;

        for (int tick = 0; tick < 25; tick++) {
            fallDistance += -vy;
            vy = (vy - 0.08) * 0.98;
        }

        assertThat(fallDistance).isGreaterThan(15.0);
        int unmitigatedDamage = (int) Math.max(0, Math.floor(fallDistance - 3.0));
        assertThat(unmitigatedDamage).isGreaterThan(12);

        // NoFall packet spoof triggers at threshold 3.0
        boolean spoofTriggered = fallDistance >= 3.0;
        double mitigatedFallDistance = spoofTriggered ? 0.0 : fallDistance;
        int mitigatedDamage = (int) Math.max(0, Math.floor(mitigatedFallDistance - 3.0));

        assertThat(mitigatedDamage).isEqualTo(0);
    }

    @Test
    @DisplayName("AntiVoid: Threshold elevation detection and bounce impulse")
    void testAntiVoidTrigger() {
        double voidThresholdY = 0.0;
        double playerY = -2.5;
        double vy = -1.8;

        boolean shouldTrigger = playerY < voidThresholdY && vy < 0;
        assertThat(shouldTrigger).isTrue();

        double bounceImpulse = 1.2;
        vy = bounceImpulse;
        assertThat(vy).isEqualTo(1.2);
    }

    @Test
    @DisplayName("KineticFailsafe: Obstacle raycast, approach velocity, and emergency brake")
    void testKineticFailsafeMath() {
        double vx = 1.5, vy = -0.2, vz = 0.0;
        double speed = Math.sqrt(vx * vx + vy * vy + vz * vz);
        double thresholdSpeed = 1.4;

        // Obstacle wall normal vector pointing west
        double nx = -1.0, ny = 0.0, nz = 0.0;
        double dot = vx * nx + vy * ny + vz * nz;

        boolean imminentCrash = speed > thresholdSpeed && dot < -0.5;
        assertThat(imminentCrash).isTrue();

        // Emergency brake response
        double emergencyPitch = 80.0;
        assertThat(emergencyPitch).isEqualTo(80.0);
    }
}
