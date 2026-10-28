package com.ballsclient.core.module.player.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ChorusFruitCollector — Prioritizes harvesting chorus flowers before felling chorus plant stems.
 */
public class ChorusFruitCollector extends Module {
    private final BooleanSetting prioritizeFlowers = new BooleanSetting("PrioritizeFlowers", "Harvest top chorus flower blocks before breaking plant stems", true);
    private final NumberSetting reachRadius = new NumberSetting("ReachRadius", "Interaction range for high chorus flowers", 4.5, 2.0, 6.0, 0.5);

    public ChorusFruitCollector() {
        super("chorus-fruit-collector", "Chorus Fruit Collector", "Prioritizes harvesting chorus flowers before felling chorus plant stems", Category.PLAYER);
        addSettings(prioritizeFlowers, reachRadius);
    }

    public BooleanSetting getPrioritizeFlowers() {
        return prioritizeFlowers;
    }

    public NumberSetting getReachRadius() {
        return reachRadius;
    }

    public static boolean isChorusFlower(String blockType) {
        if (blockType == null) return false;
        return "chorus_flower".equalsIgnoreCase(blockType) || blockType.toLowerCase().contains("chorus_flower");
    }
}
