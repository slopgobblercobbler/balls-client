package com.ballsclient.core.module.player.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * AutoShear — Detects adult unsheared sheep and automatically activates shear interaction.
 */
public class AutoShear extends Module {
    private final NumberSetting shearRadius = new NumberSetting("ShearRadius", "Distance reach to shear sheep", 4.5, 2.0, 6.0, 0.5);
    private final BooleanSetting checkDurability = new BooleanSetting("CheckDurability", "Check remaining shears durability before action", true);

    public AutoShear() {
        super("auto-shear", "Auto Shear", "Detects adult unsheared sheep and automatically activates shear interaction", Category.PLAYER);
        addSettings(shearRadius, checkDurability);
    }

    public NumberSetting getShearRadius() {
        return shearRadius;
    }

    public BooleanSetting getCheckDurability() {
        return checkDurability;
    }

    public static boolean canShear(boolean isSheared, boolean isAdult, double distance, double maxRadius) {
        return !isSheared && isAdult && distance <= maxRadius;
    }
}
