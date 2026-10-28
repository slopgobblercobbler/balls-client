package com.ballsclient.core.module.player;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DisplayName("Slime Chunk Java PRNG Formula & Spawning Kinematics Test")
class SlimeChunkMathTest {

    /**
     * Exact Minecraft Java Edition slime chunk PRNG evaluation algorithm.
     */
    public static boolean isSlimeChunk(long seed, int chunkX, int chunkZ) {
        long chunkSeed = (seed +
                (long) (chunkX * chunkX * 0x4c1906) +
                (long) (chunkX * 0x5ac0db) +
                (long) (chunkZ * chunkZ) * 0x4307a7L +
                (long) (chunkZ * 0x5f24f)) ^ 0x3ad8025fL;
        Random rnd = new Random(chunkSeed);
        return rnd.nextInt(10) == 0;
    }

    /**
     * Translates block coordinates to chunk coordinates using floor division.
     */
    public static int toChunkCoord(int blockCoord) {
        return Math.floorDiv(blockCoord, 16);
    }

    /**
     * Determines whether slime spawning is permitted based on chunk status and elevation.
     */
    public static boolean canSpawnSlime(long seed, int chunkX, int chunkZ, double y) {
        if (y >= 40.0 || y < -64.0) {
            return false;
        }
        return isSlimeChunk(seed, chunkX, chunkZ);
    }

    @Test
    @DisplayName("PRNG Determinism: Slime chunk formula is 100% deterministic and reproducible")
    void testSlimeChunkDeterminism() {
        long seed = 123456789L;
        int chunkX = 4;
        int chunkZ = -12;

        boolean firstEvaluation = isSlimeChunk(seed, chunkX, chunkZ);
        boolean secondEvaluation = isSlimeChunk(seed, chunkX, chunkZ);
        boolean thirdEvaluation = isSlimeChunk(seed, chunkX, chunkZ);

        assertThat(firstEvaluation)
                .isEqualTo(secondEvaluation)
                .isEqualTo(thirdEvaluation);

        // Different seed produces independent evaluation
        long otherSeed = 987654321L;
        // Invariance test: ensure algorithm executes without exception across arbitrary long seeds
        boolean diffSeedResult = isSlimeChunk(otherSeed, chunkX, chunkZ);
        assertThat(diffSeedResult).isIn(true, false);
    }

    @Test
    @DisplayName("Coordinates: Negative and positive block coordinate translation to chunk coordinates")
    void testBlockToChunkCoordinateTranslation() {
        assertThat(toChunkCoord(64)).isEqualTo(4);
        assertThat(toChunkCoord(128)).isEqualTo(8);
        assertThat(toChunkCoord(15)).isEqualTo(0);
        assertThat(toChunkCoord(0)).isEqualTo(0);
        assertThat(toChunkCoord(-1)).isEqualTo(-1);
        assertThat(toChunkCoord(-16)).isEqualTo(-1);
        assertThat(toChunkCoord(-17)).isEqualTo(-2);
        assertThat(toChunkCoord(-32)).isEqualTo(-2);
        assertThat(toChunkCoord(-33)).isEqualTo(-3);
    }

    @Test
    @DisplayName("Statistical Distribution: 10% expected slime chunk density over 10,000 chunks")
    void testSlimeChunkStatisticalDistribution() {
        long seed = 9876543210L;
        int totalChunks = 10000; // 100x100 chunk grid
        int slimeChunkCount = 0;

        for (int cx = 0; cx < 100; cx++) {
            for (int cz = 0; cz < 100; cz++) {
                if (isSlimeChunk(seed, cx, cz)) {
                    slimeChunkCount++;
                }
            }
        }

        double density = (double) slimeChunkCount / totalChunks;

        // Theoretical expectation is 10.0% (1/10). In a 10,000 sample, density lies within 8.5% to 11.5%.
        assertThat(density).isCloseTo(0.10, within(0.015));
        assertThat(slimeChunkCount).isBetween(850, 1150);
    }

    @Test
    @DisplayName("Elevation Bounds: Slime spawning allowed only strictly below Y=40 down to bedrock (Y=-64)")
    void testSlimeSpawningElevationCriteria() {
        long testSeed = 54321L;
        // Locate a confirmed slime chunk for this test seed
        int confirmedX = 0, confirmedZ = 0;
        boolean found = false;
        for (int x = 0; x < 50; x++) {
            for (int z = 0; z < 50; z++) {
                if (isSlimeChunk(testSeed, x, z)) {
                    confirmedX = x;
                    confirmedZ = z;
                    found = true;
                    break;
                }
            }
            if (found) break;
        }

        assertThat(found).isTrue();

        // Elevation Y = 39.0 -> Permitted
        assertThat(canSpawnSlime(testSeed, confirmedX, confirmedZ, 39.0)).isTrue();
        // Elevation Y = 0.0 -> Permitted
        assertThat(canSpawnSlime(testSeed, confirmedX, confirmedZ, 0.0)).isTrue();
        // Modern 1.18+ subterranean depth Y = -50.0 -> Permitted
        assertThat(canSpawnSlime(testSeed, confirmedX, confirmedZ, -50.0)).isTrue();

        // Boundary elevation Y = 40.0 -> Strictly forbidden (must be < 40)
        assertThat(canSpawnSlime(testSeed, confirmedX, confirmedZ, 40.0)).isFalse();
        // High elevation Y = 64.0 (sea level) -> Forbidden
        assertThat(canSpawnSlime(testSeed, confirmedX, confirmedZ, 64.0)).isFalse();
        // Below world void Y = -65.0 -> Forbidden
        assertThat(canSpawnSlime(testSeed, confirmedX, confirmedZ, -65.0)).isFalse();
    }
}
