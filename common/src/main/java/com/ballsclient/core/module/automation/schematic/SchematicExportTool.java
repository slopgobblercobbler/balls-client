package com.ballsclient.core.module.automation.schematic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.automation.model.LitematicaBitPackingParser;
import com.ballsclient.core.module.automation.model.SchematicVolume;
import com.ballsclient.core.module.automation.model.SpongeVarIntParser;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * SchematicExportTool — Selects world corners and serializes player-built structures into .litematic or .schem.
 */
public class SchematicExportTool extends Module {
    private final ModeSetting exportFormat = new ModeSetting("ExportFormat", "Serialization format", "Litematica", "Litematica", "SpongeV2");
    private final BooleanSetting includeTileEntities = new BooleanSetting("IncludeTileEntities", "Serializes block entity NBT compound data", true);

    public SchematicExportTool() {
        super("schematic-export-tool", "Schematic Export Tool", "Selects two world corners and serializes player-built structures into .litematic format", Category.AUTOMATION);
        addSettings(exportFormat, includeTileEntities);
    }

    public ModeSetting getExportFormat() {
        return exportFormat;
    }

    public BooleanSetting getIncludeTileEntities() {
        return includeTileEntities;
    }

    public static byte[] exportToSpongeVarInt(SchematicVolume volume) {
        if (volume == null) return new byte[0];
        return SpongeVarIntParser.encodeAll(volume.getBlockIndices());
    }

    public static long[] exportToLitematicaBitStream(SchematicVolume volume) {
        if (volume == null) return new long[0];
        int bits = LitematicaBitPackingParser.calculateBitsPerBlock(volume.getPalette().size());
        return LitematicaBitPackingParser.pack(volume.getBlockIndices(), bits);
    }
}
