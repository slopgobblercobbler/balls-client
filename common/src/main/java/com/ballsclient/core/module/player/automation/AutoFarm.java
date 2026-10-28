package com.ballsclient.core.module.player.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * AutoFarm — Scans and harvests mature crops (age 7) and immediately replants seeds.
 */
public class AutoFarm extends Module {
    private final BooleanSetting wheat = new BooleanSetting("Wheat", "Harvest mature wheat crops", true);
    private final BooleanSetting carrots = new BooleanSetting("Carrots", "Harvest mature carrot crops", true);
    private final BooleanSetting potatoes = new BooleanSetting("Potatoes", "Harvest mature potato crops", true);
    private final BooleanSetting autoReplant = new BooleanSetting("AutoReplant", "Automatically plant matching seed after harvest", true);

    public AutoFarm() {
        super("auto-farm", "Auto Farm", "Scans and harvests mature crops (age 7) and immediately replants seeds", Category.PLAYER);
        addSettings(wheat, carrots, potatoes, autoReplant);
    }

    public BooleanSetting getWheat() {
        return wheat;
    }

    public BooleanSetting getCarrots() {
        return carrots;
    }

    public BooleanSetting getPotatoes() {
        return potatoes;
    }

    public BooleanSetting getAutoReplant() {
        return autoReplant;
    }

    public static boolean isCropFullyGrown(String cropType, int age) {
        if (cropType == null) return false;
        String typeLower = cropType.toLowerCase();
        if (typeLower.contains("beetroot")) {
            return age >= 3;
        }
        // Wheat, carrots, potatoes reach maturity at age 7
        return age >= 7;
    }
}
