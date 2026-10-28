package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * DurabilityDangerAlerter — Emits alerts when equipped gear durability drops below configurable threshold.
 */
public class DurabilityDangerAlerter extends Module {
    private final NumberSetting warningThresholdUses = new NumberSetting("WarningThresholdUses", "Remaining durability uses triggering alert", 15.0, 1.0, 50.0, 1.0);

    public DurabilityDangerAlerter() {
        super("durability-danger-alerter", "Durability Danger Alerter", "Emits alerts when equipped gear durability drops below threshold", Category.INVENTORY);
        addSettings(warningThresholdUses);
    }

    public NumberSetting getWarningThresholdUses() {
        return warningThresholdUses;
    }

    public static boolean isDangerouslyLow(int durability, int warningUses) {
        return durability <= warningUses;
    }
}
