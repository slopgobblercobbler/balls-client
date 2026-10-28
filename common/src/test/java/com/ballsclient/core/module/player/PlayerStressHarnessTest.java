package com.ballsclient.core.module.player;

import com.ballsclient.core.event.EventBus;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.ModuleManager;
import com.ballsclient.core.module.player.world.*;
import com.ballsclient.core.module.player.interaction.*;
import com.ballsclient.core.module.player.mining.*;
import com.ballsclient.core.module.player.utility.*;
import com.ballsclient.core.module.player.automation.*;
import com.ballsclient.core.setting.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DisplayName("Player & World Suite Empirical Adversarial Stress Harness")
class PlayerStressHarnessTest {

    // =========================================================================
    // 1. SLIME CHUNK PRNG UNDER EXTREME BOUNDARIES
    // =========================================================================

    @Test
    @DisplayName("SlimeChunk Stress: PRNG seed boundary extremes (0, Long.MIN_VALUE, Long.MAX_VALUE, -1L, 1L)")
    void testSlimeChunkSeedBoundaryExtremes() {
        long[] extremeSeeds = new long[]{
                0L,
                Long.MIN_VALUE,
                Long.MAX_VALUE,
                -1L,
                1L,
                123456789L,
                -9223372036854775807L
        };

        int[][] testCoordinates = new int[][]{
                {0, 0},
                {10, -10},
                {1000, 2000},
                {-5000, -8000},
                {1_875_000, 1_875_000}, // World border chunk
                {-1_875_000, -1_875_000}
        };

        for (long seed : extremeSeeds) {
            for (int[] coord : testCoordinates) {
                int cx = coord[0];
                int cz = coord[1];

                // Verify determinism across multiple calls
                boolean first = SlimeChunkMatrix.isSlimeChunk(seed, cx, cz);
                boolean second = SlimeChunkMatrix.isSlimeChunk(seed, cx, cz);
                boolean third = SlimeChunkMathTest.isSlimeChunk(seed, cx, cz);

                assertThat(first)
                        .as("SlimeChunk determinism failed for seed %d at (%d, %d)", seed, cx, cz)
                        .isEqualTo(second)
                        .isEqualTo(third);
            }
        }

        // Statistical distribution test under extreme seeds (1,000 chunks each)
        for (long seed : new long[]{0L, Long.MIN_VALUE, Long.MAX_VALUE}) {
            int slimeCount = 0;
            int total = 1000;
            for (int cx = 0; cx < 50; cx++) {
                for (int cz = 0; cz < 20; cz++) {
                    if (SlimeChunkMatrix.isSlimeChunk(seed, cx, cz)) {
                        slimeCount++;
                    }
                }
            }
            double density = (double) slimeCount / total;
            // 1/10 expected density, should comfortably fall within [5%, 16%] for 1000 samples
            assertThat(density)
                    .as("Statistical density under extreme seed %d deviated abnormally: %f", seed, density)
                    .isBetween(0.05, 0.16);
        }
    }

    @Test
    @DisplayName("SlimeChunk Stress: Extreme chunk coordinates (+-2,000,000) and world border floor division")
    void testSlimeChunkExtremeCoordinates() {
        long seed = 9876543210L;

        int[][] extremeChunks = new int[][]{
                {2_000_000, 2_000_000},
                {-2_000_000, -2_000_000},
                {2_000_000, -2_000_000},
                {-2_000_000, 2_000_000},
                {1_875_000, -1_875_000}, // +-30,000,000 / 16
                {Integer.MAX_VALUE, Integer.MAX_VALUE},
                {Integer.MIN_VALUE, Integer.MIN_VALUE},
                {Integer.MAX_VALUE, Integer.MIN_VALUE}
        };

        for (int[] chunk : extremeChunks) {
            int cx = chunk[0];
            int cz = chunk[1];

            // Must evaluate without throwing ArithmeticException, StackOverflow, or NPE
            boolean result = SlimeChunkMatrix.isSlimeChunk(seed, cx, cz);
            assertThat(result).isIn(true, false);

            boolean helperResult = SlimeChunkMathTest.isSlimeChunk(seed, cx, cz);
            assertThat(result).isEqualTo(helperResult);
        }

        // Block coordinate translation at +-30,000,000 world border
        assertThat(SlimeChunkMatrix.toChunkCoord(30_000_000)).isEqualTo(1_875_000);
        assertThat(SlimeChunkMatrix.toChunkCoord(-30_000_000)).isEqualTo(-1_875_000);
        assertThat(SlimeChunkMatrix.toChunkCoord(-30_000_001)).isEqualTo(-1_875_001);
        assertThat(SlimeChunkMatrix.toChunkCoord(29_999_999)).isEqualTo(1_874_999);

        // Integer extrema coordinate conversion
        assertThat(SlimeChunkMatrix.toChunkCoord(Integer.MAX_VALUE)).isEqualTo(Math.floorDiv(Integer.MAX_VALUE, 16));
        assertThat(SlimeChunkMatrix.toChunkCoord(Integer.MIN_VALUE)).isEqualTo(Math.floorDiv(Integer.MIN_VALUE, 16));
    }

