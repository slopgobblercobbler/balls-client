package com.ballsclient.core.module.combat.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Auto Pot — Equips, casts downward splash healing or regen potion, and restores held slot in <1 tick
 */
public class AutoPot extends Module {
    private final NumberSetting healthThreshold = new NumberSetting("HealthThreshold", "Health threshold to throw healing potion", 12.0, 6.0, 16.0, 0.5);
    private final ModeSetting potionType = new ModeSetting("PotionType", "Target potion buff type", "InstantHealth", "InstantHealth", "Regeneration");
    private final BooleanSetting pitchDown = new BooleanSetting("PitchDown", "Look straight down when throwing", true);

    public AutoPot() {
        super("auto-pot", "Auto Pot", "Equips, casts downward splash healing or regen potion, and restores held slot in <1 tick", Category.COMBAT);
        addSettings(healthThreshold, potionType, pitchDown);
    }

    public double getHealthThreshold() {
        return healthThreshold.getValue();
    }

    public String getPotionType() {
        return potionType.getValue();
    }

    public boolean isPitchDown() {
        return pitchDown.getValue();
    }

    public boolean shouldThrowPotion(double currentHealth, double threshold, boolean hasPotion, boolean isAlreadyPotted) {
        return currentHealth <= threshold && hasPotion && !isAlreadyPotted;
    }
}
