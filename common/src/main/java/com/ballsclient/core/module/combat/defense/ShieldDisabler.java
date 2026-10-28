package com.ballsclient.core.module.combat.defense;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Shield Disabler — Predicts enemy shield raise and cues instant axe attacks to trigger 5-second disable
 */
public class ShieldDisabler extends Module {
    private final NumberSetting attackRange = new NumberSetting("AttackRange", "Maximum axe strike range to disable shield", 3.5, 2.0, 4.5, 0.1);
    private final BooleanSetting autoSwapAxe = new BooleanSetting("AutoSwapAxe", "Hot-swap to axe immediately when enemy blocks", true);

    public ShieldDisabler() {
        super("shield-disabler", "Shield Disabler", "Predicts enemy shield raise and cues instant axe attacks to trigger 5-second disable", Category.COMBAT);
        addSettings(attackRange, autoSwapAxe);
    }

    public double getAttackRange() {
        return attackRange.getValue();
    }

    public boolean isAutoSwapAxe() {
        return autoSwapAxe.getValue();
    }

    public boolean canDisableShield(boolean isEnemyBlocking, double distance, double maxRange, boolean hasAxe) {
        return isEnemyBlocking && distance <= maxRange && hasAxe;
    }
}
