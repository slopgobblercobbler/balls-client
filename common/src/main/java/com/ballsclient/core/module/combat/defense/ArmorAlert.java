package com.ballsclient.core.module.combat.defense;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Armor Alert — High-priority tactical alert when armor piece durability falls below safety threshold
 */
public class ArmorAlert extends Module {
    private final NumberSetting warningPercentage = new NumberSetting("WarningPercentage", "Critical durability percentage trigger", 20.0, 5.0, 50.0, 1.0);
    private final BooleanSetting playSound = new BooleanSetting("PlaySound", "Emit warning audio ping", true);

    public ArmorAlert() {
        super("armor-alert", "Armor Alert", "High-priority tactical alert when armor piece durability falls below safety threshold", Category.COMBAT);
        addSettings(warningPercentage, playSound);
    }

    public double getWarningPercentage() {
        return warningPercentage.getValue();
    }

    public boolean isPlaySound() {
        return playSound.getValue();
    }

    public boolean isArmorCritical(int currentDurability, int maxDurability, double thresholdPercent) {
        if (maxDurability <= 0) return false;
        double pct = (double) currentDurability / (double) maxDurability * 100.0;
        return pct <= thresholdPercent;
    }
}
