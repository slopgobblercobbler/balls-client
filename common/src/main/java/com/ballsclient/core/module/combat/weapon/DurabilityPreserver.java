package com.ballsclient.core.module.combat.weapon;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Durability Preserver — Swaps away weapons and armor when durability drops to critical threshold
 */
public class DurabilityPreserver extends Module {
    private final NumberSetting minDurabilityThreshold = new NumberSetting("MinDurabilityThreshold", "Minimum remaining durability before swap away", 5.0, 1.0, 20.0, 1.0);
    private final BooleanSetting notifyUser = new BooleanSetting("NotifyUser", "Display warning on HUD when piece is swapped", true);

    public DurabilityPreserver() {
        super("durability-preserver", "Durability Preserver", "Swaps away weapons and armor when durability drops to critical threshold", Category.COMBAT);
        addSettings(minDurabilityThreshold, notifyUser);
    }

    public double getMinDurabilityThreshold() {
        return minDurabilityThreshold.getValue();
    }

    public boolean isNotifyUser() {
        return notifyUser.getValue();
    }

    public boolean isItemAtRisk(int remainingDurability, int threshold) {
        return remainingDurability <= threshold && remainingDurability > 0;
    }
}
