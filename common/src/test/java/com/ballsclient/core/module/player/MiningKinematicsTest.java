package com.ballsclient.core.module.player;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DisplayName("Mining Kinematics, Tool Efficiency & Instamine Inequality Test")
class MiningKinematicsTest {

    /**
     * Calculates tool harvest speed based on base material, efficiency enchantment level, and tool harvest match.
     */
    public static double calculateToolSpeed(double baseSpeed, int efficiencyLevel, boolean isProperTool) {
        if (!isProperTool) {
            return 1.0;
        }
        if (efficiencyLevel <= 0) {
            return baseSpeed;
        }
        return baseSpeed + (efficiencyLevel * efficiencyLevel + 1);
    }

    /**
     * Calculates Haste potion effect speed multiplier: 1.0 + 0.2 * level.
     */
    public static double calculateHasteMultiplier(int hasteLevel) {
        if (hasteLevel <= 0) return 1.0;
        return 1.0 + 0.2 * hasteLevel;
    }

    /**
     * Calculates Mining Fatigue damper multiplier: 0.3 ^ min(level, 4).
     */
    public static double calculateFatigueMultiplier(int fatigueLevel) {
        if (fatigueLevel <= 0) return 1.0;
        int clampedLevel = Math.min(fatigueLevel, 4);
        return Math.pow(0.3, clampedLevel);
    }

    /**
     * Calculates effective mining speed factoring in tool, haste, fatigue, water, and airborne penalties.
     */
    public static double calculateEffectiveSpeed(double toolSpeed, int hasteLevel, int fatigueLevel,
                                                 boolean inWaterWithoutAqua, boolean isAirborne) {
        double speed = toolSpeed;
        if (hasteLevel > 0) speed *= calculateHasteMultiplier(hasteLevel);
        if (fatigueLevel > 0) speed *= calculateFatigueMultiplier(fatigueLevel);
        if (inWaterWithoutAqua) speed /= 5.0;
        if (isAirborne) speed /= 5.0;
        return speed;
    }

    /**
     * Calculates block damage per tick. Divisor is 30.0 for proper harvestable tools, 100.0 otherwise.
     */
    public static double calculateDamagePerTick(double effectiveSpeed, double blockHardness, boolean canHarvest) {
        double divisor = canHarvest ? (blockHardness * 30.0) : (blockHardness * 100.0);
        return effectiveSpeed / divisor;
    }

    /**
     * Instamine inequality: damagePerTick >= 1.0 (equivalent to speed >= 30.0 * hardness).
     */
    public static boolean isInstamine(double effectiveSpeed, double blockHardness, boolean canHarvest) {
        if (!canHarvest) return false;
        return effectiveSpeed >= (30.0 * blockHardness);
    }

    /**
     * Calculates total client ticks required to break a block.
     */
    public static int calculateTicksToBreak(double damagePerTick) {
        if (damagePerTick >= 1.0) return 1;
        return (int) Math.ceil(1.0 / damagePerTick);
    }

    @Test
    @DisplayName("Tool Efficiency: Formula (base + level^2 + 1) scales correctly across material tiers")
    void testToolEfficiencyFormula() {
        // Hand (base 1.0, no enchantments)
        assertThat(calculateToolSpeed(1.0, 0, false)).isEqualTo(1.0);

        // Wooden Pickaxe (base 2.0)
        assertThat(calculateToolSpeed(2.0, 1, true)).isEqualTo(4.0); // 2 + (1 + 1)
        assertThat(calculateToolSpeed(2.0, 5, true)).isEqualTo(28.0); // 2 + (25 + 1)

        // Iron Pickaxe (base 6.0)
        assertThat(calculateToolSpeed(6.0, 5, true)).isEqualTo(32.0); // 6 + 26

        // Diamond Pickaxe (base 8.0)
        assertThat(calculateToolSpeed(8.0, 1, true)).isEqualTo(10.0); // 8 + 2
        assertThat(calculateToolSpeed(8.0, 2, true)).isEqualTo(13.0); // 8 + 5
        assertThat(calculateToolSpeed(8.0, 3, true)).isEqualTo(18.0); // 8 + 10
        assertThat(calculateToolSpeed(8.0, 4, true)).isEqualTo(25.0); // 8 + 17
        assertThat(calculateToolSpeed(8.0, 5, true)).isEqualTo(34.0); // 8 + 26

        // Netherite Pickaxe (base 9.0)
        assertThat(calculateToolSpeed(9.0, 5, true)).isEqualTo(35.0); // 9 + 26

        // Gold Pickaxe (base 12.0)
        assertThat(calculateToolSpeed(12.0, 5, true)).isEqualTo(38.0); // 12 + 26

        // Improper tool (Axe on Stone block) ignores efficiency enchantments
        assertThat(calculateToolSpeed(9.0, 5, false)).isEqualTo(1.0);
    }