    @Test
    @DisplayName("SlimeChunk Stress: Height boundaries (y = 39.999 vs 40.0, y = -64.0 vs -64.001, NaN/Infinity)")
    void testSlimeChunkHeightBoundaries() {
        long seed = 777123L;

        // Locate a verified slime chunk
        int slimeCx = -1;
        int slimeCz = -1;
        for (int x = 0; x < 100; x++) {
            for (int z = 0; z < 100; z++) {
                if (SlimeChunkMatrix.isSlimeChunk(seed, x, z)) {
                    slimeCx = x;
                    slimeCz = z;
                    break;
                }
            }
            if (slimeCx != -1) break;
        }
        assertThat(slimeCx).isGreaterThanOrEqualTo(0);

        // Find a non-slime chunk for negative verification
        int nonSlimeCx = -1;
        int nonSlimeCz = -1;
        for (int x = 0; x < 100; x++) {
            for (int z = 0; z < 100; z++) {
                if (!SlimeChunkMatrix.isSlimeChunk(seed, x, z)) {
                    nonSlimeCx = x;
                    nonSlimeCz = z;
                    break;
                }
            }
            if (nonSlimeCx != -1) break;
        }
        assertThat(nonSlimeCx).isGreaterThanOrEqualTo(0);

        // Subcase 1: Upper Y boundary at 40.0
        // y < 40.0 must allow slime spawning in a slime chunk
        assertThat(SlimeChunkMatrix.canSpawnSlime(seed, slimeCx, slimeCz, 39.999)).isTrue();
        assertThat(SlimeChunkMatrix.canSpawnSlime(seed, slimeCx, slimeCz, 39.999999)).isTrue();
        assertThat(SlimeChunkMatrix.canSpawnSlime(seed, slimeCx, slimeCz, 39.0)).isTrue();

        // y >= 40.0 strictly prohibits slime spawning
        assertThat(SlimeChunkMatrix.canSpawnSlime(seed, slimeCx, slimeCz, 40.0)).isFalse();
        assertThat(SlimeChunkMatrix.canSpawnSlime(seed, slimeCx, slimeCz, 40.0001)).isFalse();
        assertThat(SlimeChunkMatrix.canSpawnSlime(seed, slimeCx, slimeCz, 64.0)).isFalse();
        assertThat(SlimeChunkMatrix.canSpawnSlime(seed, slimeCx, slimeCz, 320.0)).isFalse();

        // Subcase 2: Lower Y boundary at -64.0 (bedrock level in modern 1.18+)
        // y >= -64.0 permits
        assertThat(SlimeChunkMatrix.canSpawnSlime(seed, slimeCx, slimeCz, -64.0)).isTrue();
        assertThat(SlimeChunkMatrix.canSpawnSlime(seed, slimeCx, slimeCz, -63.999)).isTrue();

        // y < -64.0 forbids (void)
        assertThat(SlimeChunkMatrix.canSpawnSlime(seed, slimeCx, slimeCz, -64.0001)).isFalse();
        assertThat(SlimeChunkMatrix.canSpawnSlime(seed, slimeCx, slimeCz, -65.0)).isFalse();
        assertThat(SlimeChunkMatrix.canSpawnSlime(seed, slimeCx, slimeCz, -100.0)).isFalse();

        // Subcase 3: Non-slime chunk never spawns at any valid elevation
        assertThat(SlimeChunkMatrix.canSpawnSlime(seed, nonSlimeCx, nonSlimeCz, 39.999)).isFalse();
        assertThat(SlimeChunkMatrix.canSpawnSlime(seed, nonSlimeCx, nonSlimeCz, 0.0)).isFalse();
        assertThat(SlimeChunkMatrix.canSpawnSlime(seed, nonSlimeCx, nonSlimeCz, -64.0)).isFalse();

        // Subcase 4: Module Setting Gating via canSlimeSpawn(...)
        // When checkElevation is FALSE, elevation restriction is bypassed
        assertThat(SlimeChunkMatrix.canSlimeSpawn(seed, slimeCx, slimeCz, 40.0, false, 40.0)).isTrue();
        assertThat(SlimeChunkMatrix.canSlimeSpawn(seed, slimeCx, slimeCz, 256.0, false, 40.0)).isTrue();

        // When checkElevation is TRUE, maxElevation threshold governs
        assertThat(SlimeChunkMatrix.canSlimeSpawn(seed, slimeCx, slimeCz, 39.999, true, 40.0)).isTrue();
        assertThat(SlimeChunkMatrix.canSlimeSpawn(seed, slimeCx, slimeCz, 40.0, true, 40.0)).isFalse();
        assertThat(SlimeChunkMatrix.canSlimeSpawn(seed, slimeCx, slimeCz, 69.9, true, 70.0)).isTrue();
        assertThat(SlimeChunkMatrix.canSlimeSpawn(seed, slimeCx, slimeCz, 70.0, true, 70.0)).isFalse();

        // Subcase 5: Floating point extremes (+/- Infinity, NaN elevation handling)
        assertThat(SlimeChunkMatrix.canSpawnSlime(seed, slimeCx, slimeCz, Double.POSITIVE_INFINITY)).isFalse();
        assertThat(SlimeChunkMatrix.canSpawnSlime(seed, slimeCx, slimeCz, Double.NEGATIVE_INFINITY)).isFalse();
        // canSlimeSpawn explicitly validates elevation range [-64.0, maxElevation) which safely rejects NaN
        assertThat(SlimeChunkMatrix.canSlimeSpawn(seed, slimeCx, slimeCz, Double.NaN, true, 40.0)).isFalse();
    }

