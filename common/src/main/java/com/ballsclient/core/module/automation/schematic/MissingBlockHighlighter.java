package com.ballsclient.core.module.automation.schematic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.automation.model.SchematicVolume;
import com.ballsclient.core.setting.NumberSetting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;

/**
 * MissingBlockHighlighter — Emits pulsing wireframe outlines around mismatched, missing, or wrongly rotated blocks.
 */
public class MissingBlockHighlighter extends Module {
    public record BlockPos(int x, int y, int z) {}

    private final NumberSetting pulseSpeed = new NumberSetting("PulseSpeed", "Outline pulse animation speed multiplier", 2.0, 0.5, 5.0, 0.5);
    private final NumberSetting outlineWidth = new NumberSetting("OutlineWidth", "Wireframe thickness in pixels", 2.0, 1.0, 5.0, 0.5);

    public MissingBlockHighlighter() {
        super("missing-block-highlighter", "Missing Block Highlighter", "Emits pulsing red wireframe outlines around mismatched, missing, or wrongly rotated blocks", Category.AUTOMATION);
        addSettings(pulseSpeed, outlineWidth);
    }

    public NumberSetting getPulseSpeed() {
        return pulseSpeed;
    }

    public NumberSetting getOutlineWidth() {
        return outlineWidth;
    }

    public static List<BlockPos> findMissingBlocks(SchematicVolume volume, BiFunction<BlockPos, String, Boolean> blockChecker, int playerX, int playerY, int playerZ) {
        if (volume == null) return List.of();
        List<BlockPos> missing = new ArrayList<>();
        for (int y = 0; y < volume.getHeight(); y++) {
            for (int z = 0; z < volume.getLength(); z++) {
                for (int x = 0; x < volume.getWidth(); x++) {
                    String req = volume.getBlockState(x, y, z);
                    if (!req.equals("minecraft:air") && !req.equals("air")) {
                        BlockPos pos = new BlockPos(x, y, z);
                        if (blockChecker != null && !blockChecker.apply(pos, req)) {
                            missing.add(pos);
                        }
                    }
                }
            }
        }
        missing.sort(Comparator.comparingDouble(p -> {
            double dx = p.x() - playerX;
            double dy = p.y() - playerY;
            double dz = p.z() - playerZ;
            return dx * dx + dy * dy + dz * dz;
        }));
        return missing;
    }
}
