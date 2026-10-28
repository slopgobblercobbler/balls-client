package com.ballsclient.core.module.combat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DisplayName("Vanilla Armor Defense, Toughness & Enchantment Reduction Test")
class ArmorEfficiencyTest {

    private double calculateVanillaDamage(double rawDmg, double defense, double toughness, int epf) {
        if (rawDmg <= 0.0) return 0.0;
        // Step 1: Defense points & Toughness mitigation
        double defensePenalty = rawDmg / (2.0 + toughness / 4.0);
        double effectiveDefense = Math.min(20.0, Math.max(defense / 5.0, defense - defensePenalty));
        double postArmor = rawDmg * (1.0 - effectiveDefense / 25.0);

        // Step 2: Protection Enchantment Factor (EPF capped at 20)
        int clampedEPF = Math.min(20, Math.max(0, epf));
        double postEnchant = postArmor * (1.0 - clampedEPF / 25.0);
        return Math.max(0.0, postEnchant);
    }

    @Test
    @DisplayName("Armor: Zero armor receives 100% unmitigated raw damage")
    void testZeroArmor() {
        double rawDmg = 20.0;
        double netDmg = calculateVanillaDamage(rawDmg, 0.0, 0.0, 0);
        assertThat(netDmg).isEqualTo(20.0);
    }

    @Test
    @DisplayName("Armor: Full Iron (defense=15, toughness=0) vs Full Diamond (defense=20, toughness=8) against 10 damage")
    void testIronVsDiamondMediumDamage() {
        double rawDmg = 10.0;

        // Iron: 15 defense, 0 toughness -> penalty = 10 / 2 = 5 -> effDef = max(2, 15 - 5) = 10 -> net = 10 * (1 - 10/25) = 6.0
        double ironDmg = calculateVanillaDamage(rawDmg, 15.0, 0.0, 0);
        assertThat(ironDmg).isCloseTo(6.0, within(1e-4));

        // Diamond: 20 defense, 8 toughness -> penalty = 10 / (2 + 2) = 2.5 -> effDef = max(2, 20 - 2.5) = 17.5 -> net = 10 * (1 - 17.5/25) = 3.0
        double diamondDmg = calculateVanillaDamage(rawDmg, 20.0, 8.0, 0);
        assertThat(diamondDmg).isCloseTo(3.0, within(1e-4));

        assertThat(diamondDmg).isLessThan(ironDmg);
    }

    @Test
    @DisplayName("Armor Toughness: Heavy damage (50.0) heavily penalizes zero toughness compared to Netherite (12 toughness)")
    void testArmorPenetrationOnHighDamage() {
        double heavyRawDmg = 50.0;

        // Zero toughness, 20 defense: penalty = 50 / 2 = 25 -> effDef = max(4.0, 20 - 25) = 4.0 -> net = 50 * (1 - 4/25) = 42.0 (84% dmg taken)
        double zeroToughnessDmg = calculateVanillaDamage(heavyRawDmg, 20.0, 0.0, 0);
        assertThat(zeroToughnessDmg).isCloseTo(42.0, within(1e-4));

        double rawDmg30 = 30.0;
        double zeroToughDmg30 = calculateVanillaDamage(rawDmg30, 20.0, 0.0, 0);
        double netheriteDmg30 = calculateVanillaDamage(rawDmg30, 20.0, 12.0, 0);

        assertThat(zeroToughDmg30).isCloseTo(24.0, within(1e-4));
        assertThat(netheriteDmg30).isCloseTo(13.2, within(1e-4));
        assertThat(netheriteDmg30).isLessThan(zeroToughDmg30);
    }

    @Test
    @DisplayName("Protection IV: Full Protection IV armor set (EPF = 16) reduces post-armor damage by 64%")
    void testFullProtectionIVReduction() {
        double rawDmg = 20.0;
        // Full Netherite: 20 defense, 12 toughness
        // penalty = 20 / 5 = 4. effDef = 20 - 4 = 16. postArmor = 20 * (1 - 16/25) = 7.2.
        double postArmor = calculateVanillaDamage(rawDmg, 20.0, 12.0, 0);
        assertThat(postArmor).isCloseTo(7.2, within(1e-4));

        // With 4 pieces of Protection IV: EPF = 4 * 4 = 16.
        // Reduction = 16 / 25 = 64%. Net = 7.2 * (1 - 0.64) = 2.592.
        double fullProtNet = calculateVanillaDamage(rawDmg, 20.0, 12.0, 16);
        assertThat(fullProtNet).isCloseTo(2.592, within(1e-4));
        assertThat(fullProtNet / postArmor).isCloseTo(0.36, within(1e-4));
    }

    @Test
    @DisplayName("EPF Cap: EPF values exceeding 20 are strictly clamped to the maximum 80% reduction cap")
    void testEPFCapAt20() {
        double rawDmg = 20.0;
        double dmgEPF20 = calculateVanillaDamage(rawDmg, 20.0, 12.0, 20);
        double dmgEPF32 = calculateVanillaDamage(rawDmg, 20.0, 12.0, 32);

        assertThat(dmgEPF20).isEqualTo(dmgEPF32);
    }
}
