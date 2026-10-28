package com.ballsclient.core.module.inventory.sorting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.*;

/**
 * StackConsolidation — Merges fragmented partial item stacks of identical type up to maximum stack capacity.
 */
public class StackConsolidation extends Module {
    private final BooleanSetting targetHotbar = new BooleanSetting("TargetHotbar", "Prioritize consolidating towards hotbar slots", true);
    private final BooleanSetting fillPartialFirst = new BooleanSetting("FillPartialFirst", "Fill already partially filled stacks before empty slots", true);

    public StackConsolidation() {
        super("stack-consolidation", "Stack Consolidation", "Merges fragmented partial item stacks of identical type up to maximum stack capacity", Category.INVENTORY);
        addSettings(targetHotbar, fillPartialFirst);
    }

    public BooleanSetting getTargetHotbar() {
        return targetHotbar;
    }

    public BooleanSetting getFillPartialFirst() {
        return fillPartialFirst;
    }

    public record ConsolidationMove(int fromSlot, int toSlot, int count) {}

    public static List<ConsolidationMove> planConsolidation(List<ItemSlot> inventory, Set<Integer> lockedSlots) {
        List<ConsolidationMove> moves = new ArrayList<>();
        if (inventory == null || inventory.isEmpty()) return moves;

        int[] counts = new int[inventory.size()];
        for (int i = 0; i < inventory.size(); i++) {
            ItemSlot s = inventory.get(i);
            counts[i] = (s != null && !s.isEmpty()) ? s.count() : 0;
        }

        for (int i = 0; i < inventory.size(); i++) {
            ItemSlot slotA = inventory.get(i);
            if (slotA == null || slotA.isEmpty() || (lockedSlots != null && lockedSlots.contains(i))) {
                continue;
            }
            int maxStack = slotA.maxStackSize() > 0 ? slotA.maxStackSize() : 64;
            if (counts[i] >= maxStack) continue;

            for (int j = i + 1; j < inventory.size(); j++) {
                ItemSlot slotB = inventory.get(j);
                if (slotB == null || slotB.isEmpty() || (lockedSlots != null && lockedSlots.contains(j))) {
                    continue;
                }
                if (slotA.itemId().equals(slotB.itemId()) && counts[j] > 0) {
                    int space = maxStack - counts[i];
                    int transfer = Math.min(space, counts[j]);
                    if (transfer > 0) {
                        counts[i] += transfer;
                        counts[j] -= transfer;
                        moves.add(new ConsolidationMove(j, i, transfer));
                    }
                    if (counts[i] >= maxStack) break;
                }
            }
        }
        return moves;
    }
}