    // =========================================================================
    // 2. NETHER PORTAL SYNC UNDER EXTREME BOUNDARIES
    // =========================================================================

    @Test
    @DisplayName("NetherPortal Stress: Extreme world border coordinates (+-30,000,000)")
    void testNetherPortalWorldBorderCoordinates() {
        double worldBorder = 30_000_000.0;

        // Overworld (+30,000,000, 64, +30,000,000) -> Nether (+3,750,000, 64, +3,750,000)
        double[] netherBorderPos = NetherPortalSync.toNether(worldBorder, 64.0, worldBorder, true);
        assertThat(netherBorderPos[0]).isEqualTo(3_750_000.0);
        assertThat(netherBorderPos[1]).isEqualTo(64.0);
        assertThat(netherBorderPos[2]).isEqualTo(3_750_000.0);

        // Overworld (-30,000,000, 64, -30,000,000) -> Nether (-3,750,000, 64, -3,750,000)
        double[] netherBorderNeg = NetherPortalSync.toNether(-worldBorder, 64.0, -worldBorder, true);
        assertThat(netherBorderNeg[0]).isEqualTo(-3_750_000.0);
        assertThat(netherBorderNeg[1]).isEqualTo(64.0);
        assertThat(netherBorderNeg[2]).isEqualTo(-3_750_000.0);

        // Exact round-trip fidelity at world border
        double[] roundTrip = NetherPortalSync.toOverworld(netherBorderPos[0], netherBorderPos[1], netherBorderPos[2]);
        assertThat(roundTrip[0]).isEqualTo(worldBorder);
        assertThat(roundTrip[1]).isEqualTo(64.0);
        assertThat(roundTrip[2]).isEqualTo(worldBorder);

        // Nether world border coordinate scaling to Overworld
        // Nether at +-30,000,000 -> Overworld at +-240,000,000
        double[] owExtreme = NetherPortalSync.toOverworld(worldBorder, 64.0, -worldBorder);
        assertThat(owExtreme[0]).isEqualTo(240_000_000.0);
        assertThat(owExtreme[1]).isEqualTo(64.0);
        assertThat(owExtreme[2]).isEqualTo(-240_000_000.0);

        // Subcase: Extreme coordinate scales beyond world border (10^9)
        double[] billionNether = NetherPortalSync.toNether(1_000_000_000.0, 64.0, -1_000_000_000.0, true);
        assertThat(billionNether[0]).isEqualTo(125_000_000.0);
        assertThat(billionNether[2]).isEqualTo(-125_000_000.0);
    }

