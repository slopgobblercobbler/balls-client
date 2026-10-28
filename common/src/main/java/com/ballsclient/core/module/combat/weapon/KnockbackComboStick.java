package com.ballsclient.core.module.combat.weapon;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Knockback Combo Stick — Hits with Knockback II item for initial spacing then switches to Sharpness weapon
 */
public class KnockbackComboStick extends Module {
    private final NumberSetting swapToDamageWeaponDelayMs = new NumberSetting("SwapToDamageWeaponDelayMs", "Delay before swapping from KB stick to main weapon", 100.0, 50.0, 300.0, 10.0);
    private final BooleanSetting onlyOnSprint = new BooleanSetting("OnlyOnSprint", "Require sprinting for knockback hit", true);

    public KnockbackComboStick() {
        super("knockback-combo-stick", "Knockback Combo Stick", "Hits with Knockback II item for initial spacing then switches to Sharpness weapon", Category.COMBAT);
        addSettings(swapToDamageWeaponDelayMs, onlyOnSprint);
    }

    public double getSwapToDamageWeaponDelayMs() {
        return swapToDamageWeaponDelayMs.getValue();
    }

    public boolean isOnlyOnSprint() {
        return onlyOnSprint.getValue();
    }

    public boolean isComboInitiated(boolean firstHitDone, long timeSinceHitMs, long delayMs) {
        return firstHitDone && timeSinceHitMs >= delayMs;
    }
}
