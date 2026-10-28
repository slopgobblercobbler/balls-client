package com.ballsclient.core.module.inventory.transfer;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * BulkDepositMatching — Automatically deposits all player items that currently exist in the open container.
 */
public class BulkDepositMatching extends Module {
    private final BooleanSetting preserveHotbar = new BooleanSetting("PreserveHotbar", "Keep matching items that reside in the hotbar", true);

    public BulkDepositMatching() {
        super("bulk-deposit-matching", "Bulk Deposit Matching", "Deposits all player items matching container items", Category.INVENTORY);
        addSettings(preserveHotbar);
    }

    public BooleanSetting getPreserveHotbar() {
        return preserveHotbar;
    }

    public static List<Integer> findMatchingSlots(List<ItemSlot> playerInventory, List<ItemSlot> containerSlots, boolean preserveHotbar) {
        List<Integer> matches = new ArrayList<>();
        if (playerInventory == null || containerSlots == null) return matches;

        Set<String> containerIds = new HashSet<>();
        for (ItemSlot cs : containerSlots) {
            if (cs != null && !cs.isEmpty() && cs.itemId() != null) {
                containerIds.add(cs.itemId().toLowerCase());
            }
        }

        for (int i = 0; i < playerInventory.size(); i++) {
            if (preserveHotbar && i >= 0 && i <= 8) {
                continue;
            }
            ItemSlot ps = playerInventory.get(i);
            if (ps != null && !ps.isEmpty() && ps.itemId() != null) {
                if (containerIds.contains(ps.itemId().toLowerCase())) {
                    matches.add(i);
                }
            }
        }
        return matches;
    }
}