    @Test
    @DisplayName("NetherPortal Stress: Nether Y ceiling clamping [0, 127] and Overworld bounds [-64, 319]")
    void testNetherPortalElevationClamping() {
        // Clamping Enabled (clampCeiling = true)
        // High Overworld heights clamp strictly to 127.0
        assertThat(NetherPortalSync.toNether(0.0, 127.0, 0.0, true)[1]).isEqualTo(127.0);
        assertThat(NetherPortalSync.toNether(0.0, 127.0001, 0.0, true)[1]).isEqualTo(127.0);
        assertThat(NetherPortalSync.toNether(0.0, 256.0, 0.0, true)[1]).isEqualTo(127.0);
        assertThat(NetherPortalSync.toNether(0.0, 320.0, 0.0, true)[1]).isEqualTo(127.0);
        assertThat(NetherPortalSync.toNether(0.0, 10_000.0, 0.0, true)[1]).isEqualTo(127.0);

        // Deep/Negative Overworld heights clamp strictly to 0.0
        assertThat(NetherPortalSync.toNether(0.0, 0.0, 0.0, true)[1]).isEqualTo(0.0);
        assertThat(NetherPortalSync.toNether(0.0, -0.0001, 0.0, true)[1]).isEqualTo(0.0);
        assertThat(NetherPortalSync.toNether(0.0, -64.0, 0.0, true)[1]).isEqualTo(0.0);
        assertThat(NetherPortalSync.toNether(0.0, -10_000.0, 0.0, true)[1]).isEqualTo(0.0);

        // Clamping Disabled (clampCeiling = false) -> Allows roof building and negative deep layers
        assertThat(NetherPortalSync.toNether(0.0, 256.0, 0.0, false)[1]).isEqualTo(256.0);
        assertThat(NetherPortalSync.toNether(0.0, -64.0, 0.0, false)[1]).isEqualTo(-64.0);

        // Overworld Destination Height Clamping in toOverworld: [-64, 319]
        assertThat(NetherPortalSync.toOverworld(0.0, 400.0, 0.0)[1]).isEqualTo(319.0);
        assertThat(NetherPortalSync.toOverworld(0.0, 319.0, 0.0)[1]).isEqualTo(319.0);
        assertThat(NetherPortalSync.toOverworld(0.0, 319.001, 0.0)[1]).isEqualTo(319.0);
        assertThat(NetherPortalSync.toOverworld(0.0, -64.0, 0.0)[1]).isEqualTo(-64.0);
        assertThat(NetherPortalSync.toOverworld(0.0, -64.001, 0.0)[1]).isEqualTo(-64.0);
        assertThat(NetherPortalSync.toOverworld(0.0, -500.0, 0.0)[1]).isEqualTo(-64.0);
    }

    @Test
    @DisplayName("NetherPortal Stress: Nearest portal candidate distance comparisons and search radius edge cases")
    void testNetherPortalCandidateDistanceComparisons() {
        // Edge cases: null or empty candidate list
        assertThat(NetherPortalSync.findNearestPortal(0, 64, 0, null, 128.0)).isEqualTo(-1);
        assertThat(NetherPortalSync.findNearestPortal(0, 64, 0, new double[0][], 128.0)).isEqualTo(-1);

        // Candidate list with malformed or null rows
        double[][] malformedCandidates = new double[][]{
                null,
                new double[]{10.0},
                new double[]{10.0, 20.0},
                {10.0, 64.0, 10.0} // Valid candidate at index 3
        };
        assertThat(NetherPortalSync.findNearestPortal(0, 64, 0, malformedCandidates, 128.0)).isEqualTo(3);

        // Search Radius Boundary: exactly 128.0m vs 128.001m
        double targetX = 0.0, targetY = 64.0, targetZ = 0.0;
        double[][] boundaryCandidates = new double[][]{
                {128.0, 64.0, 0.0},     // dist2D = 128.0 -> Exactly on boundary (IN)
                {0.0, 64.0, 128.001}     // dist2D = 128.001 -> Out of range (OUT)
        };
        assertThat(NetherPortalSync.findNearestPortal(targetX, targetY, targetZ, boundaryCandidates, 128.0)).isEqualTo(0);

        // Out of range all candidates
        double[][] outOfRange = new double[][]{
                {128.1, 64.0, 0.0},
                {0.0, 64.0, 150.0}
        };
        assertThat(NetherPortalSync.findNearestPortal(targetX, targetY, targetZ, outOfRange, 128.0)).isEqualTo(-1);

        // 3D Euclidean Tie-breaking: Portals with identical 2D distance but differing elevation
        double[][] tieBreakCandidates = new double[][]{
                {10.0, 120.0, 0.0}, // dist2D = 10.0, dy = |120 - 64| = 56 -> dist3D = sqrt(100 + 3136) = 56.88
                {10.0, 65.0, 0.0},  // dist2D = 10.0, dy = |65 - 64| = 1 -> dist3D = sqrt(100 + 1) = 10.05 (BEST)
                {10.0, 30.0, 0.0}   // dist2D = 10.0, dy = |30 - 64| = 34 -> dist3D = sqrt(100 + 1156) = 35.44
        };
        assertThat(NetherPortalSync.findNearestPortal(targetX, targetY, targetZ, tieBreakCandidates, 128.0)).isEqualTo(1);

        // High-volume candidate stress at extreme world border coordinates (+3,750,000)
        double wbNetherX = 3_750_000.0;
        double wbNetherZ = 3_750_000.0;
        int totalCandidates = 1000;
        double[][] largeCandidateSet = new double[totalCandidates][3];
        Random rnd = new Random(42);

        // Place all random candidates > 150m away
        for (int i = 0; i < totalCandidates; i++) {
            largeCandidateSet[i][0] = wbNetherX + 200.0 + rnd.nextDouble() * 500.0;
            largeCandidateSet[i][1] = 64.0;
            largeCandidateSet[i][2] = wbNetherZ + 200.0 + rnd.nextDouble() * 500.0;
        }

        // Place exactly ONE target portal at distance 45.0m at index 731
        int targetIndex = 731;
        largeCandidateSet[targetIndex][0] = wbNetherX + 30.0;
        largeCandidateSet[targetIndex][1] = 68.0;
        largeCandidateSet[targetIndex][2] = wbNetherZ + 30.0; // dist2D = sqrt(900+900) = 42.42m <= 128.0

        int nearest = NetherPortalSync.findNearestPortal(wbNetherX, 64.0, wbNetherZ, largeCandidateSet, 128.0);
        assertThat(nearest).isEqualTo(targetIndex);
    }

