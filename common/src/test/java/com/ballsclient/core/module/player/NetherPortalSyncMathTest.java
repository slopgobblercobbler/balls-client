package com.ballsclient.core.module.player;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DisplayName("Nether Portal Synchronization & Spatial Geometry Test")
class NetherPortalSyncMathTest {

    private static final double OVERWORLD_TO_NETHER_RATIO = 8.0;
    private static final double VANILLA_PORTAL_SEARCH_RADIUS = 128.0;

    /**
     * Converts Overworld coordinates to Nether destination coordinates with elevation clamping.
     */
    public static double[] toNether(double x, double y, double z, boolean clampCeiling) {
        double netherX = x / OVERWORLD_TO_NETHER_RATIO;
        double netherZ = z / OVERWORLD_TO_NETHER_RATIO;
        double netherY = y;
        if (clampCeiling) {
            netherY = Math.max(0.0, Math.min(127.0, y));
        }
        return new double[]{netherX, netherY, netherZ};
    }

    /**
     * Converts Nether coordinates to Overworld destination coordinates with 1.18+ world depth bounds.
     */
    public static double[] toOverworld(double x, double y, double z) {
        double overworldX = x * OVERWORLD_TO_NETHER_RATIO;
        double overworldZ = z * OVERWORLD_TO_NETHER_RATIO;
        double overworldY = Math.max(-64.0, Math.min(319.0, y));
        return new double[]{overworldX, overworldY, overworldZ};
    }

    /**
     * Computes 2D horizontal Euclidean distance between two points.
     */
    public static double horizontalDistance(double x1, double z1, double x2, double z2) {
        double dx = x1 - x2;
        double dz = z1 - z2;
        return Math.sqrt(dx * dx + dz * dz);
    }

    /**
     * Computes 3D Euclidean distance between two points.
     */
    public static double euclidean3D(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        double dz = z1 - z2;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Test
    @DisplayName("Scaling: Exact 8:1 dimensional coordinate mapping between Overworld and Nether")
    void testCoordinateScaling() {
        double owX = 800.0, owY = 64.0, owZ = -1600.0;
        double[] nether = toNether(owX, owY, owZ, true);

        assertThat(nether[0]).isEqualTo(100.0);
        assertThat(nether[1]).isEqualTo(64.0);
        assertThat(nether[2]).isEqualTo(-200.0);

        double[] backToOverworld = toOverworld(nether[0], nether[1], nether[2]);
        assertThat(backToOverworld[0]).isEqualTo(800.0);
        assertThat(backToOverworld[1]).isEqualTo(64.0);
        assertThat(backToOverworld[2]).isEqualTo(-1600.0);

        // Fractional precision verification
        double fracX = 123.456, fracY = 71.0, fracZ = -789.128;
        double[] fracNether = toNether(fracX, fracY, fracZ, true);
        assertThat(fracNether[0]).isCloseTo(15.432, within(0.001));
        assertThat(fracNether[1]).isEqualTo(71.0);
        assertThat(fracNether[2]).isCloseTo(-98.641, within(0.001));
    }

    @Test
    @DisplayName("Elevation: Nether bedrock ceiling (Y=127) and floor (Y=0) clamping")
    void testNetherElevationClamping() {
        // High Overworld mountain (Y=260) clamped to Nether build ceiling (Y=127)
        double[] highPortal = toNether(1000.0, 260.0, 1000.0, true);
        assertThat(highPortal[1]).isEqualTo(127.0);

        // Deep dark / deep underground Overworld Y=-50 clamped to Nether floor (Y=0)
        double[] deepPortal = toNether(1000.0, -50.0, 1000.0, true);
        assertThat(deepPortal[1]).isEqualTo(0.0);

        // Overworld height range clamping (-64 to 319)
        double[] highOverworld = toOverworld(100.0, 400.0, 100.0);
        assertThat(highOverworld[1]).isEqualTo(319.0);

        double[] lowOverworld = toOverworld(100.0, -100.0, 100.0);
        assertThat(lowOverworld[1]).isEqualTo(-64.0);
    }

    @Test
    @DisplayName("Search Radius: 128-block horizontal destination search window and optimal portal selection")
    void testPortalSearchRadiusAndSelection() {
        double targetX = 100.0, targetY = 64.0, targetZ = 200.0;

        // Candidate portals in destination dimension: {X, Y, Z}
        double[][] candidatePortals = new double[][]{
                {105.0, 64.0, 205.0}, // Candidate A: dx=5, dz=5 -> dist2D = 7.07m (In range)
                {180.0, 60.0, 200.0}, // Candidate B: dx=80, dz=0 -> dist2D = 80.0m (In range)
                {235.0, 70.0, 200.0}, // Candidate C: dx=135, dz=0 -> dist2D = 135.0m (> 128m, Out of range!)
                {102.0, 66.0, 198.0}  // Candidate D: dx=2, dz=-2 -> dist2D = 2.828m, dist3D = 3.464m (Best match!)
        };

        int bestIndex = -1;
        double minDistance3D = Double.MAX_VALUE;

        for (int i = 0; i < candidatePortals.length; i++) {
            double[] portal = candidatePortals[i];
            double dist2D = horizontalDistance(targetX, targetZ, portal[0], portal[2]);
            if (dist2D <= VANILLA_PORTAL_SEARCH_RADIUS) {
                double dist3D = euclidean3D(targetX, targetY, targetZ, portal[0], portal[1], portal[2]);
                if (dist3D < minDistance3D) {
                    minDistance3D = dist3D;
                    bestIndex = i;
                }
            }
        }

        // Candidate D (index 3) is closest
        assertThat(bestIndex).isEqualTo(3);
        assertThat(minDistance3D).isCloseTo(3.464, within(0.01));

        // Candidate C is verified out of range
        double outOfRangeDist = horizontalDistance(targetX, targetZ, candidatePortals[2][0], candidatePortals[2][2]);
        assertThat(outOfRangeDist).isGreaterThan(VANILLA_PORTAL_SEARCH_RADIUS);
    }

    @Test
    @DisplayName("Portal Desync: Calculates spatial drift and return targeting offset")
    void testBidirectionalDrift() {
        double originalOwX = 800.0, originalOwZ = 800.0;
        double exactNetherX = 100.0, exactNetherZ = 100.0;

        // If player builds Nether portal offset by 6 blocks in Nether
        double displacedNetherX = exactNetherX + 6.0;
        double displacedNetherZ = exactNetherZ;

        double[] returnOw = toOverworld(displacedNetherX, 64.0, displacedNetherZ);
        double driftBlocks = horizontalDistance(originalOwX, originalOwZ, returnOw[0], returnOw[2]);

        // 6 blocks in Nether translates to exactly 48 blocks drift in Overworld
        assertThat(driftBlocks).isEqualTo(48.0);
    }
}
