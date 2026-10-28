package com.ballsclient.core.module.movement.failsafe;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Solidifies liquid surfaces (water/lava) to allow running on surface.
 */
public class WaterWalk extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", "Liquid interaction mode", "Solid", "Solid", "DolphinDrip", "Bounce");
    private final BooleanSetting lavaSupport = new BooleanSetting("LavaSupport", "Apply solidification logic to lava surfaces", true);
    private final NumberSetting speedBonus = new NumberSetting("SpeedBonus", "Running speed multiplier while on liquid surface", 1.15, 1.0, 2.0, 0.05);

    public WaterWalk() {
        super("water-walk", "Water Walk", "Solidifies liquid surfaces (water/lava) to allow running on surface", Category.MOVEMENT);
        addSettings(mode, lavaSupport, speedBonus);
    }

    public String getMode() { return mode.getValue(); }
    public boolean isLavaSupport() { return lavaSupport.getValue(); }
    public double getSpeedBonus() { return speedBonus.getValue(); }

    public boolean shouldSolidify(boolean inLiquid, boolean isLava) {
        if (!isEnabled()) return false;
        if (isLava && !isLavaSupport()) return false;
        return !inLiquid;
    }
}
