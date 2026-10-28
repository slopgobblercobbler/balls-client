package com.ballsclient.core.module.automation.macro;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * PacketRateThrottler — Enforces strict maximum client action packets per tick to eliminate kicking for spamming.
 */
public class PacketRateThrottler extends Module {
    private final NumberSetting maxPacketsPerTick = new NumberSetting("MaxPacketsPerTick", "Maximum allowed packet dispatch rate per tick", 4.0, 1.0, 20.0, 1.0);
    private final NumberSetting burstAllowance = new NumberSetting("BurstAllowance", "Maximum accumulated burst token capacity", 2.0, 1.0, 10.0, 1.0);

    private double availableTokens = 2.0;
    private long lastTickTime = 0L;

    public PacketRateThrottler() {
        super("packet-rate-throttler", "Packet Rate Throttler", "Enforces strict maximum client action packets per tick to eliminate kicking for spamming", Category.AUTOMATION);
        addSettings(maxPacketsPerTick, burstAllowance);
    }

    public NumberSetting getMaxPacketsPerTick() {
        return maxPacketsPerTick;
    }

    public NumberSetting getBurstAllowance() {
        return burstAllowance;
    }

    public synchronized boolean tryAcquire(long currentTime) {
        double maxTokens = burstAllowance.getValue();
        double ratePerMs = maxPacketsPerTick.getValue() / 50.0; // 50ms per tick

        if (lastTickTime > 0L) {
            double elapsedMs = Math.max(0, currentTime - lastTickTime);
            availableTokens = Math.min(maxTokens, availableTokens + elapsedMs * ratePerMs);
        }
        lastTickTime = currentTime;

        if (availableTokens >= 1.0) {
            availableTokens -= 1.0;
            return true;
        }
        return false;
    }
}
