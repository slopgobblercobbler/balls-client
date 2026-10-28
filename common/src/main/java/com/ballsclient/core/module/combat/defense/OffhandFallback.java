package com.ballsclient.core.module.combat.defense;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Offhand Fallback — Intelligently cycles offhand between Shield, Gapple, and Totem based on combat state
 */
public class OffhandFallback extends Module {
    private final ModeSetting defaultOffhand = new ModeSetting("DefaultOffhand", "Primary resting offhand item", "Totem", "Totem", "Shield", "Gapple");
    private final NumberSetting gappleHealthThreshold = new NumberSetting("GappleHealthThreshold", "Health threshold to switch to Golden Apple", 12.0, 6.0, 16.0, 0.5);

    public OffhandFallback() {
        super("offhand-fallback", "Offhand Fallback", "Intelligently cycles offhand between Shield, Gapple, and Totem based on combat state", Category.COMBAT);
        addSettings(defaultOffhand, gappleHealthThreshold);
    }

    public String getDefaultOffhand() {
        return defaultOffhand.getValue();
    }

    public double getGappleHealthThreshold() {
        return gappleHealthThreshold.getValue();
    }

    public String selectBestOffhandItem(double currentHp, double gappleThreshold, boolean enemyHasAxe, boolean underHeavyFire) {
        if (currentHp <= 6.0 || underHeavyFire) return "Totem";
        if (currentHp < gappleThreshold) return "Gapple";
        if (enemyHasAxe) return "Totem";
        return "Shield";
    }
}
