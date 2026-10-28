package com.ballsclient.core.module.player.utility;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * AntiCrammingAlert — Monitors entity count per voxel, alerting when approaching 24-entity cramming limit.
 */
public class AntiCrammingAlert extends Module {
    private final NumberSetting warningThreshold = new NumberSetting("WarningThreshold", "Entity count in 1x1 area to trigger warning", 20.0, 10.0, 24.0, 1.0);
    private final NumberSetting maxEntityCramming = new NumberSetting("MaxEntityCramming", "Server maxEntityCramming game rule", 24.0, 20.0, 30.0, 1.0);
    private final BooleanSetting autoEject = new BooleanSetting("AutoEject", "Automatically move away from cramming cluster", true);

    public AntiCrammingAlert() {
        super("anti-cramming-alert", "Anti Cramming Alert", "Monitors entity count per voxel, alerting when approaching 24-entity cramming limit", Category.PLAYER);
        addSettings(warningThreshold, maxEntityCramming, autoEject);
    }

    public NumberSetting getWarningThreshold() {
        return warningThreshold;
    }

    public NumberSetting getMaxEntityCramming() {
        return maxEntityCramming;
    }

    public BooleanSetting getAutoEject() {
        return autoEject;
    }

    public static boolean isCrammingDangerous(int entityCount, double threshold, double maxCramming) {
        return entityCount >= threshold;
    }
}
