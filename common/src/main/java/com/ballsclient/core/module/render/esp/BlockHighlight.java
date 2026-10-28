package com.ballsclient.core.module.render.esp;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * 3D bounding box outlines and translucent faces on currently targeted block.
 */
public class BlockHighlight extends Module {
    private final ModeSetting style = new ModeSetting("Style", "Highlight visual style", "BOTH", "OUTLINE", "FILLED", "BOTH");
    private final ColorSetting highlightColor = new ColorSetting("HighlightColor", "Target highlight tint", 0x8000FF00);
    private final NumberSetting pulseSpeed = new NumberSetting("PulseSpeed", "Color pulse frequency", 1.0, 0.0, 5.0, 0.5);

    private int targetBlockX = 0;
    private int targetBlockY = 0;
    private int targetBlockZ = 0;
    private boolean hasTarget = false;

    public BlockHighlight() {
        super("block-highlight", "Block Highlight", "Wireframe and filled face overlays on targeted block", Category.RENDER);
        addSettings(style, highlightColor, pulseSpeed);
    }

    public void setTargetBlock(int x, int y, int z) {
        this.targetBlockX = x;
        this.targetBlockY = y;
        this.targetBlockZ = z;
        this.hasTarget = true;
    }

    public void clearTarget() {
        this.hasTarget = false;
    }

    public boolean hasTarget() { return hasTarget; }
    public int getTargetBlockX() { return targetBlockX; }
    public int getTargetBlockY() { return targetBlockY; }
    public int getTargetBlockZ() { return targetBlockZ; }

    public int getPulsedColor(long timeMillis) {
        double speed = pulseSpeed.getValue();
        if (speed <= 0.0) return highlightColor.getValue();
        double sin = Math.sin((timeMillis * 0.003) * speed);
        float pulseFactor = (float) (0.5 + 0.5 * sin);
        int base = highlightColor.getValue();
        int a = (int) (highlightColor.getAlpha() * pulseFactor);
        return (a << 24) | (base & 0x00FFFFFF);
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render block highlight
    }
}
