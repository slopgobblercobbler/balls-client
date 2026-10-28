package com.ballsclient.core.module.automation.schematic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * GhostPlacementOverlay — Translucent holographic ghost block overlay guiding player block placement.
 */
public class GhostPlacementOverlay extends Module {
    public enum BlockPlacementStatus {
        CORRECT,
        MISSING,
        MISMATCHED,
        EXTRA_BLOCK
    }

    private final ModeSetting renderMode = new ModeSetting("RenderMode", "Holographic display technique", "FullBlock", "FullBlock", "Wireframe", "Points");
    private final ColorSetting matchColor = new ColorSetting("MatchColor", "Correctly placed block tint", 0x5500FF00);
    private final ColorSetting mismatchColor = new ColorSetting("MismatchColor", "Incorrectly placed block tint", 0x55FF0000);

    public GhostPlacementOverlay() {
        super("ghost-placement-overlay", "Ghost Placement Overlay", "Translucent holographic ghost block overlay guiding player block placement", Category.AUTOMATION);
        addSettings(renderMode, matchColor, mismatchColor);
    }

    public ModeSetting getRenderMode() {
        return renderMode;
    }

    public ColorSetting getMatchColor() {
        return matchColor;
    }

    public ColorSetting getMismatchColor() {
        return mismatchColor;
    }

    public static BlockPlacementStatus evaluatePosition(String worldBlock, String schematicBlock) {
        boolean worldEmpty = worldBlock == null || worldBlock.equals("minecraft:air") || worldBlock.equals("air");
        boolean schemEmpty = schematicBlock == null || schematicBlock.equals("minecraft:air") || schematicBlock.equals("air");

        if (worldEmpty && schemEmpty) {
            return BlockPlacementStatus.CORRECT;
        } else if (worldEmpty) {
            return BlockPlacementStatus.MISSING;
        } else if (schemEmpty) {
            return BlockPlacementStatus.EXTRA_BLOCK;
        } else if (worldBlock.equalsIgnoreCase(schematicBlock)) {
            return BlockPlacementStatus.CORRECT;
        } else {
            return BlockPlacementStatus.MISMATCHED;
        }
    }
}
