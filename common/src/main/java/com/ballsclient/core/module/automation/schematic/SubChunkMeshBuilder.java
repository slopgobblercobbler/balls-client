package com.ballsclient.core.module.automation.schematic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.automation.model.SchematicVolume;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * SubChunkMeshBuilder — Partitions large schematics into 16x16x16 sub-chunk bounding meshes for frustum culling.
 */
public class SubChunkMeshBuilder extends Module {
    public record SubChunkCoord(int chunkX, int chunkY, int chunkZ) {}

    private final NumberSetting chunkSize = new NumberSetting("ChunkSize", "Voxel bounding cubic partition dimension", 16.0, 8.0, 32.0, 8.0);
    private final BooleanSetting frustumCulling = new BooleanSetting("FrustumCulling", "Culls sub-meshes lying outside client camera view frustum", true);

    public SubChunkMeshBuilder() {
        super("sub-chunk-mesh-builder", "Sub-Chunk Mesh Builder", "Partitions large schematics into 16x16x16 sub-chunk bounding meshes", Category.AUTOMATION);
        addSettings(chunkSize, frustumCulling);
    }

    public NumberSetting getChunkSize() {
        return chunkSize;
    }

    public BooleanSetting getFrustumCulling() {
        return frustumCulling;
    }

    public static int calculateSubChunkCount(SchematicVolume volume, int size) {
        if (volume == null || size <= 0) return 0;
        int chunksX = (volume.getWidth() + size - 1) / size;
        int chunksY = (volume.getHeight() + size - 1) / size;
        int chunksZ = (volume.getLength() + size - 1) / size;
        return chunksX * chunksY * chunksZ;
    }
}
