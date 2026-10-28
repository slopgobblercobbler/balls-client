package com.ballsclient.core.module.player.mining;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * DeepslateSpeedUp — Cadence and swing timing optimization for high-hardness (3.0) deepslate.
 */
public class DeepslateSpeedUp extends Module {
    private final BooleanSetting optimizePacketCadence = new BooleanSetting("OptimizePacketCadence", "Eliminate redundant break start packets", true);
    private final BooleanSetting preAimAdjacent = new BooleanSetting("PreAimAdjacent", "Pre-aim crosshair at next contiguous deepslate block", true);
    private final NumberSetting swingAcceleration = new NumberSetting("SwingAcceleration", "Packet dispatch acceleration factor", 1.25, 1.0, 1.5, 0.05);

    public DeepslateSpeedUp() {
        super("deepslate-speed-up", "Deepslate Speed Up", "Cadence and swing timing optimization for high-hardness (3.0) deepslate", Category.PLAYER);
        addSettings(optimizePacketCadence, preAimAdjacent, swingAcceleration);
    }

    public BooleanSetting getOptimizePacketCadence() {
        return optimizePacketCadence;
    }

    public BooleanSetting getPreAimAdjacent() {
        return preAimAdjacent;
    }

    public NumberSetting getSwingAcceleration() {
        return swingAcceleration;
    }

    public static int calculateDeepslateTicks(double efficiencySpeed, int hasteLevel) {
        double hasteMult = hasteLevel > 0 ? (1.0 + 0.2 * hasteLevel) : 1.0;
        double speed = efficiencySpeed * hasteMult;
        double damagePerTick = speed / (3.0 * 30.0);
        if (damagePerTick >= 1.0) return 1;
        return (int) Math.ceil(1.0 / damagePerTick);
    }
}
