package com.ballsclient.core.module.automation.farming;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * GlowBerryVineHarvester — Right-clicks glowing berries along vertical cave vines while preventing vine destruction.
 */
public class GlowBerryVineHarvester extends Module {
    private final BooleanSetting harvestBerriesOnly = new BooleanSetting("HarvestBerriesOnly", "Interacts to pluck berries rather than breaking vine stalk", true);

    public GlowBerryVineHarvester() {
        super("glow-berry-vine-harvester", "Glow Berry Harvester", "Right-clicks glowing berries along vertical cave vines while preventing vine destruction", Category.AUTOMATION);
        addSettings(harvestBerriesOnly);
    }

    public BooleanSetting getHarvestBerriesOnly() {
        return harvestBerriesOnly;
    }

    public boolean canHarvest(boolean hasBerries) {
        return hasBerries;
    }
}
