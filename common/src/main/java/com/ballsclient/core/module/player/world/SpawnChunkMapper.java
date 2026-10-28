package com.ballsclient.core.module.player.world;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * SpawnChunkMapper — Calculates 19x19 / 23x23 entity-ticking and lazy chunk boundaries around world spawn.
 */
public class SpawnChunkMapper extends Module {
    private final NumberSetting spawnChunkRadius = new NumberSetting("SpawnChunkRadius", "Spawn chunk level radius", 11.0, 1.0, 19.0, 1.0);
    private final BooleanSetting highlightEntityTicking = new BooleanSetting("HighlightEntityTicking", "Highlight entity ticking center chunks", true);
    private final BooleanSetting highlightLazyChunks = new BooleanSetting("HighlightLazyChunks", "Highlight perimeter lazy loading chunks", true);

    public SpawnChunkMapper() {
        super("spawn-chunk-mapper", "Spawn Chunk Mapper", "Calculates 19x19 / 23x23 entity-ticking and lazy chunk boundaries around world spawn", Category.PLAYER);
        addSettings(spawnChunkRadius, highlightEntityTicking, highlightLazyChunks);
    }

    public NumberSetting getSpawnChunkRadius() {
        return spawnChunkRadius;
    }

    public BooleanSetting getHighlightEntityTicking() {
        return highlightEntityTicking;
    }

    public BooleanSetting getHighlightLazyChunks() {
        return highlightLazyChunks;
    }

    public static boolean isEntityTicking(int chunkX, int chunkZ, int spawnX, int spawnZ, int radius) {
        int dx = Math.abs(chunkX - spawnX);
        int dz = Math.abs(chunkZ - spawnZ);
        // Fully ticking chunks are within Chebyshev distance (radius - 1)
        return Math.max(dx, dz) <= Math.max(1, radius - 2);
    }

    public static boolean isLazyChunk(int chunkX, int chunkZ, int spawnX, int spawnZ, int radius) {
        int dx = Math.abs(chunkX - spawnX);
        int dz = Math.abs(chunkZ - spawnZ);
        int maxDist = Math.max(dx, dz);
        return maxDist > Math.max(1, radius - 2) && maxDist <= radius;
    }
}
