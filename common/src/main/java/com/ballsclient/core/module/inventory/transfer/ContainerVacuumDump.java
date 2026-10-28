package com.ballsclient.core.module.inventory.transfer;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.List;

/**
 * ContainerVacuumDump — Transmits rapid sequential click packets to empty all container items into inventory.
 */
public class ContainerVacuumDump extends Module {
    private final BooleanSetting stopWhenInventoryFull = new BooleanSetting("StopWhenInventoryFull", "Cease transfers when player inventory is completely full", true);

    public ContainerVacuumDump() {
        super("container-vacuum-dump", "Container Vacuum Dump", "Transmits click packets to empty all container items into inventory", Category.INVENTORY);
        addSettings(stopWhenInventoryFull);
    }

    public BooleanSetting getStopWhenInventoryFull() {
        return stopWhenInventoryFull;
    }

    public static int calculateAvailablePlayerSlots(List<ItemSlot> playerInventory) {
        if (playerInventory == null) return 0;
        int empty = 0;
        for (ItemSlot s : playerInventory) {
            if (s == null || s.isEmpty()) {
                empty++;
            }
        }
        return empty;
    }
}
