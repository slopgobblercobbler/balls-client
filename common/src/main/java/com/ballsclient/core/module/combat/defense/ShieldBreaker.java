package com.ballsclient.core.module.combat.defense;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Shield Breaker — Detects active blocking state and coordinates immediate weapon swap to axe
 */
public class ShieldBreaker extends Module {
    private final BooleanSetting switchBackAfterHit = new BooleanSetting("SwitchBackAfterHit", "Restore previous weapon after shield disabled", true);
    private final NumberSetting maxBreakCooldownTicks = new NumberSetting("MaxBreakCooldownTicks", "Maximum wait ticks for shield stun confirmation", 20.0, 5.0, 40.0, 1.0);

    public ShieldBreaker() {
        super("shield-breaker", "Shield Breaker", "Detects active blocking state and coordinates immediate weapon swap to axe", Category.COMBAT);
        addSettings(switchBackAfterHit, maxBreakCooldownTicks);
    }

    public boolean isSwitchBackAfterHit() {
        return switchBackAfterHit.getValue();
    }

    public double getMaxBreakCooldownTicks() {
        return maxBreakCooldownTicks.getValue();
    }

    public int getShieldBreakDurationTicks() {
        return 100; // 5.0 seconds in vanilla Minecraft
    }
}
