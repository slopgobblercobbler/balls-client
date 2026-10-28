package com.ballsclient.core.module.combat.weapon;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Fire Aspect Maximizer — Tracks target fire tick timer and spaces attacks to avoid wasting fire duration
 */
public class FireAspectMaximizer extends Module {
    private final NumberSetting minRemainingFireTicks = new NumberSetting("MinRemainingFireTicks", "Minimum remaining fire ticks before re-applying fire hit", 10.0, 0.0, 40.0, 1.0);
    private final BooleanSetting allowSpamForFinish = new BooleanSetting("AllowSpamForFinish", "Ignore fire timer if enemy HP is 4.0 or lower", true);

    public FireAspectMaximizer() {
        super("fire-aspect-maximizer", "Fire Aspect Maximizer", "Tracks target fire tick timer and spaces attacks to avoid wasting fire duration", Category.COMBAT);
        addSettings(minRemainingFireTicks, allowSpamForFinish);
    }

    public double getMinRemainingFireTicks() {
        return minRemainingFireTicks.getValue();
    }

    public boolean isAllowSpamForFinish() {
        return allowSpamForFinish.getValue();
    }

    public boolean shouldDelayFireAttack(int remainingFireTicks, int minTicks, double enemyHp) {
        if (enemyHp <= 4.0) return false;
        return remainingFireTicks > minTicks;
    }
}
