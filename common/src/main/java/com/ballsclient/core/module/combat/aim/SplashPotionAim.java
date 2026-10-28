package com.ballsclient.core.module.combat.aim;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Splash Potion Aim — Calculates downward lob trajectory to land debuff potions at enemy feet or healing potions at self
 */
public class SplashPotionAim extends Module {
    private final ModeSetting potionMode = new ModeSetting("PotionMode", "Targeting mode for splash potions", "OffensiveAtEnemy", "OffensiveAtEnemy", "SelfHealing");
    private final NumberSetting potionSpeed = new NumberSetting("PotionSpeed", "Initial potion throw velocity", 0.5, 0.3, 0.8, 0.05);

    public SplashPotionAim() {
        super("splash-potion-aim", "Splash Potion Aim", "Calculates downward lob trajectory to land debuff potions at enemy feet or healing potions at self", Category.COMBAT);
        addSettings(potionMode, potionSpeed);
    }

    public String getPotionMode() {
        return potionMode.getValue();
    }

    public double getPotionSpeed() {
        return potionSpeed.getValue();
    }

    public double calculateSelfThrowPitch() {
        return 90.0; // Straight down at feet for instant splash
    }
}
