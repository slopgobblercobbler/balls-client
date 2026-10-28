package com.ballsclient.core.module.automation.schematic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.automation.model.SchematicVolume;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.function.BiFunction;

/**
 * LayerByLayerPrinter — Restricts ghost hologram rendering and placement guide to a single horizontal Y-slice.
 */
public class LayerByLayerPrinter extends Module {
    private final NumberSetting activeLayer = new NumberSetting("ActiveLayer", "Current horizontal layer Y index", 0.0, 0.0, 256.0, 1.0);
    private final ModeSetting layerMode = new ModeSetting("LayerMode", "Slicing render mode", "SingleLayer", "SingleLayer", "AllBelow", "LayerRange");
    private final BooleanSetting autoAdvance = new BooleanSetting("AutoAdvance", "Advances layer when all blocks in layer are placed", true);

    public LayerByLayerPrinter() {
        super("layer-by-layer-printer", "Layer By Layer Printer", "Restricts ghost hologram rendering and placement guide to a single horizontal Y-slice", Category.AUTOMATION);
        addSettings(activeLayer, layerMode, autoAdvance);
    }

    public NumberSetting getActiveLayer() {
        return activeLayer;
    }

    public ModeSetting getLayerMode() {
        return layerMode;
    }

    public BooleanSetting getAutoAdvance() {
        return autoAdvance;
    }

    public static boolean isLayerComplete(SchematicVolume volume, int layerY, BiFunction<Integer, Integer, String> worldBlockAtXZ) {
        if (volume == null || layerY < 0 || layerY >= volume.getHeight() || worldBlockAtXZ == null) {
            return false;
        }
        for (int z = 0; z < volume.getLength(); z++) {
            for (int x = 0; x < volume.getWidth(); x++) {
                String schemBlock = volume.getBlockState(x, layerY, z);
                if (schemBlock.equals("minecraft:air") || schemBlock.equals("air")) {
                    continue;
                }
                String worldBlock = worldBlockAtXZ.apply(x, z);
                if (worldBlock == null || !worldBlock.equalsIgnoreCase(schemBlock)) {
                    return false;
                }
            }
        }
        return true;
    }
}
