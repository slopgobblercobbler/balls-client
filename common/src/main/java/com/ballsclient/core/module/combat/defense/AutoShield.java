package com.ballsclient.core.module.combat.defense;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Auto Shield — Automatically raises shield when incoming projectiles or axe attacks enter threat cone
 */
public class AutoShield extends Module {
    private final NumberSetting projectileThreatDistance = new NumberSetting("ProjectileThreatDistance", "Maximum projectile distance to trigger shield", 12.0, 3.0, 20.0, 0.5);
    private final BooleanSetting blockChargedMelee = new BooleanSetting("BlockChargedMelee", "Raise shield against charged attacks", true);

    public AutoShield() {
        super("auto-shield", "Auto Shield", "Automatically raises shield when incoming projectiles or axe attacks enter threat cone", Category.COMBAT);
        addSettings(projectileThreatDistance, blockChargedMelee);
    }

    public double getProjectileThreatDistance() {
        return projectileThreatDistance.getValue();
    }

    public boolean isBlockChargedMelee() {
        return blockChargedMelee.getValue();
    }

    public boolean shouldRaiseShield(boolean projectileApproaching, double projDist, boolean meleeImminent, double meleeDist) {
        if (projectileApproaching && projDist <= 12.0) return true;
        if (meleeImminent && meleeDist <= 3.5) return true;
        return false;
    }
}
