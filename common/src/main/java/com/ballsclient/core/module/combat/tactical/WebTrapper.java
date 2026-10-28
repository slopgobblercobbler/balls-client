package com.ballsclient.core.module.combat.tactical;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Web Trapper — Coordinates placement of cobwebs at opponent feet to eliminate mobility
 */
public class WebTrapper extends Module {
    private final NumberSetting trapDistance = new NumberSetting("TrapDistance", "Maximum distance to place cobweb at target feet", 3.5, 1.5, 4.5, 0.1);
    private final BooleanSetting checkTargetOnGround = new BooleanSetting("CheckTargetOnGround", "Only place if target is grounded", true);

    public WebTrapper() {
        super("web-trapper", "Web Trapper", "Coordinates placement of cobwebs at opponent feet to eliminate mobility", Category.COMBAT);
        addSettings(trapDistance, checkTargetOnGround);
    }

    public double getTrapDistance() {
        return trapDistance.getValue();
    }

    public boolean isCheckTargetOnGround() {
        return checkTargetOnGround.getValue();
    }

    public boolean canTrapEntity(double distance, double maxRange, boolean onGround, boolean requiresOnGround) {
        if (distance > maxRange) return false;
        if (requiresOnGround && !onGround) return false;
        return true;
    }
}
