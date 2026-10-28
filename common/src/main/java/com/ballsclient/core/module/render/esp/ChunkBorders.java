package com.ballsclient.core.module.render.esp;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;

/**
 * 3D visual wireframe grid highlighting chunk (16x16) and sub-chunk (16x16x16) boundaries.
 */
public class ChunkBorders extends Module {
    private final BooleanSetting showSubChunks = new BooleanSetting("SubChunks", "Render 16x16x16 sub-chunk vertical sections", true);
    private final ColorSetting chunkColor = new ColorSetting("ChunkColor", "Primary chunk boundary color (Gold)", 0xFFFFD700);
    private final ColorSetting subChunkColor = new ColorSetting("SubChunkColor", "Sub-chunk subdivision color (Cyan)", 0x8000FFFF);

    private int currentChunkX = 0;
    private int currentChunkZ = 0;

    public ChunkBorders() {
        super("chunk-borders", "Chunk Borders", "Renders 3D grid lines displaying chunk and sub-chunk boundaries", Category.RENDER);
        addSettings(showSubChunks, chunkColor, subChunkColor);
    }

    public void updatePlayerChunk(int blockX, int blockZ) {
        this.currentChunkX = blockX >> 4;
        this.currentChunkZ = blockZ >> 4;
    }

    public int getChunkMinX() { return currentChunkX << 4; }
    public int getChunkMaxX() { return (currentChunkX << 4) + 16; }
    public int getChunkMinZ() { return currentChunkZ << 4; }
    public int getChunkMaxZ() { return (currentChunkZ << 4) + 16; }

    public boolean isShowSubChunks() { return showSubChunks.getValue(); }
    public int getChunkColor() { return chunkColor.getValue(); }
    public int getSubChunkColor() { return subChunkColor.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render chunk wireframe grid
    }
}
