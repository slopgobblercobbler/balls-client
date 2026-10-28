package com.ballsclient.core.module.combat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Totem Failsafe & Fatal Damage Prediction Math Test")
class TotemFailsafeMathTest {

    private boolean evaluateFailsafe(double currentHp, double absorption, double predictedNetDamage, double healthThreshold, double safetyMargin, boolean offhandHasTotem) {
        if (offhandHasTotem) return false;
        double effectiveHp = currentHp + absorption;
        if (currentHp <= healthThreshold) return true;
        if (predictedNetDamage >= (effectiveHp - safetyMargin)) return true;
        return false;
    }

    @Test
    @DisplayName("Totem Failsafe: Fatal crystal hit (net dmg > HP) predictively triggers totem swap")
    void testFatalCrystalHitTriggersSwap() {
        double currentHp = 18.0;
        double absorption = 0.0;
        double incomingCrystalDmg = 24.5; // Fatal blow
        double threshold = 6.0;
        double safetyMargin = 2.0;

        boolean triggered = evaluateFailsafe(currentHp, absorption, incomingCrystalDmg, threshold, safetyMargin, false);
        assertThat(triggered).isTrue();
    }

    @Test
    @DisplayName("Totem Failsafe: Absorption hearts absorb incoming damage and prevent unnecessary totem swap")
    void testAbsorptionHeartsMitigateAndSaveTotem() {
        double currentHp = 18.0;
        double absorption = 10.0; // Golden Apple absorption hearts
        double incomingDmg = 22.0; // Fatal without absorption (22 > 18), but 22 < 28 effective HP
        double threshold = 6.0;
        double safetyMargin = 2.0;

        // Effective HP = 28.0. Threshold condition (28.0 - 2.0 = 26.0) > 22.0 incoming
        boolean triggered = evaluateFailsafe(currentHp, absorption, incomingDmg, threshold, safetyMargin, false);
        assertThat(triggered).isFalse();
    }

    @Test
    @DisplayName("Totem Failsafe: Health below threshold triggers failsafe even without incoming damage")
    void testLowHealthTriggersFailsafeDirectly() {
        double currentHp = 4.5; // Critical HP
        double absorption = 0.0;
        double incomingDmg = 0.0;
        double threshold = 6.0;
        double safetyMargin = 2.0;

        boolean triggered = evaluateFailsafe(currentHp, absorption, incomingDmg, threshold, safetyMargin, false);
        assertThat(triggered).isTrue();
    }

    @Test
    @DisplayName("Totem Failsafe: Non-fatal hit at high HP does not waste totem")
    void testNonFatalHitDoesNotTrigger() {
        double currentHp = 20.0;
        double absorption = 0.0;
        double incomingDmg = 7.5; // Standard sword strike
        double threshold = 6.0;
        double safetyMargin = 2.0;

        boolean triggered = evaluateFailsafe(currentHp, absorption, incomingDmg, threshold, safetyMargin, false);
        assertThat(triggered).isFalse();
    }

    @Test
    @DisplayName("Totem Failsafe: If offhand already contains totem, trigger returns false to avoid swap churn")
    void testOffhandAlreadyHasTotem() {
        boolean triggered = evaluateFailsafe(2.0, 0.0, 50.0, 6.0, 2.0, true);
        assertThat(triggered).isFalse();
    }
}
