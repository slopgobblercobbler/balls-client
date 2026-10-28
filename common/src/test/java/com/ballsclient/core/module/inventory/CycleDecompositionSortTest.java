package com.ballsclient.core.module.inventory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Cycle Decomposition Minimum-Swap Permutation Test")
class CycleDecompositionSortTest {

    public record ItemSlot(int index, String id, int count) {}

    public record SlotSwap(int fromSlot, int toSlot) {}

    /**
     * Headless pure domain model of cycle decomposition minimum-swap sort.
     */
    public static class CycleSorter {
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
                // A cycle (c0, c1, ..., cm-1) can be sorted in exactly m - 1 swaps
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
    }

    @Test
    @DisplayName("Cycle Sort: Zero swaps required for already sorted permutation")
    void testAlreadySortedPermutation() {
        int[] targets = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        List<List<Integer>> cycles = CycleSorter.findDisjointCycles(targets, Collections.emptySet());
        assertThat(cycles).isEmpty();
        List<SlotSwap> swaps = CycleSorter.computeSwaps(cycles);
        assertThat(swaps).isEmpty();
        assertThat(CycleSorter.getTheoreticalMinimumSwaps(cycles)).isEqualTo(0);
    }

    @Test
    @DisplayName("Cycle Sort: Simple 2-cycle requires exactly 1 swap")
    void testSingleTwoCycleSwap() {
        // Swap slots 1 and 4
        int[] targets = {0, 4, 2, 3, 1, 5};
        List<List<Integer>> cycles = CycleSorter.findDisjointCycles(targets, Collections.emptySet());
        assertThat(cycles).hasSize(1);
        assertThat(cycles.get(0)).containsExactly(1, 4);

        List<SlotSwap> swaps = CycleSorter.computeSwaps(cycles);
        assertThat(swaps).hasSize(1);
        assertThat(swaps.get(0)).isEqualTo(new SlotSwap(1, 4));
        assertThat(CycleSorter.getTheoreticalMinimumSwaps(cycles)).isEqualTo(1);
    }

    @Test
    @DisplayName("Cycle Sort: 3-cycle requires exactly 2 swaps")
    void testThreeCycleSwaps() {
        // Cyclic shift: 0 -> 1 -> 2 -> 0
        int[] targets = {1, 2, 0};
        List<List<Integer>> cycles = CycleSorter.findDisjointCycles(targets, Collections.emptySet());
        assertThat(cycles).hasSize(1);
        assertThat(cycles.get(0)).containsExactly(0, 1, 2);

        List<SlotSwap> swaps = CycleSorter.computeSwaps(cycles);
        assertThat(swaps).hasSize(2);
        assertThat(CycleSorter.getTheoreticalMinimumSwaps(cycles)).isEqualTo(2);

        // Verify execution of swaps resolves permutation
        int[] array = {2, 0, 1};
        for (SlotSwap s : swaps) {
            int tmp = array[s.fromSlot];
            array[s.fromSlot] = array[s.toSlot];
            array[s.toSlot] = tmp;
        }
        // Result array must match inverse target positioning
        assertThat(array).isSorted();
    }

    @Test
    @DisplayName("Cycle Sort: Complex multi-cycle permutation satisfies N - K theorem")
    void testComplexDisjointCycles() {
        // Permutation: (0 3 5) (1 4) (2) (6 7 8 9)
        // Cycles: length 3 (2 swaps), length 2 (1 swap), length 1 (0 swaps), length 4 (3 swaps)
        // Total elements non-trivial: 3 + 2 + 4 = 9. Total cycles K = 3. Expected swaps = 9 - 3 = 6.
        int[] targets = {3, 4, 2, 5, 1, 0, 7, 8, 9, 6};
        List<List<Integer>> cycles = CycleSorter.findDisjointCycles(targets, Collections.emptySet());
        assertThat(cycles).hasSize(3);

        int minSwaps = CycleSorter.getTheoreticalMinimumSwaps(cycles);
        assertThat(minSwaps).isEqualTo(6);

        List<SlotSwap> swaps = CycleSorter.computeSwaps(cycles);
        assertThat(swaps).hasSize(6);
    }

    @Test
    @DisplayName("Cycle Sort: Locked slots are completely preserved and excluded from swap planning")
    void testLockedSlotsPreservation() {
        int[] targets = {1, 0, 3, 2, 4};
        Set<Integer> locked = Set.of(2, 3); // Slots 2 and 3 are frozen
        List<List<Integer>> cycles = CycleSorter.findDisjointCycles(targets, locked);

        assertThat(cycles).hasSize(1);
        assertThat(cycles.get(0)).containsExactly(0, 1);

        List<SlotSwap> swaps = CycleSorter.computeSwaps(cycles);
        assertThat(swaps).hasSize(1);
        assertThat(swaps.get(0)).isEqualTo(new SlotSwap(0, 1));

        for (SlotSwap s : swaps) {
            assertThat(locked).doesNotContain(s.fromSlot);
            assertThat(locked).doesNotContain(s.toSlot);
        }
    }
}
