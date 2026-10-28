package com.ballsclient.core.module.automation.farming;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * TreeSaplingReplacer — Automatically plants matching saplings at felled tree ground stumps with 2x2 support.
 */
public class TreeSaplingReplacer extends Module {
    public record BlockPos(int x, int y, int z) {}

    private final BooleanSetting megaTreeMode = new BooleanSetting("MegaTreeMode", "Places 2x2 grid for Jungle/Spruce/Dark Oak giant trees", false);

    public TreeSaplingReplacer() {
        super("tree-sapling-replacer", "Tree Sapling Replacer", "Automatically plants matching saplings at felled tree ground stumps with 2x2 support", Category.AUTOMATION);
        addSettings(megaTreeMode);
    }

    public BooleanSetting getMegaTreeMode() {
        return megaTreeMode;
    }

    public List<BlockPos> getSaplingOffsets(int stumpX, int stumpY, int stumpZ) {
        List<BlockPos> list = new ArrayList<>();
        list.add(new BlockPos(stumpX, stumpY, stumpZ));
        if (megaTreeMode.getValue()) {
            list.add(new BlockPos(stumpX + 1, stumpY, stumpZ));
            list.add(new BlockPos(stumpX, stumpY, stumpZ + 1));
            list.add(new BlockPos(stumpX + 1, stumpY, stumpZ + 1));
        }
        return list;
    }
}
