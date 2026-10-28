package com.ballsclient.core.module.automation.farming;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * StemCropCutter — Automatically targets and harvests mature pumpkins and melons while protecting stems.
 */
public class StemCropCutter extends Module {
    private final BooleanSetting harvestPumpkins = new BooleanSetting("HarvestPumpkins", "Breaks mature pumpkin blocks", true);
    private final BooleanSetting harvestMelons = new BooleanSetting("HarvestMelons", "Breaks mature melon blocks", true);

    public StemCropCutter() {
        super("stem-crop-cutter", "Stem Crop Cutter", "Automatically targets and harvests mature pumpkins and melons while protecting stems", Category.AUTOMATION);
        addSettings(harvestPumpkins, harvestMelons);
    }

    public BooleanSetting getHarvestPumpkins() {
        return harvestPumpkins;
    }

    public BooleanSetting getHarvestMelons() {
        return harvestMelons;
    }

    public boolean isHarvestableTarget(String blockId) {
        if (blockId == null) return false;
        if (blockId.contains("stem")) {
            return false; // Strictly protect stems
        }
        if (harvestPumpkins.getValue() && (blockId.equals("minecraft:pumpkin") || blockId.equals("pumpkin"))) {
            return true;
        }
        if (harvestMelons.getValue() && (blockId.equals("minecraft:melon") || blockId.equals("melon"))) {
            return true;
        }
        return false;
    }
}
