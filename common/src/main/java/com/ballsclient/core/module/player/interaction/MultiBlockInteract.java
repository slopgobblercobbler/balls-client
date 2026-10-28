package com.ballsclient.core.module.player.interaction;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * MultiBlockInteract — Multi-target placement packet scheduler for laying lines and planes.
 */
public class MultiBlockInteract extends Module {
    private final NumberSetting batchSize = new NumberSetting("BatchSize", "Number of blocks placed per tick packet", 3.0, 1.0, 5.0, 1.0);
    private final ModeSetting pattern = new ModeSetting("Pattern", "Geometric placement sequence pattern", "Line", "Line", "Plane", "Column");
    private final NumberSetting tickSpacing = new NumberSetting("TickSpacing", "Ticks between batch dispatches", 1.0, 1.0, 4.0, 1.0);

    public MultiBlockInteract() {
        super("multi-block-interact", "Multi Block Interact", "Multi-target placement packet scheduler for laying lines and planes", Category.PLAYER);
        addSettings(batchSize, pattern, tickSpacing);
    }

    public NumberSetting getBatchSize() {
        return batchSize;
    }

    public ModeSetting getPattern() {
        return pattern;
    }

    public NumberSetting getTickSpacing() {
        return tickSpacing;
    }

    public static int[][] getPlacementOffsets(String pattern, int batchSize) {
        int count = Math.max(1, batchSize);
        int[][] offsets = new int[count][3];

        if ("Column".equalsIgnoreCase(pattern)) {
            for (int i = 0; i < count; i++) {
                offsets[i] = new int[]{0, i, 0};
            }
        } else if ("Plane".equalsIgnoreCase(pattern)) {
            int side = (int) Math.ceil(Math.sqrt(count));
            int idx = 0;
            for (int x = 0; x < side && idx < count; x++) {
                for (int z = 0; z < side && idx < count; z++) {
                    offsets[idx++] = new int[]{x, 0, z};
                }
            }
        } else {
            // Line
            for (int i = 0; i < count; i++) {
                offsets[i] = new int[]{i, 0, 0};
            }
        }
        return offsets;
    }
}
