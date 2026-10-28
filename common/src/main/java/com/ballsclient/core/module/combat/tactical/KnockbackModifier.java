package com.ballsclient.core.module.combat.tactical;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Knockback Modifier — Optimizes sprint-reset timing (W-tap / S-tap) to maximize outgoing knockback impulse
 */
public class KnockbackModifier extends Module {
    private final ModeSetting resetMode = new ModeSetting("ResetMode", "Sprint reset technique", "WTap", "WTap", "STap", "SneakTap");
    private final NumberSetting tapDurationMs = new NumberSetting("TapDurationMs", "Key release duration in milliseconds", 45.0, 20.0, 100.0, 5.0);

    public KnockbackModifier() {
        super("knockback-modifier", "Knockback Modifier", "Optimizes sprint-reset timing (W-tap / S-tap) to maximize outgoing knockback impulse", Category.COMBAT);
        addSettings(resetMode, tapDurationMs);
    }

    public String getResetMode() {
        return resetMode.getValue();
    }

    public double getTapDurationMs() {
        return tapDurationMs.getValue();
    }

    public boolean shouldPerformSprintReset(boolean wasSprinting, boolean hitRegistered, long ticksSinceLastReset) {
        return wasSprinting && hitRegistered && ticksSinceLastReset >= 5;
    }
}
