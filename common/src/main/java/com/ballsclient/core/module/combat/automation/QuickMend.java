package com.ballsclient.core.module.combat.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Quick Mend — Throws Bottles o' Enchanting at feet while holding Mending gear until repaired
 */
public class QuickMend extends Module {
    private final NumberSetting targetDurabilityPercent = new NumberSetting("TargetDurabilityPercent", "Stop throwing XP bottles once target durability percentage is met", 100.0, 80.0, 100.0, 1.0);
    private final NumberSetting throwDelayMs = new NumberSetting("ThrowDelayMs", "Delay in milliseconds between XP bottle throws", 40.0, 20.0, 100.0, 5.0);

    public QuickMend() {
        super("quick-mend", "Quick Mend", "Throws Bottles o' Enchanting at feet while holding Mending gear until repaired", Category.COMBAT);
        addSettings(targetDurabilityPercent, throwDelayMs);
    }

    public double getTargetDurabilityPercent() {
        return targetDurabilityPercent.getValue();
    }

    public double getThrowDelayMs() {
        return throwDelayMs.getValue();
    }

    public boolean isMendingComplete(int currentDurability, int maxDurability, double targetPercent) {
        return ((double) currentDurability / maxDurability * 100.0) >= targetPercent;
    }
}