    // =========================================================================
    // 3. MINING KINEMATICS UNDER EXTREME ENCHANTMENTS & CONDITIONS
    // =========================================================================

    @Test
    @DisplayName("MiningKinematics Stress: Efficiency scaling from 0 up to 100")
    void testMiningKinematicsExtremeEfficiency() {
        double netheriteBaseSpeed = 9.0;
        double diamondBaseSpeed = 8.0;

        // Eff 0 (no enchantment)
        assertThat(MiningKinematicsTest.calculateToolSpeed(netheriteBaseSpeed, 0, true)).isEqualTo(9.0);
        assertThat(MiningKinematicsTest.calculateToolSpeed(diamondBaseSpeed, 0, true)).isEqualTo(8.0);

        // Negative efficiency level falls back safely to base speed
        assertThat(MiningKinematicsTest.calculateToolSpeed(netheriteBaseSpeed, -10, true)).isEqualTo(9.0);

        // Vanilla Levels (1 through 5): base + (L^2 + 1)
        assertThat(MiningKinematicsTest.calculateToolSpeed(netheriteBaseSpeed, 1, true)).isEqualTo(11.0); // 9 + 2
        assertThat(MiningKinematicsTest.calculateToolSpeed(netheriteBaseSpeed, 2, true)).isEqualTo(14.0); // 9 + 5
        assertThat(MiningKinematicsTest.calculateToolSpeed(netheriteBaseSpeed, 3, true)).isEqualTo(19.0); // 9 + 10
        assertThat(MiningKinematicsTest.calculateToolSpeed(netheriteBaseSpeed, 4, true)).isEqualTo(26.0); // 9 + 17
        assertThat(MiningKinematicsTest.calculateToolSpeed(netheriteBaseSpeed, 5, true)).isEqualTo(35.0); // 9 + 26

        // Extreme Levels (10 to 100)
        // Eff 10: 9 + (100 + 1) = 110.0
        assertThat(MiningKinematicsTest.calculateToolSpeed(netheriteBaseSpeed, 10, true)).isEqualTo(110.0);
        // Eff 20: 9 + (400 + 1) = 410.0
        assertThat(MiningKinematicsTest.calculateToolSpeed(netheriteBaseSpeed, 20, true)).isEqualTo(410.0);
        // Eff 50: 9 + (2500 + 1) = 2510.0
        assertThat(MiningKinematicsTest.calculateToolSpeed(netheriteBaseSpeed, 50, true)).isEqualTo(2510.0);
        // Eff 100: 9 + (10000 + 1) = 10010.0
        assertThat(MiningKinematicsTest.calculateToolSpeed(netheriteBaseSpeed, 100, true)).isEqualTo(10010.0);

        // Parity with InstamineThresholdIndicator module helper
        assertThat(InstamineThresholdIndicator.calculateEffectiveSpeed(netheriteBaseSpeed, 100, 0))
                .isEqualTo(10010.0);

        // Improper tool (e.g. Sword on Stone) MUST return 1.0 regardless of efficiency 100
        assertThat(MiningKinematicsTest.calculateToolSpeed(netheriteBaseSpeed, 100, false)).isEqualTo(1.0);
    }

