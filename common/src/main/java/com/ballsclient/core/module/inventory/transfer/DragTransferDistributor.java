package com.ballsclient.core.module.inventory.transfer;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;

/**
 * DragTransferDistributor — Simulates mouse drag distribution to evenly divide an item stack across multiple slots.
 */
public class DragTransferDistributor extends Module {
    private final ModeSetting distributionMode = new ModeSetting("DistributionMode", "Distribution pattern", "Evenly", "Evenly", "FillFirst");

    public DragTransferDistributor() {
        super("drag-transfer-distributor", "Drag Transfer Distributor", "Divides an item stack across multiple slots", Category.INVENTORY);
        addSettings(distributionMode);
    }

    public ModeSetting getDistributionMode() {
        return distributionMode;
    }

    public static int[] distributeStack(int totalCount, int targetSlotsCount) {
        if (targetSlotsCount <= 0) return new int[0];
        int[] result = new int[targetSlotsCount];
        int perSlot = totalCount / targetSlotsCount;
        int remainder = totalCount % targetSlotsCount;
        for (int i = 0; i < targetSlotsCount; i++) {
            result[i] = perSlot + (i < remainder ? 1 : 0);
        }
        return result;
    }
}
