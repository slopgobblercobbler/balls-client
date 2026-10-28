package com.ballsclient.core.module.automation.schematic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.automation.model.SchematicVolume;
import com.ballsclient.core.module.automation.model.SpongeVarIntParser;
import com.ballsclient.core.setting.NumberSetting;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * SpongeSchematicViewer — Decodes and renders Sponge v2/v3 .schem files with LEB128 VarInt streaming.
 */
public class SpongeSchematicViewer extends Module {
    private final NumberSetting renderDistance = new NumberSetting("RenderDistance", "Hologram culling distance in blocks", 48.0, 8.0, 128.0, 8.0);
    private final NumberSetting ghostAlpha = new NumberSetting("GhostAlpha", "Ghost block opacity level", 0.6, 0.1, 1.0, 0.05);

    public SpongeSchematicViewer() {
        super("sponge-schematic-viewer", "Sponge Schematic Viewer", "Decodes and renders Sponge v2/v3 .schem files with LEB128 VarInt byte streaming", Category.AUTOMATION);
        addSettings(renderDistance, ghostAlpha);
    }

    public NumberSetting getRenderDistance() {
        return renderDistance;
    }

    public NumberSetting getGhostAlpha() {
        return ghostAlpha;
    }

    public static SchematicVolume parseSpongeByteArray(byte[] varIntData, Map<String, Integer> paletteMap, int width, int height, int length) {
        int totalBlocks = width * height * length;
        ByteBuffer buffer = ByteBuffer.wrap(varIntData != null ? varIntData : new byte[0]);
        int[] indices = SpongeVarIntParser.readAll(buffer, totalBlocks);

        int maxIdx = 0;
        if (paletteMap != null) {
            for (int v : paletteMap.values()) {
                if (v > maxIdx) maxIdx = v;
            }
        }
        List<String> paletteList = new ArrayList<>(Collections.nCopies(maxIdx + 1, "minecraft:air"));
        if (paletteMap != null) {
            for (Map.Entry<String, Integer> entry : paletteMap.entrySet()) {
                if (entry.getValue() >= 0 && entry.getValue() < paletteList.size()) {
                    paletteList.set(entry.getValue(), entry.getKey());
                }
            }
        }

        return new SchematicVolume(width, height, length, paletteList, indices);
    }
}
