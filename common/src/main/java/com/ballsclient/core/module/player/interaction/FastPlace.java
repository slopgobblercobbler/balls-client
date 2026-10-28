package com.ballsclient.core.module.player.interaction;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * FastPlace — Reduces or eliminates vanilla 4-tick right-click placement delay.
 */
public class FastPlace extends Module {
    private final NumberSetting delayTicks = new NumberSetting("DelayTicks", "Placement cooldown delay ticks", 0.0, 0.0, 4.0, 1.0);
    private final BooleanSetting blocksOnly = new BooleanSetting("BlocksOnly", "Apply delay bypass only to block items", false);
    private final BooleanSetting fireworksBypass = new BooleanSetting("FireworksBypass", "Allow rapid firework rocket deployment", true);

    public FastPlace() {
        super("fast-place", "Fast Place", "Reduces or eliminates vanilla 4-tick right-click placement delay", Category.PLAYER);
        addSettings(delayTicks, blocksOnly, fireworksBypass);
    }

    public NumberSetting getDelayTicks() {
        return delayTicks;
    }

    public BooleanSetting getBlocksOnly() {
        return blocksOnly;
    }

    public BooleanSetting getFireworksBypass() {
        return fireworksBypass;
    }

    public static boolean shouldResetDelay(int currentDelay, double configuredDelay, boolean isBlock, boolean isFirework) {
        if (isFirework) return true;
        return currentDelay > configuredDelay;
    }
}