    @Test
    @DisplayName("MiningKinematics Stress: Haste 255 and Mining Fatigue 255 scaling")
    void testMiningKinematicsExtremeHasteAndFatigue() {
        // Haste scaling: 1.0 + 0.2 * level
        assertThat(MiningKinematicsTest.calculateHasteMultiplier(0)).isEqualTo(1.0);
        assertThat(MiningKinematicsTest.calculateHasteMultiplier(-5)).isEqualTo(1.0);
        assertThat(MiningKinematicsTest.calculateHasteMultiplier(1)).isCloseTo(1.2, within(1e-4));
        assertThat(MiningKinematicsTest.calculateHasteMultiplier(2)).isCloseTo(1.4, within(1e-4));

        // Extreme Haste 255: 1.0 + 0.2 * 255 = 1.0 + 51.0 = 52.0x multiplier!
        assertThat(MiningKinematicsTest.calculateHasteMultiplier(255)).isCloseTo(52.0, within(1e-4));

        // Fatigue scaling: 0.3 ^ min(level, 4)
        assertThat(MiningKinematicsTest.calculateFatigueMultiplier(0)).isEqualTo(1.0);
        assertThat(MiningKinematicsTest.calculateFatigueMultiplier(-2)).isEqualTo(1.0);
        assertThat(MiningKinematicsTest.calculateFatigueMultiplier(1)).isCloseTo(0.3, within(1e-4));
        assertThat(MiningKinematicsTest.calculateFatigueMultiplier(2)).isCloseTo(0.09, within(1e-4));
        assertThat(MiningKinematicsTest.calculateFatigueMultiplier(3)).isCloseTo(0.027, within(1e-4));
        assertThat(MiningKinematicsTest.calculateFatigueMultiplier(4)).isCloseTo(0.0081, within(1e-5));

        // Fatigue 5 and Fatigue 255 strictly cap at 4 (0.0081)
        assertThat(MiningKinematicsTest.calculateFatigueMultiplier(5)).isCloseTo(0.0081, within(1e-5));
        assertThat(MiningKinematicsTest.calculateFatigueMultiplier(255)).isCloseTo(0.0081, within(1e-5));

        // Instamine under extreme Haste 255:
        // Obsidian (Hardness 50.0 -> Threshold: 50 * 30 = 1500.0)
        double nethEff5Speed = MiningKinematicsTest.calculateToolSpeed(9.0, 5, true); // 35.0
        double nethEff5Haste255 = MiningKinematicsTest.calculateEffectiveSpeed(nethEff5Speed, 255, 0, false, false);
        // 35.0 * 52.0 = 1820.0
        assertThat(nethEff5Haste255).isEqualTo(1820.0);
        // 1820.0 >= 1500.0 -> Instamines Obsidian!
        assertThat(MiningKinematicsTest.isInstamine(nethEff5Haste255, 50.0, true)).isTrue();

        // Parity with InstamineThresholdIndicator
        assertThat(InstamineThresholdIndicator.canInstamine(9.0, 5, 255, 50.0, true)).isTrue();

        // Combined extreme Haste 255 and Fatigue 255
        double hasteAndFatigueSpeed = MiningKinematicsTest.calculateEffectiveSpeed(nethEff5Speed, 255, 255, false, false);
        // 35.0 * 52.0 * 0.0081 = 14.742
        assertThat(hasteAndFatigueSpeed).isCloseTo(14.742, within(0.001));
    }

    @Test
    @DisplayName("MiningKinematics Stress: Negative hardness, unbreakable blocks, and zero hardness")
    void testMiningKinematicsNegativeHardnessAndUnbreakable() {
        // Bedrock, Barriers, End Portal Frames have hardness = -1.0
        assertThat(UnbreakableFilter.isUnbreakable(-1.0)).isTrue();
        assertThat(UnbreakableFilter.isUnbreakable(-100.0)).isTrue();

        // Normal blocks are breakable
        assertThat(UnbreakableFilter.isUnbreakable(1.5)).isFalse();  // Stone
        assertThat(UnbreakableFilter.isUnbreakable(50.0)).isFalse(); // Obsidian
        assertThat(UnbreakableFilter.isUnbreakable(0.5)).isFalse();  // Dirt

        // Zero hardness (Torches, Redstone Wire, Slime Block)
        assertThat(UnbreakableFilter.isUnbreakable(0.0)).isFalse();

        // Damage per tick on zero-hardness block evaluates to Positive Infinity
        double zeroHardnessDmg = MiningKinematicsTest.calculateDamagePerTick(1.0, 0.0, true);
        assertThat(Double.isInfinite(zeroHardnessDmg)).isTrue();
        // Infinity damage per tick immediately breaks in exactly 1 tick
        assertThat(MiningKinematicsTest.calculateTicksToBreak(zeroHardnessDmg)).isEqualTo(1);

        // Extreme tools (Eff 100 + Haste 255) must NOT violate UnbreakableFilter
        double hyperSpeed = MiningKinematicsTest.calculateEffectiveSpeed(
                MiningKinematicsTest.calculateToolSpeed(9.0, 100, true), 255, 0, false, false
        );
        assertThat(hyperSpeed).isEqualTo(520_520.0);
        // Bedrock still recognized as unbreakable
        assertThat(UnbreakableFilter.isUnbreakable(-1.0)).isTrue();
    }

