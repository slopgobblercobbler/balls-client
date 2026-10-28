package com.ballsclient.core.module.combat.tactical;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Combat Stamina — Restricts attack strikes to 100% weapon recharge charge for maximum damage
 */
public class CombatStamina extends Module {
    private final NumberSetting minCharge = new NumberSetting("MinCharge", "Minimum attack charge ratio required to swing", 0.98, 0.85, 1.0, 0.01);
    private final BooleanSetting allowSpamForShieldBreak = new BooleanSetting("AllowSpamForShieldBreak", "Bypass charge requirement if opponent blocks with shield", false);

    public CombatStamina() {
        super("combat-stamina", "Combat Stamina", "Restricts attack strikes to 100% weapon recharge charge for maximum damage", Category.COMBAT);
        addSettings(minCharge, allowSpamForShieldBreak);
    }

    public double getMinCharge() {
        return minCharge.getValue();
    }

    public boolean isAllowSpamForShieldBreak() {
        return allowSpamForShieldBreak.getValue();
    }

    public boolean canSwing(double currentCharge, double minCharge, boolean isShieldBypass, boolean enemyBlocking) {
        if (isShieldBypass && enemyBlocking) return true;
        return currentCharge >= minCharge;
    }
}
