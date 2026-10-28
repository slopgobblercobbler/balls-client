package com.ballsclient.core.module.combat.tactical;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Anchor Helper — Coordinates Respawn Anchor placement, glowstone charging, and explosive detonation
 */
public class AnchorHelper extends Module {
    private final NumberSetting maxBreakRange = new NumberSetting("MaxBreakRange", "Maximum interaction reach for anchor detonation", 4.0, 2.0, 5.0, 0.1);
    private final BooleanSetting requireExplosionSafety = new BooleanSetting("RequireExplosionSafety", "Prevent detonation if self is not shielded", true);

    public AnchorHelper() {
        super("anchor-helper", "Anchor Helper", "Coordinates Respawn Anchor placement, glowstone charging, and explosive detonation", Category.COMBAT);
        addSettings(maxBreakRange, requireExplosionSafety);
    }

    public double getMaxBreakRange() {
        return maxBreakRange.getValue();
    }

    public boolean isRequireExplosionSafety() {
        return requireExplosionSafety.getValue();
    }

    public boolean canDetonateAnchor(int charges, boolean inOverworldOrEnd, double distance, double maxRange) {
        return charges > 0 && inOverworldOrEnd && distance <= maxRange;
    }
}
