package com.ballsclient.core.module.combat.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Fast Eat — Optimizes food packet ingestion throughput for rapid emergency healing
 */
public class FastEat extends Module {
    private final NumberSetting packetBurstCount = new NumberSetting("PacketBurstCount", "Number of use-item food packets transmitted per burst", 2.0, 1.0, 5.0, 1.0);
    private final BooleanSetting onlyWhenSafe = new BooleanSetting("OnlyWhenSafe", "Suppress fast eat when enemy within 3 blocks", false);

    public FastEat() {
        super("fast-eat", "Fast Eat", "Optimizes food packet ingestion throughput for rapid emergency healing", Category.COMBAT);
        addSettings(packetBurstCount, onlyWhenSafe);
    }

    public double getPacketBurstCount() {
        return packetBurstCount.getValue();
    }

    public boolean isOnlyWhenSafe() {
        return onlyWhenSafe.getValue();
    }

    public boolean canFastEat(double enemyDist, boolean requireSafe) {
        if (requireSafe && enemyDist < 3.5) return false;
        return true;
    }
}
