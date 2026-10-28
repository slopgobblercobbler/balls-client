package com.ballsclient.core.module.inventory.sorting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.module.inventory.SlotSwap;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.*;

/**
 * CycleSort — Cycle decomposition minimum-swap inventory sorter minimizing network packet clicks.
 */
public class CycleSort extends Module {
    private final BooleanSetting preserveLockedSlots = new BooleanSetting("PreserveLockedSlots", "Excludes locked slots from permutation cycles", true);
    private final NumberSetting packetDelayMs = new NumberSetting("PacketDelayMs", "Network click pacing delay in milliseconds", 25.0, 0.0, 200.0, 5.0);
    private final ModeSetting sortCriterion = new ModeSetting("SortCriterion", "Criterion for sorting slots", "Lexicographical", "Lexicographical", "Rarity", "Durability", "Quantity");

    public CycleSort() {
        super("cycle-sort", "Cycle Sort", "Cycle decomposition minimum-swap inventory sorter minimizing network packet clicks", Category.INVENTORY);
        addSettings(preserveLockedSlots, packetDelayMs, sortCriterion);
    }

    public BooleanSetting getPreserveLockedSlots() {
        return preserveLockedSlots;
    }

    public NumberSetting getPacketDelayMs() {
        return packetDelayMs;
    }

    public ModeSetting getSortCriterion() {
        return sortCriterion;
    }

    public static List<List<Integer>> findDisjointCycles(int[] targetPositions, Set<Integer> lockedSlots) {
        int n = targetPositions.length;
        boolean[] visited = new boolean[n];
        List<List<Integer>> cycles = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (visited[i] || (lockedSlots != null && lockedSlots.contains(i)) || targetPositions[i] == i) {
                visited[i] = true;
                continue;
            }

            List<Integer> currentCycle = new ArrayList<>();
            int curr = i;
            while (!visited[curr]) {
                visited[curr] = true;
                currentCycle.add(curr);
                curr = targetPositions[curr];
                if (lockedSlots != null && lockedSlots.contains(curr)) {
                    break;
                }
            }
            if (currentCycle.size() > 1) {
                cycles.add(currentCycle);
            }
        }
        return cycles;
    }

    public static List<SlotSwap> computeSwaps(List<List<Integer>> cycles) {
        List<SlotSwap> swaps = new ArrayList<>();
        if (cycles == null) return swaps;
        for (List<Integer> cycle : cycles) {
            for (int i = cycle.size() - 1; i > 0; i--) {
                swaps.add(new SlotSwap(cycle.get(0), cycle.get(i)));
            }
        }
        return swaps;
    }

    public static int getTheoreticalMinimumSwaps(List<List<Integer>> cycles) {
        if (cycles == null) return 0;
        int swaps = 0;
        for (List<Integer> cycle : cycles) {
            swaps += (cycle.size() - 1);
        }
        return swaps;
    }

    public static int computeMinimalSwaps(int totalElements, int disjointCycleCount) {
        return Math.max(0, totalElements - disjointCycleCount);
    }
}
