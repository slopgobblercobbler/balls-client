package com.ballsclient.core.module.automation.building;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;

/**
 * StairRotationGuide — Locks stair corner orientation (straight, inner_left, outer_right) and upside-down state.
 */
public class StairRotationGuide extends Module {
    private final ModeSetting lockFacing = new ModeSetting("LockFacing", "Forced horizontal facing orientation", "Auto", "Auto", "North", "South", "East", "West");
    private final ModeSetting lockHalf = new ModeSetting("LockHalf", "Forced vertical half position", "Bottom", "Bottom", "Top");

    public StairRotationGuide() {
        super("stair-rotation-guide", "Stair Rotation Guide", "Locks stair corner orientation (straight, inner_left, outer_right) and upside-down state", Category.AUTOMATION);
        addSettings(lockFacing, lockHalf);
    }

    public ModeSetting getLockFacing() {
        return lockFacing;
    }

    public ModeSetting getLockHalf() {
        return lockHalf;
    }

    public String resolveFacing(String playerFacing) {
        String locked = lockFacing.getValue();
        if ("Auto".equalsIgnoreCase(locked)) {
            return playerFacing != null ? playerFacing : "North";
        }
        return locked;
    }
}