    @Test
    @DisplayName("MiningKinematics Stress: Submerged in water and airborne 5x and 25x environmental penalties")
    void testMiningKinematicsEnvironmentalPenalties() {
        double diaEff5Speed = MiningKinematicsTest.calculateToolSpeed(8.0, 5, true); // 34.0
        double stoneHardness = 1.5;

        // Ground condition
        double groundSpeed = MiningKinematicsTest.calculateEffectiveSpeed(diaEff5Speed, 0, 0, false, false);
        assertThat(groundSpeed).isEqualTo(34.0);
        // damage = 34.0 / 45.0 = 0.7555... -> 2 ticks
        assertThat(MiningKinematicsTest.calculateTicksToBreak(MiningKinematicsTest.calculateDamagePerTick(groundSpeed, stoneHardness, true))).isEqualTo(2);

        // Submerged without Aqua Affinity (5x damper -> 6.8)
        double waterSpeed = MiningKinematicsTest.calculateEffectiveSpeed(diaEff5Speed, 0, 0, true, false);
        assertThat(waterSpeed).isCloseTo(6.8, within(1e-4));
        // damage = 6.8 / 45.0 = 0.1511... -> 7 ticks
        assertThat(MiningKinematicsTest.calculateTicksToBreak(MiningKinematicsTest.calculateDamagePerTick(waterSpeed, stoneHardness, true))).isEqualTo(7);

        // Airborne (5x damper -> 6.8)
        double airSpeed = MiningKinematicsTest.calculateEffectiveSpeed(diaEff5Speed, 0, 0, false, true);
        assertThat(airSpeed).isCloseTo(6.8, within(1e-4));
        assertThat(MiningKinematicsTest.calculateTicksToBreak(MiningKinematicsTest.calculateDamagePerTick(airSpeed, stoneHardness, true))).isEqualTo(7);

        // Submerged without Aqua Affinity AND Airborne (25x damper -> 1.36)
        double waterAndAirSpeed = MiningKinematicsTest.calculateEffectiveSpeed(diaEff5Speed, 0, 0, true, true);
        assertThat(waterAndAirSpeed).isCloseTo(1.36, within(1e-4));
        // damage = 1.36 / 45.0 = 0.03022... -> 34 ticks
        assertThat(MiningKinematicsTest.calculateTicksToBreak(MiningKinematicsTest.calculateDamagePerTick(waterAndAirSpeed, stoneHardness, true))).isEqualTo(34);

        // Instamine cancellation under water / air:
        // Netherite Eff 5 + Haste 2 on Stone: Ground speed = 49.0 >= 45.0 -> Instamines!
        double nethEff5Haste2Ground = MiningKinematicsTest.calculateEffectiveSpeed(35.0, 2, 0, false, false);
        assertThat(MiningKinematicsTest.isInstamine(nethEff5Haste2Ground, stoneHardness, true)).isTrue();

        // In water without Aqua Affinity: speed = 49.0 / 5.0 = 9.8 < 45.0 -> CANNOT instamine
        double nethEff5Haste2Water = MiningKinematicsTest.calculateEffectiveSpeed(35.0, 2, 0, true, false);
        assertThat(MiningKinematicsTest.isInstamine(nethEff5Haste2Water, stoneHardness, true)).isFalse();

        // Airborne: speed = 49.0 / 5.0 = 9.8 < 45.0 -> CANNOT instamine
        double nethEff5Haste2Air = MiningKinematicsTest.calculateEffectiveSpeed(35.0, 2, 0, false, true);
        assertThat(MiningKinematicsTest.isInstamine(nethEff5Haste2Air, stoneHardness, true)).isFalse();
    }

    // =========================================================================
    // 4. HIGH CONCURRENCY STRESS TEST (50 CONCURRENT THREADS)
    // =========================================================================

