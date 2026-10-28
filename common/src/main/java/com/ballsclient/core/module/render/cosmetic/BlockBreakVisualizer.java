package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * Animated circular radial dial or progress bar displaying block mining destruction progress.
 */
public class BlockBreakVisualizer extends Module {
    private final ColorSetting progressColor = new ColorSetting("ProgressColor", "Mining progress bar color", 0xFFFF8800);
    private final ModeSetting style = new ModeSetting("Style", "Progress indicator geometry", "CIRCLE", "CIRCLE", "BAR", "OUTLINE");

    private float miningProgress = 0.0f; // 0.0 to 1.0
    private int blockX, blockY, blockZ;
    private boolean isMining = false;

    public BlockBreakVisualizer() {
        super("block-break-visualizer", "Block Break Visualizer", "Radial dial and animated progress bar showing mining progress", Category.RENDER);
        addSettings(progressColor, style);
    }

    public void updateMining(int x, int y, int z, float progress) {
        this.blockX = x;
        this.blockY = y;
        this.blockZ = z;
        this.miningProgress = Math.min(1.0f, Math.max(0.0f, progress));
        this.isMining = progress > 0.0f && progress < 1.0f;
    }

    public void stopMining() {
        this.isMining = false;
        this.miningProgress = 0.0f;
    }

    public float getMiningProgress() { return miningProgress; }
    public boolean isMining() { return isMining; }
    public int getBlockX() { return blockX; }
    public int getBlockY() { return blockY; }
    public int getBlockZ() { return blockZ; }
    public int getProgressColor() { return progressColor.getValue(); }
    public String getStyle() { return style.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render 3D radial break dial
    }
}
