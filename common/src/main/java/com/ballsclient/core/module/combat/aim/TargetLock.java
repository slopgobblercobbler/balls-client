package com.ballsclient.core.module.combat.aim;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Target Lock — Retains camera focus locked to primary combat adversary during strafing and evasive maneuvers
 */
public class TargetLock extends Module {
    private final NumberSetting lockRange = new NumberSetting("LockRange", "Maximum target locking distance", 8.0, 4.0, 16.0, 0.5);
    private final BooleanSetting breakOnObstacle = new BooleanSetting("BreakOnObstacle", "Disengage if line of sight is obstructed", true);

    public TargetLock() {
        super("target-lock", "Target Lock", "Retains camera focus locked to primary combat adversary during strafing and evasive maneuvers", Category.COMBAT);
        addSettings(lockRange, breakOnObstacle);
    }

    public double getLockRange() {
        return lockRange.getValue();
    }

    public boolean isBreakOnObstacle() {
        return breakOnObstacle.getValue();
    }

    public boolean isLockValid(double distance, double maxRange, boolean hasLineOfSight, boolean breakOnObstacle) {
        if (distance > maxRange) return false;
        if (breakOnObstacle && !hasLineOfSight) return false;
        return true;
    }
}
