package com.ballsclient.core.module.player.world;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * StructureBoundingBox — Fortress, Witch Hut, and Monument bounding volume calculators.
 */
public class StructureBoundingBox extends Module {
    private final ModeSetting structureType = new ModeSetting("StructureType", "Type of world structure", "NetherFortress", "NetherFortress", "WitchHut", "OceanMonument", "PillagerOutpost");
    private final BooleanSetting showBoundingBoxes = new BooleanSetting("ShowBoundingBoxes", "Render 3D AABB bounding boxes", true);
    private final BooleanSetting highlightCrossroads = new BooleanSetting("HighlightCrossroads", "Highlight fortress wither skeleton intersections", true);

    public StructureBoundingBox() {
        super("structure-bounding-box", "Structure Bounding Box", "Fortress, Witch Hut, and Monument bounding volume calculators", Category.PLAYER);
        addSettings(structureType, showBoundingBoxes, highlightCrossroads);
    }

    public ModeSetting getStructureType() {
        return structureType;
    }

    public BooleanSetting getShowBoundingBoxes() {
        return showBoundingBoxes;
    }

    public BooleanSetting getHighlightCrossroads() {
        return highlightCrossroads;
    }

    public static boolean isInsideBounds(double x, double y, double z, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
    }

    public static boolean isWitchHutSpawningVoxel(double x, double y, double z, double hutMinX, double hutMinY, double hutMinZ) {
        // Vanilla witch hut bounding box is 7x9x7 (X: hutMinX to +7, Y: hutMinY to +4 spawning floors, Z: hutMinZ to +9)
        return isInsideBounds(x, y, z, hutMinX, hutMinY, hutMinZ, hutMinX + 7.0, hutMinY + 4.0, hutMinZ + 9.0);
    }
}
