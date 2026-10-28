package com.ballsclient.core.module.combat.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Refill Helper — Restocks potions, totems, and gapples from main inventory into hotbar during pauses
 */
public class RefillHelper extends Module {
    private final NumberSetting minHotbarTotems = new NumberSetting("MinHotbarTotems", "Minimum count of totems to maintain on hotbar", 2.0, 1.0, 4.0, 1.0);
    private final NumberSetting refillDelayMs = new NumberSetting("RefillDelayMs", "Delay between inventory shift-clicks", 50.0, 20.0, 150.0, 5.0);

    public RefillHelper() {
        super("refill-helper", "Refill Helper", "Restocks potions, totems, and gapples from main inventory into hotbar during pauses", Category.COMBAT);
        addSettings(minHotbarTotems, refillDelayMs);
    }

    public double getMinHotbarTotems() {
        return minHotbarTotems.getValue();
    }

    public double getRefillDelayMs() {
        return refillDelayMs.getValue();
    }

    public boolean needsRefill(int currentCount, int desiredCount) {
        return currentCount < desiredCount;
    }
}
