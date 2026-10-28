package com.ballsclient.core.module.player.mining;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * AutoTool — Evaluates hotbar tools to equip the highest damage-per-tick tool for target block.
 */
public class AutoTool extends Module {
    private final BooleanSetting preferSilkTouch = new BooleanSetting("PreferSilkTouch", "Equip Silk Touch tool when available", false);
    private final BooleanSetting preferFortune = new BooleanSetting("PreferFortune", "Equip Fortune tool on ore blocks", true);
    private final BooleanSetting saveDurability = new BooleanSetting("SaveDurability", "Avoid using low durability tools", true);
    private final NumberSetting minDurability = new NumberSetting("MinDurability", "Minimum durability safety floor", 10.0, 1.0, 50.0, 1.0);

    public AutoTool() {
        super("auto-tool", "Auto Tool", "Evaluates hotbar tools to equip the highest damage-per-tick tool for target block", Category.PLAYER);
        addSettings(preferSilkTouch, preferFortune, saveDurability, minDurability);
    }

    public BooleanSetting getPreferSilkTouch() {
        return preferSilkTouch;
    }

    public BooleanSetting getPreferFortune() {
        return preferFortune;
    }

    public BooleanSetting getSaveDurability() {
        return saveDurability;
    }

    public NumberSetting getMinDurability() {
        return minDurability;
    }

    public static double calculateToolScore(String toolType, double baseSpeed, int effLevel, int remainingDurability, String blockType, boolean needsSilkTouch) {
        if (remainingDurability <= 5) return 0.0;
        double effBonus = effLevel > 0 ? (effLevel * effLevel + 1.0) : 0.0;
        double totalSpeed = baseSpeed + effBonus;
        return totalSpeed;
    }
}
