package com.ballsclient.core.module.automation.farming;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * VillagerCropReplanter — Detects farmer villager seed inventories and supplements bread/wheat sowing cycles.
 */
public class VillagerCropReplanter extends Module {
    private final BooleanSetting replenishFarmerSeeds = new BooleanSetting("ReplenishFarmerSeeds", "Throws seed items to understocked farmer villagers", true);

    public VillagerCropReplanter() {
        super("villager-crop-replanter", "Villager Crop Replanter", "Detects farmer villager seed inventories and supplements bread/wheat sowing cycles", Category.AUTOMATION);
        addSettings(replenishFarmerSeeds);
    }

    public BooleanSetting getReplenishFarmerSeeds() {
        return replenishFarmerSeeds;
    }

    public boolean shouldDropSeeds(int villagerSeedCount) {
        return replenishFarmerSeeds.getValue() && villagerSeedCount < 8;
    }
}
