package com.ballsclient.core.module.automation.building;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;

/**
 * SlabPlacementHelper — Locks slab placement to top, bottom, or double-slab state regardless of crosshair pitch.
 */
public class SlabPlacementHelper extends Module {
    private final ModeSetting lockState = new ModeSetting("LockState", "Enforced slab placement state", "BottomSlab", "BottomSlab", "TopSlab", "DoubleSlab");

    public SlabPlacementHelper() {
        super("slab-placement-helper", "Slab Placement Helper", "Locks slab placement to top, bottom, or double-slab state regardless of crosshair pitch", Category.AUTOMATION);
        addSettings(lockState);
    }

    public ModeSetting getLockState() {
        return lockState;
    }

    public static double overrideHitY(double actualHitY, String lockState) {
        if ("TopSlab".equalsIgnoreCase(lockState)) {
            return 0.9;
        } else if ("BottomSlab".equalsIgnoreCase(lockState)) {
            return 0.1;
        }
        return actualHitY;
    }
}
