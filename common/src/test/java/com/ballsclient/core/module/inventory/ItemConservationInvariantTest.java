package com.ballsclient.core.module.inventory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Item Conservation Law Invariant Test")
class ItemConservationInvariantTest {

    public record TestItemSlot(int slot, String itemId, int count) {}

    /**
     * Computes the multiset frequency histogram: itemId -> total count across all slots.
     */
    public static Map<String, Integer> calculateInventoryChecksum(List<TestItemSlot> slots) {
        Map<String, Integer> tally = new HashMap<>();
        for (TestItemSlot s : slots) {
            if (s != null && s.itemId() != null && s.count() > 0) {
                tally.put(s.itemId(), tally.getOrDefault(s.itemId(), 0) + s.count());
            }
        }
        return tally;
    }

    /**
     * Headless stack compaction engine. Merges identical partial stacks.
     */
    public static List<TestItemSlot> compactStacks(List<TestItemSlot> input, int maxStackSize) {
        Map<String, Integer> totals = calculateInventoryChecksum(input);
        List<TestItemSlot> compacted = new ArrayList<>();
        int slotIndex = 0;

        for (Map.Entry<String, Integer> entry : totals.entrySet()) {
            String id = entry.getKey();
            int remaining = entry.getValue();

            while (remaining > 0) {
                int toAdd = Math.min(remaining, maxStackSize);
                compacted.add(new TestItemSlot(slotIndex++, id, toAdd));
                remaining -= toAdd;
            }
        }
        return compacted;
    }

    /**
     * Headless bulk deposit engine. Transfers matching items from player to container.
     */
    public static class TransferResult {
        public final List<TestItemSlot> newPlayer;
        public final List<TestItemSlot> newContainer;

        public TransferResult(List<TestItemSlot> newPlayer, List<TestItemSlot> newContainer) {
            this.newPlayer = newPlayer;
            this.newContainer = newContainer;
        }
    }

    public static TransferResult bulkDepositMatching(List<TestItemSlot> player, List<TestItemSlot> container) {
        Set<String> containerIds = new HashSet<>();
        for (TestItemSlot cs : container) {
            if (cs != null && cs.count() > 0) {
                containerIds.add(cs.itemId());
            }
        }

        List<TestItemSlot> remainingPlayer = new ArrayList<>();
        List<TestItemSlot> updatedContainer = new ArrayList<>(container);
        int nextContainerSlot = container.size();

        for (TestItemSlot ps : player) {
            if (ps != null && containerIds.contains(ps.itemId())) {
                updatedContainer.add(new TestItemSlot(nextContainerSlot++, ps.itemId(), ps.count()));
            } else {
                remainingPlayer.add(ps);
            }
        }

        return new TransferResult(remainingPlayer, updatedContainer);
    }

    @Test
    @DisplayName("Item Conservation: Stack compaction preserves exact item counts (Sum == Invariant)")
    void testStackCompactionPreservesItemCounts() {
        List<TestItemSlot> fragmented = List.of(
                new TestItemSlot(0, "minecraft:diamond", 14),
                new TestItemSlot(1, "minecraft:iron_ingot", 40),
                new TestItemSlot(2, "minecraft:diamond", 32),
                new TestItemSlot(3, "minecraft:diamond", 18), // Total diamond: 14 + 32 + 18 = 64
                new TestItemSlot(4, "minecraft:iron_ingot", 50)  // Total iron: 40 + 50 = 90
        );

        Map<String, Integer> before = calculateInventoryChecksum(fragmented);
        List<TestItemSlot> compacted = compactStacks(fragmented, 64);
        Map<String, Integer> after = calculateInventoryChecksum(compacted);

        assertThat(after)
                .as("Total item counts by ID before and after compaction must be strictly identical")
                .isEqualTo(before);

        assertThat(after.get("minecraft:diamond")).isEqualTo(64);
        assertThat(after.get("minecraft:iron_ingot")).isEqualTo(90);
    }

    @Test
    @DisplayName("Item Conservation: Bulk deposit maintains global item conservation between player and chest")
    void testBulkDepositConservation() {
        List<TestItemSlot> player = List.of(
                new TestItemSlot(0, "minecraft:cobblestone", 64),
                new TestItemSlot(1, "minecraft:gold_ingot", 32),
                new TestItemSlot(2, "minecraft:diamond", 16)
        );

        List<TestItemSlot> container = List.of(
                new TestItemSlot(0, "minecraft:gold_ingot", 10),
                new TestItemSlot(1, "minecraft:iron_ingot", 64)
        );

        // Compute global item sum before transfer
        List<TestItemSlot> combinedBefore = new ArrayList<>(player);
        combinedBefore.addAll(container);
        Map<String, Integer> globalChecksumBefore = calculateInventoryChecksum(combinedBefore);

        // Perform transfer
        TransferResult result = bulkDepositMatching(player, container);

        // Compute global item sum after transfer
        List<TestItemSlot> combinedAfter = new ArrayList<>(result.newPlayer);
        combinedAfter.addAll(result.newContainer);
        Map<String, Integer> globalChecksumAfter = calculateInventoryChecksum(combinedAfter);

        assertThat(globalChecksumAfter)
                .as("Global item universe conservation invariant: sum_{player+chest} count(id) == invariant")
                .isEqualTo(globalChecksumBefore);

        // Verify gold_ingot was deposited into chest
        assertThat(calculateInventoryChecksum(result.newPlayer).containsKey("minecraft:gold_ingot")).isFalse();
        assertThat(calculateInventoryChecksum(result.newContainer).get("minecraft:gold_ingot")).isEqualTo(42);
    }

    @RepeatedTest(10)
    @DisplayName("Item Conservation: Monte Carlo randomized stress test rigorously audits invariant under fuzzing")
    void testMonteCarloFuzzingConservation() {
        Random rng = new Random();
        String[] pool = {"diamond", "netherite_ingot", "ender_pearl", "golden_apple", "totem_of_undying", "arrow"};
        List<TestItemSlot> randomInventory = new ArrayList<>();

        int numSlots = 27 + rng.nextInt(28); // 27 to 54 slots
        for (int i = 0; i < numSlots; i++) {
            if (rng.nextBoolean()) {
                String id = "minecraft:" + pool[rng.nextInt(pool.length)];
                int count = 1 + rng.nextInt(64);
                randomInventory.add(new TestItemSlot(i, id, count));
            }
        }

        Map<String, Integer> expected = calculateInventoryChecksum(randomInventory);
        List<TestItemSlot> compacted = compactStacks(randomInventory, 64);
        Map<String, Integer> actual = calculateInventoryChecksum(compacted);

        assertThat(actual)
                .as("Randomized fuzz test invariant failure: item counts mismatch")
                .isEqualTo(expected);
    }
}