    @Test
    @DisplayName("Concurrency Stress: 50 concurrent threads instantiating, querying, and updating module settings")
    void testHighConcurrencyPlayerModuleCatalogAndSettings() throws Exception {
        int threadCount = 50;
        int operationsPerThread = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        EventBus eventBus = new EventBus();
        Path tempConfigFile = Files.createTempFile("player-modules-stress", ".json");
        tempConfigFile.toFile().deleteOnExit();

        ModuleManager manager = new ModuleManager(eventBus, tempConfigFile);
        PlayerModuleCatalog.registerAll(manager);

        List<Module> sampleModules = PlayerModuleCatalog.createAllInstances();
        assertThat(sampleModules).hasSize(90);

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);
        AtomicBoolean failureDetected = new AtomicBoolean(false);
        AtomicInteger totalOps = new AtomicInteger(0);
        List<Throwable> exceptions = new CopyOnWriteArrayList<>();

        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            executor.submit(() -> {
                try {
                    startLatch.await(); // Synchronized start

                    for (int op = 0; op < operationsPerThread; op++) {
                        int branch = (threadId + op) % 6;
                        switch (branch) {
                            case 0: {
                                // Concurrent catalog instance creation
                                List<Module> freshInstances = PlayerModuleCatalog.createAllInstances();
                                if (freshInstances.size() != 90) {
                                    throw new IllegalStateException("Fresh instance count was " + freshInstances.size());
                                }
                                break;
                            }
                            case 1: {
                                // Query module by ID and Class
                                Module randomModule = sampleModules.get((threadId * 7 + op) % sampleModules.size());
                                Module retrieved = manager.getModule(randomModule.getId());
                                if (retrieved == null) {
                                    throw new IllegalStateException("Module not found: " + randomModule.getId());
                                }
                                NetherPortalSync nps = manager.getModule(NetherPortalSync.class);
                                SlimeChunkMatrix scm = manager.getModule(SlimeChunkMatrix.class);
                                FastBreak fb = manager.getModule(FastBreak.class);
                                if (nps == null || scm == null || fb == null) {
                                    throw new IllegalStateException("Core player module not found by class");
                                }
                                break;
                            }
                            case 2: {
                                // Query category modules and iterate
                                List<Module> playerModules = manager.getModulesByCategory(Category.PLAYER);
                                if (playerModules.size() != 90) {
                                    throw new IllegalStateException("Category.PLAYER size mismatch: " + playerModules.size());
                                }
                                for (Module m : playerModules) {
                                    if (m.getId() == null) {
                                        throw new IllegalStateException("Null module ID in category iteration");
                                    }
                                }
                                break;
                            }
                            case 3: {
                                // Query and mutate module settings concurrently
                                Module mod = sampleModules.get(op % sampleModules.size());
                                for (Setting<?> setting : mod.getSettings()) {
                                    if (setting instanceof BooleanSetting) {
                                        BooleanSetting bs = (BooleanSetting) setting;
                                        bs.setValue(op % 2 == 0);
                                        bs.toggle();
                                    } else if (setting instanceof NumberSetting) {
                                        NumberSetting ns = (NumberSetting) setting;
                                        double val = ns.getMin() + (op % 10);
                                        ns.setValue(Math.min(val, ns.getMax()));
                                    } else if (setting instanceof ModeSetting) {
                                        ModeSetting ms = (ModeSetting) setting;
                                        List<String> modes = ms.getModes();
                                        if (!modes.isEmpty()) {
                                            ms.setValue(modes.get(op % modes.size()));
                                        }
                                    } else if (setting instanceof ColorSetting) {
                                        ColorSetting cs = (ColorSetting) setting;
                                        cs.setValue(0xFF000000 | (op & 0xFFFFFF));
                                    }
                                }
                                break;
                            }
                            case 4: {
                                // Concurrent listener registration and trigger
                                Module mod = sampleModules.get((op * 3) % sampleModules.size());
                                Setting<?> anySetting = mod.getSettings().get(0);
                                anySetting.onChange(val -> {
                                    // Empty listener to test thread-safe listeners list
                                });
                                anySetting.reset();
                                break;
                            }
                            case 5: {
                                // Concurrent registration deduplication check
                                Module candidate = sampleModules.get(op % sampleModules.size());
                                manager.register(candidate);
                                break;
                            }
                        }
                        totalOps.incrementAndGet();
                    }
                } catch (Throwable ex) {
                    failureDetected.set(true);
                    exceptions.add(ex);
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        // Fire start gun
        startLatch.countDown();

        boolean finished = doneLatch.await(30, TimeUnit.SECONDS);
        executor.shutdownNow();

        assertThat(finished).as("All 50 threads must terminate within 30s timeout").isTrue();
        assertThat(exceptions).as("Exceptions occurred during high concurrency stress testing").isEmpty();
        assertThat(failureDetected.get()).isFalse();
        assertThat(totalOps.get()).isEqualTo(threadCount * operationsPerThread);

        // Verify manager integrity remained uncorrupted
        assertThat(manager.getModules().size()).isEqualTo(90);
        assertThat(manager.getModulesByCategory(Category.PLAYER).size()).isEqualTo(90);

        // Verify config save under post-stress state
        manager.saveConfig();
        assertThat(Files.exists(tempConfigFile)).isTrue();
        assertThat(Files.size(tempConfigFile)).isGreaterThan(1000);
    }

    @Test
    @DisplayName("Concurrency Stress: 20 simultaneous independent ModuleManagers registering PlayerModuleCatalog")
    void testConcurrentIndependentModuleManagers() throws Exception {
        int threads = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);
        List<Throwable> errors = new CopyOnWriteArrayList<>();

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    EventBus bus = new EventBus();
                    ModuleManager mm = new ModuleManager(bus);
                    PlayerModuleCatalog.registerAll(mm);

                    assertThat(mm.getModules()).hasSize(90);
                    assertThat(mm.getModulesByCategory(Category.PLAYER)).hasSize(90);

                    for (Module m : PlayerModuleCatalog.createAllInstances()) {
                        assertThat(mm.getModule(m.getId())).isNotNull();
                        assertThat(m.getSettings()).isNotEmpty();
                    }
                } catch (Throwable t) {
                    errors.add(t);
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean complete = latch.await(20, TimeUnit.SECONDS);
        executor.shutdownNow();

        assertThat(complete).isTrue();
        assertThat(errors).isEmpty();
    }
}
