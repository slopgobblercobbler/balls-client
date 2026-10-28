package com.ballsclient.core.module.player.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * SugarcaneHarvester — Breaks multi-block sugarcane, bamboo, and cactus at block 2, leaving roots intact.
 */
public class SugarcaneHarvester extends Module {
    private final NumberSetting minHeight = new NumberSetting("MinHeight", "Minimum stalk height to trigger harvest", 2.0, 2.0, 4.0, 1.0);
    private final BooleanSetting harvestBamboo = new BooleanSetting("HarvestBamboo", "Include bamboo stalks in harvesting routine", true);
    private final BooleanSetting harvestCactus = new BooleanSetting("HarvestCactus", "Include desert cactus columns", false);

    public SugarcaneHarvester() {
        super("sugarcane-harvester", "Sugarcane Harvester", "Breaks multi-block sugarcane, bamboo, and cactus at block 2, leaving roots intact", Category.PLAYER);
        addSettings(minHeight, harvestBamboo, harvestCactus);
    }

    public NumberSetting getMinHeight() {
        return minHeight;
    }

    public BooleanSetting getHarvestBamboo() {
        return harvestBamboo;
    }

    public BooleanSetting getHarvestCactus() {
        return harvestCactus;
    }

    public static boolean shouldHarvestStem(int stalkHeight, double minHeight) {
        return stalkHeight >= minHeight;
    }
}
