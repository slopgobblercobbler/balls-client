package com.ballsclient.core.module.player.mining;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * FastBreak — Bypasses vanilla 5-tick block break delay for continuous destruction.
 */
public class FastBreak extends Module {
    private final NumberSetting breakDelayTicks = new NumberSetting("BreakDelayTicks", "Block break reset cooldown ticks", 0.0, 0.0, 5.0, 1.0);
    private final NumberSetting speedMultiplier = new NumberSetting("SpeedMultiplier", "Mining speed damage multiplier", 1.0, 1.0, 2.0, 0.1);
    private final BooleanSetting packetSpamProtection = new BooleanSetting("PacketSpamProtection", "Guard against server rate limiting", true);

    public FastBreak() {
        super("fast-break", "Fast Break", "Bypasses vanilla 5-tick block break delay for continuous destruction", Category.PLAYER);
        addSettings(breakDelayTicks, speedMultiplier, packetSpamProtection);
    }

    public NumberSetting getBreakDelayTicks() {
        return breakDelayTicks;
    }

    public NumberSetting getSpeedMultiplier() {
        return speedMultiplier;
    }

    public BooleanSetting getPacketSpamProtection() {
        return packetSpamProtection;
    }

    public static int computeBreakCooldown(int vanillaCooldown, double configuredDelay) {
        return (int) Math.min(vanillaCooldown, configuredDelay);
    }
}
