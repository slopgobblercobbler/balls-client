package com.ballsclient.core.module.telemetry.packet;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * PacketBurstBuffer — Queues and smoothens outbound packet spikes over consecutive client game ticks
 * to prevent network throttling and micro-stutters.
 */
public class PacketBurstBuffer extends Module {
    private final NumberSetting targetPacketRate = new NumberSetting("TargetPacketRate", "Target outbound packet dispatch rate per tick", 5.0, 1.0, 20.0, 1.0);
    private final NumberSetting maxQueueCapacity = new NumberSetting("MaxQueueCapacity", "Maximum queue buffer capacity in packets", 100.0, 20.0, 500.0, 10.0);
    private final BooleanSetting prioritizeMovement = new BooleanSetting("PrioritizeMovement", "Bypasses queue buffer for player movement packets", true);

    public PacketBurstBuffer() {
        super("packet-burst-buffer", "Packet Burst Buffer", "Queues and smoothens outbound packet spikes over consecutive client game ticks to prevent network throttling and micro-stutters.", Category.NETWORK);
        addSettings(targetPacketRate, maxQueueCapacity, prioritizeMovement);
    }

    public NumberSetting getTargetPacketRate() {
        return targetPacketRate;
    }

    public NumberSetting getMaxQueueCapacity() {
        return maxQueueCapacity;
    }

    public BooleanSetting getPrioritizeMovement() {
        return prioritizeMovement;
    }
}
