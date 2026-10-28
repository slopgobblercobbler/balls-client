package com.ballsclient.core.module.automation.farming;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * CactusGrowthSequencer — Identifies 3-high cactus columns and breaks upper blocks while safeguarding base nodes.
 */
public class CactusGrowthSequencer extends Module {
    public record BlockPos(int x, int y, int z) {}

    private final BooleanSetting preserveBaseNode = new BooleanSetting("PreserveBaseNode", "Safeguards ground cactus node to ensure continuous regrowth", true);

    public CactusGrowthSequencer() {
        super("cactus-growth-sequencer", "Cactus Growth Sequencer", "Identifies 3-high cactus columns and breaks upper blocks while safeguarding base nodes", Category.AUTOMATION);
        addSettings(preserveBaseNode);
    }

    public BooleanSetting getPreserveBaseNode() {
        return preserveBaseNode;
    }

    public List<BlockPos> getHarvestBlocks(int baseX, int baseY, int baseZ, int columnHeight) {
        List<BlockPos> targets = new ArrayList<>();
        int startY = preserveBaseNode.getValue() ? 1 : 0;
        for (int i = startY; i < columnHeight; i++) {
            targets.add(new BlockPos(baseX, baseY + i, baseZ));
        }
        return targets;
    }
}
