package com.ballsclient.core.module.player.world;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.Random;

/**
 * SlimeChunkMatrix — Minecraft Java PRNG slime chunk formula evaluator below Y=40.
 */
public class SlimeChunkMatrix extends Module {
    private final NumberSetting worldSeed = new NumberSetting("WorldSeed", "World generation seed", 0.0, -9223372036854775808.0, 9223372036854775807.0, 1.0);
    private final NumberSetting maxRenderRadius = new NumberSetting("MaxRenderRadius", "Rendering chunk radius", 8.0, 1.0, 16.0, 1.0);
    private final BooleanSetting checkYElevation = new BooleanSetting("CheckYElevation", "Restrict spawning verification to elevation < 40", true);
    private final NumberSetting maxElevation = new NumberSetting("MaxElevation", "Maximum Y coordinate threshold for slime spawn", 40.0, -64.0, 320.0, 1.0);

    public SlimeChunkMatrix() {
        super("slime-chunk-matrix", "Slime Chunk Matrix", "Minecraft Java PRNG slime chunk formula evaluator below Y=40", Category.PLAYER);
        addSettings(worldSeed, maxRenderRadius, checkYElevation, maxElevation);
    }

    public NumberSetting getWorldSeed() {
        return worldSeed;
    }

    public NumberSetting getMaxRenderRadius() {
        return maxRenderRadius;
    }

    public BooleanSetting getCheckYElevation() {
        return checkYElevation;
    }

    public NumberSetting getMaxElevation() {
        return maxElevation;
    }

    public static boolean isSlimeChunk(long seed, int chunkX, int chunkZ) {
        long chunkSeed = (seed +
                (long) (chunkX * chunkX * 0x4c1906) +
                (long) (chunkX * 0x5ac0db) +
                (long) (chunkZ * chunkZ) * 0x4307a7L +
                (long) (chunkZ * 0x5f24f)) ^ 0x3ad8025fL;
        Random rnd = new Random(chunkSeed);
        return rnd.nextInt(10) == 0;
    }

    public static int toChunkCoord(int blockCoord) {
        return Math.floorDiv(blockCoord, 16);
    }

    public static boolean canSpawnSlime(long seed, int chunkX, int chunkZ, double y) {
        if (y >= 40.0 || y < -64.0) {
            return false;
        }
        return isSlimeChunk(seed, chunkX, chunkZ);
    }

    public static boolean canSlimeSpawn(long seed, int chunkX, int chunkZ, double y, boolean checkElevation, double maxElevation) {
        if (!isSlimeChunk(seed, chunkX, chunkZ)) {
            return false;
        }
        if (checkElevation) {
            return y < maxElevation && y >= -64.0;
        }
        return true;
    }
}
