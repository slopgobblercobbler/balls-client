package com.ballsclient.core.module.inventory.transfer;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ThrowAwayTrashDumper — Drops blacklisted trash items onto ground when inventory reaches fullness threshold.
 */
public class ThrowAwayTrashDumper extends Module {
    private final NumberSetting fullnessThresholdPct = new NumberSetting("FullnessThresholdPct", "Inventory fullness percentage triggering trash dump", 95.0, 50.0, 100.0, 5.0);

    public ThrowAwayTrashDumper() {
        super("throw-away-trash-dumper", "Throw-Away Trash Dumper", "Drops blacklisted trash items onto ground when inventory reaches fullness threshold", Category.INVENTORY);
        addSettings(fullnessThresholdPct);
    }

    public NumberSetting getFullnessThresholdPct() {
        return fullnessThresholdPct;
    }

    public static boolean isTriggered(double currentFullness, double threshold) {
        return currentFullness >= threshold;
    }
}
