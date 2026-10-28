package com.ballsclient.core.module.automation.schematic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.automation.model.SchematicVolume;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * AutoSchematicAligner — Automatically snaps schematic origin to detected corner blocks or player target raycast.
 */
public class AutoSchematicAligner extends Module {
    public record BlockPos(int x, int y, int z) {}

    private final ModeSetting alignmentMode = new ModeSetting("AlignmentMode", "Snapping strategy", "PlayerRaycast", "PlayerRaycast", "CornerBlockMatch", "Centroid");
    private final BooleanSetting snapToGrid = new BooleanSetting("SnapToGrid", "Constrains coordinates strictly to whole integer block grid", true);

    public AutoSchematicAligner() {
        super("auto-schematic-aligner", "Auto Schematic Aligner", "Automatically snaps schematic origin to detected corner blocks or player target raycast", Category.AUTOMATION);
        addSettings(alignmentMode, snapToGrid);
    }

    public ModeSetting getAlignmentMode() {
        return alignmentMode;
    }

    public BooleanSetting getSnapToGrid() {
        return snapToGrid;
    }

    public static BlockPos computeAnchorPosition(double hitX, double hitY, double hitZ, SchematicVolume volume, String mode) {
        int x = (int) Math.floor(hitX);
        int y = (int) Math.floor(hitY);
        int z = (int) Math.floor(hitZ);

        if (volume == null) {
            return new BlockPos(x, y, z);
        }

        if ("Centroid".equalsIgnoreCase(mode)) {
            return new BlockPos(x - volume.getWidth() / 2, y, z - volume.getLength() / 2);
        }
        return new BlockPos(x, y, z);
    }
}