    @Test
    @DisplayName("Haste Multipliers: 1.0 + 0.2 * level scaling")
    void testHasteMultipliers() {
        assertThat(calculateHasteMultiplier(0)).isEqualTo(1.0);
        assertThat(calculateHasteMultiplier(1)).isCloseTo(1.2, within(0.001));
        assertThat(calculateHasteMultiplier(2)).isCloseTo(1.4, within(0.001));
        assertThat(calculateHasteMultiplier(3)).isCloseTo(1.6, within(0.001));

        // Netherite Efficiency V (35.0) under Haste II (1.4x)
        double netheriteSpeed = calculateToolSpeed(9.0, 5, true);
        double hasteSpeed = netheriteSpeed * calculateHasteMultiplier(2);
        assertThat(hasteSpeed).isCloseTo(49.0, within(0.001));
    }

    @Test
    @DisplayName("Fatigue Dampers: 0.3^min(level, 4) exponential reduction")
    void testFatigueDampers() {
        assertThat(calculateFatigueMultiplier(0)).isEqualTo(1.0);
        assertThat(calculateFatigueMultiplier(1)).isCloseTo(0.3, within(0.001));
        assertThat(calculateFatigueMultiplier(2)).isCloseTo(0.09, within(0.001));
        assertThat(calculateFatigueMultiplier(3)).isCloseTo(0.027, within(0.001));
        assertThat(calculateFatigueMultiplier(4)).isCloseTo(0.0081, within(0.0001));
        // Level 5 capped at 4
        assertThat(calculateFatigueMultiplier(5)).isCloseTo(0.0081, within(0.0001));
    }

    @Test
    @DisplayName("Instamine Inequality: Evaluates threshold (effectiveSpeed >= 30 * H) for Stone, Deepslate, Dirt")
    void testInstamineInequality() {
        double stoneHardness = 1.5;
        double deepslateHardness = 3.0;
        double dirtHardness = 0.5;

        // Stone required threshold: 1.5 * 30 = 45.0
        double nethEff5Haste2 = calculateEffectiveSpeed(calculateToolSpeed(9.0, 5, true), 2, 0, false, false); // 49.0
        double diaEff5Haste2 = calculateEffectiveSpeed(calculateToolSpeed(8.0, 5, true), 2, 0, false, false); // 47.6
        double diaEff5Haste1 = calculateEffectiveSpeed(calculateToolSpeed(8.0, 5, true), 1, 0, false, false); // 40.8
        double nethEff5NoHaste = calculateEffectiveSpeed(calculateToolSpeed(9.0, 5, true), 0, 0, false, false); // 35.0

        // Netherite Eff 5 + Haste 2 -> Instamine Stone! (49.0 >= 45.0)
        assertThat(isInstamine(nethEff5Haste2, stoneHardness, true)).isTrue();
        // Diamond Eff 5 + Haste 2 -> Instamine Stone! (47.6 >= 45.0)
        assertThat(isInstamine(diaEff5Haste2, stoneHardness, true)).isTrue();
        // Diamond Eff 5 + Haste 1 -> Cannot Instamine Stone (40.8 < 45.0)
        assertThat(isInstamine(diaEff5Haste1, stoneHardness, true)).isFalse();
        // Netherite Eff 5 without Haste -> Cannot Instamine Stone (35.0 < 45.0)
        assertThat(isInstamine(nethEff5NoHaste, stoneHardness, true)).isFalse();

        // Deepslate required threshold: 3.0 * 30 = 90.0
        // Vanilla tools cannot instamine Deepslate (49.0 < 90.0)
        assertThat(isInstamine(nethEff5Haste2, deepslateHardness, true)).isFalse();

        // Dirt required threshold: 0.5 * 30 = 15.0
        double diaShovelEff3 = calculateToolSpeed(8.0, 3, true); // 8 + 10 = 18.0
        double diaShovelEff2 = calculateToolSpeed(8.0, 2, true); // 8 + 5 = 13.0
        // Diamond Shovel Eff 3 instamines Dirt without Haste! (18.0 >= 15.0)
        assertThat(isInstamine(diaShovelEff3, dirtHardness, true)).isTrue();
        // Diamond Shovel Eff 2 cannot instamine Dirt without Haste (13.0 < 15.0)
        assertThat(isInstamine(diaShovelEff2, dirtHardness, true)).isFalse();
    }

