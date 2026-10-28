package com.ballsclient.core.module.inventory.container;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * HopperTransferProfiler — Computes hopper throughput (8 ticks/item) and identifies container bottlenecks.
 */
public class HopperTransferProfiler extends Module {
    private final BooleanSetting alertBottlenecks = new BooleanSetting("AlertBottlenecks", "Display alert when hopper throughput is saturated", true);

    public HopperTransferProfiler() {
        super("hopper-transfer-profiler", "Hopper Transfer Profiler", "Computes hopper throughput and identifies container bottlenecks", Category.INVENTORY);
        addSettings(alertBottlenecks);
    }

    public BooleanSetting getAlertBottlenecks() {
        return alertBottlenecks;
    }

    public static double calculateTransferRateItemsPerSecond(int itemsTransferred, long elapsedTicks) {
        if (elapsedTicks <= 0) return 0.0;
        return (itemsTransferred * 20.0) / elapsedTicks;
    }
}
