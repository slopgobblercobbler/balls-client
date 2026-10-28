package com.ballsclient.core.module.automation.schematic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.automation.model.LitematicaBitPackingParser;
import com.ballsclient.core.module.automation.model.SchematicVolume;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.List;

/**
 * LitematicaViewer — 3D volumetric preview of loaded .litematic files with continuous 64-bit word spanning.
 */
public class LitematicaViewer extends Module {
    private final NumberSetting renderDistance = new NumberSetting("RenderDistance", "Hologram culling distance in blocks", 48.0, 8.0, 128.0, 8.0);
    private final NumberSetting ghostAlpha = new NumberSetting("GhostAlpha", "Ghost block opacity level", 0.6, 0.1, 1.0, 0.05);
    private final BooleanSetting highlightAir = new BooleanSetting("HighlightAir", "Renders ghost outlines for required air blocks", false);

    public LitematicaViewer() {
        super("litematica-viewer", "Litematica Viewer", "3D volumetric preview of loaded .litematic files with multi-region offset rendering", Category.AUTOMATION);
        addSettings(renderDistance, ghostAlpha, highlightAir);
    }

    public NumberSetting getRenderDistance() {
        return renderDistance;
    }

    public NumberSetting getGhostAlpha() {
        return ghostAlpha;
    }

    public BooleanSetting getHighlightAir() {
        return highlightAir;
    }

    public static SchematicVolume loadFromBitStream(long[] blockStates, List<String> palette, int width, int height, int length) {
        int bitsPerBlock = LitematicaBitPackingParser.calculateBitsPerBlock(palette != null ? palette.size() : 1);
        int totalBlocks = width * height * length;
        int[] indices = LitematicaBitPackingParser.unpack(blockStates, totalBlocks, bitsPerBlock);
        return new SchematicVolume(width, height, length, palette, indices);
    }
}
