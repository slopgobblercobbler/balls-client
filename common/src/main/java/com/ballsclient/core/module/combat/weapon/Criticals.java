package com.ballsclient.core.module.combat.weapon;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Criticals — Synchronizes attack timing with downward falling velocity phase for crits
 */
public class Criticals extends Module {
    private final ModeSetting critMode = new ModeSetting("CritMode", "Critical strike coordination mode", "FallingOnly", "FallingOnly", "PacketJump", "MiniHop");
    private final NumberSetting minFallDistance = new NumberSetting("MinFallDistance", "Minimum fall distance to trigger critical", 0.1, 0.05, 0.5, 0.01);

    public Criticals() {
        super("criticals", "Criticals", "Synchronizes attack timing with downward falling velocity phase for crits", Category.COMBAT);
        addSettings(critMode, minFallDistance);
    }

    public String getCritMode() {
        return critMode.getValue();
    }

    public double getMinFallDistance() {
        return minFallDistance.getValue();
    }

    public boolean canCrit(double verticalVelocity, double fallDistance, boolean onGround, boolean inWater, boolean onLadder, boolean hasBlindness) {
        if (onGround || inWater || onLadder || hasBlindness) return false;
        return verticalVelocity < 0.0 && fallDistance > 0.0;
    }
}
