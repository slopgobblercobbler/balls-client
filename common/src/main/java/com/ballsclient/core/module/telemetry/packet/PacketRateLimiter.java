package com.ballsclient.core.module.telemetry.packet;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * PacketRateLimiter — Monitors outgoing packet frequency and alerts or throttles client bursts
 * to prevent kick due to packet flood limits.
 */
public class PacketRateLimiter extends Module {
    private final NumberSetting maxPacketsPerSec = new NumberSetting("MaxPacketsPerSec", "Maximum permitted outbound packets per second", 150.0, 20.0, 500.0, 10.0);
    private final NumberSetting burstTolerance = new NumberSetting("BurstTolerance", "Burst allowance above baseline rate", 30.0, 5.0, 100.0, 5.0);
    private final BooleanSetting autoThrottle = new BooleanSetting("AutoThrottle", "Automatically throttles bursts exceeding rate limit", true);

    public PacketRateLimiter() {
        super("packet-rate-limiter", "Packet Rate Limiter", "Monitors outgoing packet frequency and alerts or throttles client bursts to prevent kick due to packet flood limits.", Category.NETWORK);
        addSettings(maxPacketsPerSec, burstTolerance, autoThrottle);
    }

    public NumberSetting getMaxPacketsPerSec() {
        return maxPacketsPerSec;
    }

    public NumberSetting getBurstTolerance() {
        return burstTolerance;
    }

    public BooleanSetting getAutoThrottle() {
        return autoThrottle;
    }
}
