package com.ballsclient.core.module.automation.schematic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.automation.model.SchematicVolume;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * SchematicPaletteSwapper — Replaces all occurrences of a block state in the schematic palette with an alternate block.
 */
public class SchematicPaletteSwapper extends Module {
    private final BooleanSetting preserveProperties = new BooleanSetting("PreserveProperties", "Retains orientation properties when swapping block types", true);

    public SchematicPaletteSwapper() {
        super("schematic-palette-swapper", "Schematic Palette Swapper", "Replaces all occurrences of a block state in the schematic palette with an alternate block", Category.AUTOMATION);
        addSettings(preserveProperties);
    }

    public BooleanSetting getPreserveProperties() {
        return preserveProperties;
    }

    public static SchematicVolume swapPaletteEntry(SchematicVolume original, String sourceId, String targetId) {
        if (original == null || sourceId == null || targetId == null) {
            return original;
        }
        List<String> newPalette = new ArrayList<>(original.getPalette());
        for (int i = 0; i < newPalette.size(); i++) {
            if (newPalette.get(i).equalsIgnoreCase(sourceId)) {
                newPalette.set(i, targetId);
            }
        }
        return new SchematicVolume(original.getWidth(), original.getHeight(), original.getLength(), newPalette, original.getBlockIndices());
    }
}
