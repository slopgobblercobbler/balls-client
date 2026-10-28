package com.ballsclient.core.module.combat.aim;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Crossbow Sniper — Long-range projectile calculator for charged crossbow fireworks and arrows with gravity compensation
 */
public class CrossbowSniper extends Module {
    private final ModeSetting projectileType = new ModeSetting("ProjectileType", "Crossbow projectile ammunition type", "Arrow", "Arrow", "Firework");
    private final NumberSetting zoomFactor = new NumberSetting("ZoomFactor", "Crossbow scope zoom magnification factor", 3.0, 1.5, 5.0, 0.1);

    public CrossbowSniper() {
        super("crossbow-sniper", "Crossbow Sniper", "Long-range projectile calculator for charged crossbow fireworks and arrows with gravity compensation", Category.COMBAT);
        addSettings(projectileType, zoomFactor);
    }

    public String getProjectileType() {
        return projectileType.getValue();
    }

    public double getZoomFactor() {
        return zoomFactor.getValue();
    }

    public double getCrossbowVelocity(String type) {
        return "Firework".equalsIgnoreCase(type) ? 2.0 : 3.15;
    }
}