    @Test
    @DisplayName("Discrete Break Ticks: Computes exact tick durations for Stone, Obsidian, and Deepslate")
    void testDiscreteBreakTicks() {
        // Stone (H=1.5) with un-enchanted Iron Pickaxe (speed 6.0)
        // damagePerTick = 6.0 / (1.5 * 30.0) = 6.0 / 45.0 = 0.1333...
        // ticks = ceil(45.0 / 6.0) = ceil(7.5) = 8 ticks (0.40 seconds)
        double dmgIronStone = calculateDamagePerTick(6.0, 1.5, true);
        assertThat(dmgIronStone).isCloseTo(0.1333, within(0.001));
        assertThat(calculateTicksToBreak(dmgIronStone)).isEqualTo(8);

        // Obsidian (H=50.0) with un-enchanted Diamond Pickaxe (speed 8.0)
        // damagePerTick = 8.0 / (50.0 * 30.0) = 8.0 / 1500.0 = 0.005333...
        // ticks = ceil(1500.0 / 8.0) = 188 ticks (9.40 seconds)
        double dmgDiaObsidian = calculateDamagePerTick(8.0, 50.0, true);
        assertThat(calculateTicksToBreak(dmgDiaObsidian)).isEqualTo(188);

        // Obsidian with Netherite Eff 5 + Haste 2 (speed 49.0)
        // damagePerTick = 49.0 / 1500.0 = 0.032666...
        // ticks = ceil(1500.0 / 49.0) = ceil(30.61) = 31 ticks (1.55 seconds)
        double dmgNethObsidian = calculateDamagePerTick(49.0, 50.0, true);
        assertThat(calculateTicksToBreak(dmgNethObsidian)).isEqualTo(31);
    }

    @Test
    @DisplayName("Penalties: Submerged in water and airborne 5x/25x speed dampers")
    void testEnvironmentalPenalties() {
        double toolSpeed = calculateToolSpeed(8.0, 5, true); // Diamond Eff 5 = 34.0
        double stoneHardness = 1.5;

        // Ground condition: damage = 34.0 / 45.0 = 0.7555... -> 2 ticks
        double groundSpeed = calculateEffectiveSpeed(toolSpeed, 0, 0, false, false);
        assertThat(calculateTicksToBreak(calculateDamagePerTick(groundSpeed, stoneHardness, true))).isEqualTo(2);

        // Submerged without Aqua Affinity (speed divided by 5 -> 6.8): damage = 6.8 / 45.0 = 0.1511... -> 7 ticks
        double waterSpeed = calculateEffectiveSpeed(toolSpeed, 0, 0, true, false);
        assertThat(waterSpeed).isCloseTo(6.8, within(0.001));
        assertThat(calculateTicksToBreak(calculateDamagePerTick(waterSpeed, stoneHardness, true))).isEqualTo(7);

        // Airborne while falling/jumping (speed divided by 5 -> 6.8): -> 7 ticks
        double airSpeed = calculateEffectiveSpeed(toolSpeed, 0, 0, false, true);
        assertThat(airSpeed).isCloseTo(6.8, within(0.001));
        assertThat(calculateTicksToBreak(calculateDamagePerTick(airSpeed, stoneHardness, true))).isEqualTo(7);

        // Both submerged and airborne (speed divided by 25 -> 1.36): damage = 1.36 / 45.0 = 0.03022... -> 34 ticks
        double bothSpeed = calculateEffectiveSpeed(toolSpeed, 0, 0, true, true);
        assertThat(bothSpeed).isCloseTo(1.36, within(0.001));
        assertThat(calculateTicksToBreak(calculateDamagePerTick(bothSpeed, stoneHardness, true))).isEqualTo(34);
    }
}
