package com.ballsclient.core.module.player.world;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ChunkBorderDisplay — Sub-chunk 16x16 grid boundary math and farm loading alignment.
 */
public class ChunkBorderDisplay extends Module {
    private final BooleanSetting showSubChunk = new BooleanSetting("ShowSubChunk", "Display 16x16x16 sub-chunk partitions", true);
    private final ColorSetting color = new ColorSetting("Color", "Grid boundary line color", 0xFF55FF55);
    private final NumberSetting radiusChunks = new NumberSetting("RadiusChunks", "Visible chunk boundary radius", 2.0, 1.0, 8.0, 1.0);
    private final BooleanSetting highlightCenter = new BooleanSetting("HighlightCenter", "Highlight center 8x8 voxel area", true);

    public ChunkBorderDisplay() {
        super("chunk-border-display", "Chunk Border Display", "Sub-chunk 16x16 grid boundary math and farm loading alignment", Category.PLAYER);
        addSettings(showSubChunk, color, radiusChunks, highlightCenter);
    }

    public BooleanSetting getShowSubChunk() {
        return showSubChunk;
    }

    public ColorSetting getColor() {
        return color;
    }

    public NumberSetting getRadiusChunks() {
        return radiusChunks;
    }

    public BooleanSetting getHighlightCenter() {
        return highlightCenter;
    }

    public static int toChunkCoord(int blockCoord) {
        return Math.floorDiv(blockCoord, 16);
    }

    public static int toSubChunkCoord(int blockY) {
        return Math.floorDiv(blockY, 16);
    }

    public static boolean isChunkEdge(int blockX, int blockZ) {
        int remX = Math.floorMod(blockX, 16);
        int remZ = Math.floorMod(blockZ, 16);
        return (remX == 0 || remX == 15) || (remZ == 0 || remZ == 15);
    }
}
