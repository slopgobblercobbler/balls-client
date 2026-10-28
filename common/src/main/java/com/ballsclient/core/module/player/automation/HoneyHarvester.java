package com.ballsclient.core.module.player.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * HoneyHarvester — Monitors bee hives for honey level 5, harvesting bottles or honeycombs safely.
 */
public class HoneyHarvester extends Module {
    private final BooleanSetting checkCampfire = new BooleanSetting("CheckCampfire", "Ensure lit campfire beneath hive before harvesting", true);
    private final ModeSetting harvestMode = new ModeSetting("HarvestMode", "Harvest item target", "Both", "HoneyBottle", "Honeycomb", "Both");

    public HoneyHarvester() {
        super("honey-harvester", "Honey Harvester", "Monitors bee hives for honey level 5, harvesting bottles or honeycombs safely", Category.PLAYER);
        addSettings(checkCampfire, harvestMode);
    }

    public BooleanSetting getCheckCampfire() {
        return checkCampfire;
    }

    public ModeSetting getHarvestMode() {
        return harvestMode;
    }

    public static boolean canHarvestHoney(int honeyLevel, boolean hasSmoke, boolean checkCampfire) {
        if (honeyLevel < 5) return false;
        if (checkCampfire && !hasSmoke) return false;
        return true;
    }
}
