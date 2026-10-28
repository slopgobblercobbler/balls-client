package com.ballsclient.core.module.inventory.container;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.List;

/**
 * ContainerCapacityHUD — Computes percentage of container space fullness and remaining stack capacity.
 */
public class ContainerCapacityHUD extends Module {
    private final BooleanSetting showPercentage = new BooleanSetting("ShowPercentage", "Display numeric fullness percentage on container HUD", true);

    public ContainerCapacityHUD() {
        super("container-capacity-hud", "Container Capacity HUD", "Computes percentage of container space fullness and remaining capacity", Category.INVENTORY);
        addSettings(showPercentage);
    }

    public BooleanSetting getShowPercentage() {
        return showPercentage;
    }

    public static double calculateFillRatio(List<ItemSlot> slots, int totalSlotCapacity) {
        if (totalSlotCapacity <= 0 || slots == null) return 0.0;
        int occupied = 0;
        for (ItemSlot s : slots) {
            if (s != null && !s.isEmpty()) {
                occupied++;
            }
        }
        return Math.min(1.0, occupied / (double) totalSlotCapacity);
    }
}
