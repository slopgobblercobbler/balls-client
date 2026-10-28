package com.ballsclient.core.module.inventory.transfer;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * QuickTransferBatching — Batches shift-click transfer packets into atomic bursts with rate-limit pacing.
 */
public class QuickTransferBatching extends Module {
    private final NumberSetting packetsPerTick = new NumberSetting("PacketsPerTick", "Maximum transfer packets sent per tick", 3.0, 1.0, 10.0, 1.0);
    private final NumberSetting tickInterval = new NumberSetting("TickInterval", "Interval ticks between transfer packet bursts", 1.0, 1.0, 5.0, 1.0);

    public QuickTransferBatching() {
        super("quick-transfer-batching", "Quick Transfer Batching", "Batches shift-click transfer packets into atomic bursts", Category.INVENTORY);
        addSettings(packetsPerTick, tickInterval);
    }

    public NumberSetting getPacketsPerTick() {
        return packetsPerTick;
    }

    public NumberSetting getTickInterval() {
        return tickInterval;
    }

    public static List<List<Integer>> partitionTransferSlots(List<Integer> slotsToTransfer, int batchSize) {
        if (slotsToTransfer == null || slotsToTransfer.isEmpty()) return Collections.emptyList();
        int size = Math.max(1, batchSize);
        List<List<Integer>> batches = new ArrayList<>();
        for (int i = 0; i < slotsToTransfer.size(); i += size) {
            batches.add(new ArrayList<>(slotsToTransfer.subList(i, Math.min(slotsToTransfer.size(), i + size))));
        }
        return batches;
    }
}
