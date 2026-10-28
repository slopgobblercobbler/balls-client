package com.ballsclient.core.module.automation.farming;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * AmethystClusterHarvester — Highlights and harvests fully grown amethyst clusters without breaking budding blocks.
 */
public class AmethystClusterHarvester extends Module {
    private final BooleanSetting protectBuddingBlocks = new BooleanSetting("ProtectBuddingBlocks", "Prohibits attacks targeting irreplaceable budding amethyst blocks", true);
    private final BooleanSetting requireSilkTouch = new BooleanSetting("RequireSilkTouch", "Restricts harvesting to tools enchanted with Silk Touch", false);

    public AmethystClusterHarvester() {
        super("amethyst-cluster-harvester", "Amethyst Cluster Harvester", "Highlights and harvests fully grown amethyst clusters without breaking budding blocks", Category.AUTOMATION);
        addSettings(protectBuddingBlocks, requireSilkTouch);
    }

    public BooleanSetting getProtectBuddingBlocks() {
        return protectBuddingBlocks;
    }

    public BooleanSetting getRequireSilkTouch() {
        return requireSilkTouch;
    }

    public boolean isSafeClusterTarget(String blockId) {
        if (blockId == null) return false;
        if (protectBuddingBlocks.getValue() && blockId.contains("budding")) {
            return false;
        }
        return blockId.equals("minecraft:amethyst_cluster") || blockId.equals("amethyst_cluster");
    }
}
