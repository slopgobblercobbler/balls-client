package com.ballsclient.core.module.render.esp;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;

/**
 * Visualizes loaded vs unloaded chunk boundaries and sub-chunk render distance limits.
 */
public class SubChunkMeshAudit extends Module {
    private final BooleanSetting showUnloadedEdges = new BooleanSetting("UnloadedEdges", "Highlight perimeter of loaded chunk radius", true);
    private final ColorSetting edgeColor = new ColorSetting("EdgeColor", "Perimeter boundary wireframe color", 0xFFFF3333);

    private int renderDistanceChunks = 12;

    public SubChunkMeshAudit() {
        super("subchunk-mesh-audit", "Sub-Chunk Mesh Audit", "Visualizes loaded chunk boundaries and render distance borders", Category.RENDER);
        addSettings(showUnloadedEdges, edgeColor);
    }

    public void setRenderDistanceChunks(int chunks) {
        this.renderDistanceChunks = Math.max(2, Math.min(64, chunks));
    }

    public int getRenderDistanceChunks() {
        return renderDistanceChunks;
    }

    public boolean isShowUnloadedEdges() {
        return showUnloadedEdges.getValue();
    }

    public int getEdgeColor() {
        return edgeColor.getValue();
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render subchunk mesh boundaries
    }
}
