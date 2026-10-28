package com.ballsclient.core.module.inventory.container;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * BarrelQuickPeek — Raycast inspection viewer revealing barrel contents without opening animations.
 */
public class BarrelQuickPeek extends Module {
    private final NumberSetting maxPeekDistance = new NumberSetting("MaxPeekDistance", "Maximum raycast distance for barrel inspection", 4.5, 2.0, 8.0, 0.5);

    public BarrelQuickPeek() {
        super("barrel-quick-peek", "Barrel Quick Peek", "Raycast inspection viewer revealing barrel contents without opening animations", Category.INVENTORY);
        addSettings(maxPeekDistance);
    }

    public NumberSetting getMaxPeekDistance() {
        return maxPeekDistance;
    }

    public static boolean isWithinPeekDistance(double px, double py, double pz, double bx, double by, double bz, double maxDist) {
        double dx = px - bx;
        double dy = py - by;
        double dz = pz - bz;
        return (dx * dx + dy * dy + dz * dz) <= (maxDist * maxDist);
    }
}
