package com.ballsclient.core.module.player.utility;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * InstantRespawn — Dispatches respawn packets instantaneously upon death, skipping death GUI.
 */
public class InstantRespawn extends Module {
    private final NumberSetting delayTicks = new NumberSetting("DelayTicks", "Ticks after death before sending respawn packet", 0.0, 0.0, 20.0, 1.0);
    private final BooleanSetting logCoords = new BooleanSetting("LogCoords", "Print death coordinates to chat before respawning", true);

    public InstantRespawn() {
        super("instant-respawn", "Instant Respawn", "Dispatches respawn packets instantaneously upon death, skipping death GUI", Category.PLAYER);
        addSettings(delayTicks, logCoords);
    }

    public NumberSetting getDelayTicks() {
        return delayTicks;
    }

    public BooleanSetting getLogCoords() {
        return logCoords;
    }

    public static boolean shouldTriggerRespawn(boolean isDead, int ticksSinceDeath, double configuredDelay) {
        return isDead && ticksSinceDeath >= configuredDelay;
    }
}
