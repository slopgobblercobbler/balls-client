package com.ballsclient.core.module.inventory.vacuum;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ThrowAwayPreventer — Suppresses pickup packets for items intentionally dropped by player within cooldown.
 */
public class ThrowAwayPreventer extends Module {
    private final NumberSetting cooldownSeconds = new NumberSetting("CooldownSeconds", "Suppression duration after intentional drop", 3.0, 1.0, 10.0, 0.5);

    public ThrowAwayPreventer() {
        super("throw-away-preventer", "Throw-Away Preventer", "Suppresses pickup packets for items intentionally dropped", Category.INVENTORY);
        addSettings(cooldownSeconds);
    }

    public NumberSetting getCooldownSeconds() {
        return cooldownSeconds;
    }

    public static boolean isUnderDropCooldown(long dropTimeMillis, long currentTimeMillis, double cooldownSeconds) {
        long elapsed = currentTimeMillis - dropTimeMillis;
        return elapsed >= 0 && elapsed < (long)(cooldownSeconds * 1000.0);
    }
}
