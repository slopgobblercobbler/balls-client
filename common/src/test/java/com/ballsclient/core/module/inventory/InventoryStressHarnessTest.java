package com.ballsclient.core.module.inventory;

import com.ballsclient.core.event.EventBus;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.ModuleManager;
import com.ballsclient.core.module.inventory.container.ContainerCapacityHUD;
import com.ballsclient.core.module.inventory.container.DoubleChestMapper;
import com.ballsclient.core.module.inventory.container.ShulkerNestDepthCounter;
import com.ballsclient.core.module.inventory.container.ShulkerPreviewCache;
import com.ballsclient.core.module.inventory.management.ItemConservationValidator;
import com.ballsclient.core.module.inventory.management.RepairCostCalculator;
import com.ballsclient.core.module.inventory.management.StackSplitRatioCalculator;
import com.ballsclient.core.module.inventory.sorting.CycleSort;
import com.ballsclient.core.module.inventory.sorting.StackConsolidation;
import com.ballsclient.core.setting.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Inventory & Storage Suite Empirical Adversarial Stress Harness")
class InventoryStressHarnessTest {

    // =========================================================================
    // 1. CYCLE DECOMPOSITION MINIMAL SWAP OPTIMALITY (N - C THEOREM)
    // =========================================================================

    /**
     * Permutation cycle decomposition minimal swap optimality test:
     * Generates 100 randomized inventory configurations of size 27 (Single Chest / Player inventory)
     * and asserts swaps == N - C.
     */
    @Test
    @DisplayName("Cycle Sort: Minimal swap bound (swaps == N - C) strictly holds across 100 randomized 27-slot inventories")
    void testCycleDecompositionMinimalSwapOptimality27Slots() {
        int n = 27;
        Random rng = new Random(42_27);

        for (int iter = 0; iter < 100; iter++) {
            int[] targets = generateRandomPermutation(n, rng);
            int c = countTotalDisjointCycles(targets);

            List<List<Integer>> cycles = CycleSort.findDisjointCycles(targets, Collections.emptySet());
            List<SlotSwap> swaps = CycleSort.computeSwaps(cycles);
            int theoreticalMin = CycleSort.getTheoreticalMinimumSwaps(cycles);
            int formulaSwaps = CycleSort.computeMinimalSwaps(n, c);

            // Invariant: Exact minimal swap equality: swaps == N - C
            assertThat(swaps)
                    .as("27-slot permutation iteration %d: swaps count must strictly equal N - C", iter)
                    .hasSize(n - c);
            assertThat(theoreticalMin).isEqualTo(n - c);
            assertThat(formulaSwaps).isEqualTo(n - c);

            // Verify empirical execution of swaps resolves the permutation
            verifySwapExecutionRestoresOrder(targets, swaps);
        }
    }

    /**
     * Permutation cycle decomposition minimal swap optimality test:
     * Generates 100 randomized inventory configurations of size 54 (Double Chest)
     * and asserts swaps == N - C.
     */
    @Test
    @DisplayName("Cycle Sort: Minimal swap bound (swaps == N - C) strictly holds across 100 randomized 54-slot inventories")
    void testCycleDecompositionMinimalSwapOptimality54Slots() {
        int n = 54;
        Random rng = new Random(42_54);

        for (int iter = 0; iter < 100; iter++) {
            int[] targets = generateRandomPermutation(n, rng);
            int c = countTotalDisjointCycles(targets);

            List<List<Integer>> cycles = CycleSort.findDisjointCycles(targets, Collections.emptySet());
            List<SlotSwap> swaps = CycleSort.computeSwaps(cycles);
            int theoreticalMin = CycleSort.getTheoreticalMinimumSwaps(cycles);
            int formulaSwaps = CycleSort.computeMinimalSwaps(n, c);

            // Invariant: Exact minimal swap equality: swaps == N - C
            assertThat(swaps)
                    .as("54-slot permutation iteration %d: swaps count must strictly equal N - C", iter)
                    .hasSize(n - c);
            assertThat(theoreticalMin).isEqualTo(n - c);
            assertThat(formulaSwaps).isEqualTo(n - c);

            // Verify empirical execution of swaps resolves the permutation
            verifySwapExecutionRestoresOrder(targets, swaps);
        }
    }

    @Test
    @DisplayName("Cycle Sort: Locked slots preservation under randomized permutations")
    void testCycleDecompositionLockedSlotsPreservation() {
        int n = 27;
        Random rng = new Random(1337);
        Set<Integer> lockedSlots = Set.of(0, 4, 8, 13, 26); // Specific locked hotbar/inventory slots

        for (int iter = 0; iter < 50; iter++) {
            int[] targets = generateRandomPermutation(n, rng);
            List<List<Integer>> cycles = CycleSort.findDisjointCycles(targets, lockedSlots);
            List<SlotSwap> swaps = CycleSort.computeSwaps(cycles);

            for (SlotSwap s : swaps) {
                assertThat(lockedSlots)
                        .as("Locked slots must never be involved in any planned swap: from=%d, to=%d", s.fromSlot(), s.toSlot())
                        .doesNotContain(s.fromSlot())
                        .doesNotContain(s.toSlot());
            }

            // Unlocked slots must also satisfy cycle bounds
            int unlockedCount = n - lockedSlots.size();
            assertThat(swaps.size()).isLessThanOrEqualTo(unlockedCount);
        }
    }

    @Test
    @DisplayName("Cycle Sort: Mathematical boundary cases (identity, derangement, single full cycle, 2-cycles)")
    void testCycleDecompositionBoundaryCases() {
        int n = 27;

        // Subcase 1: Identity permutation (already sorted) -> C = N, swaps == 0
        int[] identity = new int[n];
        for (int i = 0; i < n; i++) identity[i] = i;
        int cIdentity = countTotalDisjointCycles(identity);
        assertThat(cIdentity).isEqualTo(n);
        List<List<Integer>> cyclesIdent = CycleSort.findDisjointCycles(identity, null);
        assertThat(cyclesIdent).isEmpty();
        List<SlotSwap> swapsIdent = CycleSort.computeSwaps(cyclesIdent);
        assertThat(swapsIdent).isEmpty();
        assertThat(CycleSort.computeMinimalSwaps(n, cIdentity)).isEqualTo(0);

        // Subcase 2: Single full cyclic shift (0 -> 1 -> ... -> N-1 -> 0) -> C = 1, swaps == N - 1
        int[] fullCycle = new int[n];
        for (int i = 0; i < n; i++) fullCycle[i] = (i + 1) % n;
        int cFull = countTotalDisjointCycles(fullCycle);
        assertThat(cFull).isEqualTo(1);
        List<List<Integer>> cyclesFull = CycleSort.findDisjointCycles(fullCycle, null);
        assertThat(cyclesFull).hasSize(1);
        List<SlotSwap> swapsFull = CycleSort.computeSwaps(cyclesFull);
        assertThat(swapsFull).hasSize(n - 1);
        assertThat(CycleSort.computeMinimalSwaps(n, cFull)).isEqualTo(n - 1);
        verifySwapExecutionRestoresOrder(fullCycle, swapsFull);

        // Subcase 3: Pure transpositions (disjoint 2-cycles)
        // For N = 26 (even): 13 disjoint 2-cycles -> C = 13, swaps == 26 - 13 = 13
        int nEven = 26;
        int[] transpositions = new int[nEven];
        for (int i = 0; i < nEven; i += 2) {
            transpositions[i] = i + 1;
            transpositions[i + 1] = i;
        }
        int cTrans = countTotalDisjointCycles(transpositions);
        assertThat(cTrans).isEqualTo(nEven / 2);
        List<List<Integer>> cyclesTrans = CycleSort.findDisjointCycles(transpositions, null);
        assertThat(cyclesTrans).hasSize(nEven / 2);
        List<SlotSwap> swapsTrans = CycleSort.computeSwaps(cyclesTrans);
        assertThat(swapsTrans).hasSize(nEven / 2);
        assertThat(CycleSort.computeMinimalSwaps(nEven, cTrans)).isEqualTo(nEven / 2);
        verifySwapExecutionRestoresOrder(transpositions, swapsTrans);
    }

    // =========================================================================
    // 2. GLOBAL ITEM CONSERVATION LAW (1,000 RANDOMIZED TRANSFERS & SPLITS)
    // =========================================================================

    /**
     * Global item conservation law across 1,000 randomized transfers/splits
     * across dual 54-slot inventories (DoubleChest).
     * Asserts zero item duping or deletion (sum(initial) == sum(final)) and exact multiset equality.
     */
    @Test
    @DisplayName("Item Conservation: Zero item duping/deletion across 1,000 randomized transfers, splits & consolidations")
    void testGlobalItemConservationDual54Slot1000Transfers() {
        int containerSize = 54;
        Random rng = new Random(999_1000);

        String[] itemCatalog = {
                "minecraft:diamond",
                "minecraft:netherite_ingot",
                "minecraft:iron_ingot",
                "minecraft:gold_ingot",
                "minecraft:cobblestone",
                "minecraft:oak_planks",
                "minecraft:ender_pearl",         // max stack 16
                "minecraft:snowball",            // max stack 16
                "minecraft:totem_of_undying",    // max stack 1
                "minecraft:diamond_sword",       // max stack 1
                "minecraft:golden_apple",        // max stack 64
                "minecraft:enchanted_golden_apple",
                "minecraft:arrow",
                "minecraft:redstone",
                "minecraft:emerald",
                "minecraft:lapis_lazuli"
        };

        // Initialize dual 54-slot inventories (total 108 slots)
        ItemSlot[] chestA = new ItemSlot[containerSize];
        ItemSlot[] chestB = new ItemSlot[containerSize];

        for (int i = 0; i < containerSize; i++) {
            if (rng.nextBoolean()) {
                String id = itemCatalog[rng.nextInt(itemCatalog.length)];
                int maxStack = getMaxStackSizeFor(id);
                int count = 1 + rng.nextInt(maxStack);
                chestA[i] = new ItemSlot(i, id, count, maxStack, 0, 0, null);
            } else {
                chestA[i] = new ItemSlot(i, null, 0);
            }

            if (rng.nextBoolean()) {
                String id = itemCatalog[rng.nextInt(itemCatalog.length)];
                int maxStack = getMaxStackSizeFor(id);
                int count = 1 + rng.nextInt(maxStack);
                chestB[i] = new ItemSlot(i, id, count, maxStack, 0, 0, null);
            } else {
                chestB[i] = new ItemSlot(i, null, 0);
            }
        }

        // Calculate initial invariant checksum
        Map<String, Integer> initialTotals = calculateTotalUniverse(chestA, chestB);
        int initialTotalItemCount = sumItemCounts(initialTotals);

        assertThat(initialTotalItemCount).isGreaterThan(0);

        // Execute 1,000 randomized operations
        int totalOperations = 1_000;
        for (int op = 0; op < totalOperations; op++) {
            int actionType = rng.nextInt(6);

            switch (actionType) {
                case 0: {
                    // Quick transfer from Chest A to Chest B
                    simulateQuickTransfer(chestA, chestB, rng);
                    break;
                }
                case 1: {
                    // Quick transfer from Chest B to Chest A
                    simulateQuickTransfer(chestB, chestA, rng);
                    break;
                }
                case 2: {
                    // Stack split within Chest A or Chest B
                    ItemSlot[] targetChest = rng.nextBoolean() ? chestA : chestB;
                    simulateStackSplit(targetChest, rng);
                    break;
                }
                case 3: {
                    // Stack consolidation pass on Chest A or Chest B
                    ItemSlot[] targetChest = rng.nextBoolean() ? chestA : chestB;
                    simulateStackConsolidation(targetChest);
                    break;
                }
                case 4: {
                    // Inter-chest slot swap
                    int slotA = rng.nextInt(containerSize);
                    int slotB = rng.nextInt(containerSize);
                    ItemSlot temp = chestA[slotA];
                    chestA[slotA] = new ItemSlot(slotA, chestB[slotB].itemId(), chestB[slotB].count(), chestB[slotB].maxStackSize(), 0, 0, null);
                    chestB[slotB] = new ItemSlot(slotB, temp.itemId(), temp.count(), temp.maxStackSize(), 0, 0, null);
                    break;
                }
                case 5: {
                    // Bulk deposit matching items from Chest A into Chest B
                    simulateBulkDepositMatching(chestA, chestB);
                    break;
                }
            }

            // Invariant audit after EVERY SINGLE OPERATION
            Map<String, Integer> currentTotals = calculateTotalUniverse(chestA, chestB);
            assertThat(currentTotals)
                    .as("Item conservation failed at step %d: multiset altered", op)
                    .isEqualTo(initialTotals);

            assertThat(sumItemCounts(currentTotals))
                    .as("Total item universe count drifted at step %d", op)
                    .isEqualTo(initialTotalItemCount);

            // Audit physical constraints: count within [0, maxStackSize]
            auditSlotBounds(chestA);
            auditSlotBounds(chestB);
        }

        // Final verification using domain module ItemConservationValidator
        List<ItemSlot> allSlotsFinal = new ArrayList<>(Arrays.asList(chestA));
        allSlotsFinal.addAll(Arrays.asList(chestB));

        Map<String, Integer> domainTotals = ItemConservationValidator.calculateItemTotals(allSlotsFinal);
        assertThat(domainTotals).isEqualTo(initialTotals);
    }

    @Test
    @DisplayName("Item Conservation: StackSplitRatioCalculator exact integer partition under chaos")
    void testStackSplitRatioCalculatorConservation() {
        Random rng = new Random(777);

        for (int iter = 0; iter < 500; iter++) {
            int total = rng.nextInt(5000);
            int parts = 1 + rng.nextInt(32);

            int[] splits = StackSplitRatioCalculator.splitStack(total, parts);
            assertThat(splits).hasSize(parts);

            int sum = 0;
            int minPart = Integer.MAX_VALUE;
            int maxPart = Integer.MIN_VALUE;

            for (int p : splits) {
                assertThat(p).isGreaterThanOrEqualTo(0);
                sum += p;
                minPart = Math.min(minPart, p);
                maxPart = Math.max(maxPart, p);
            }

            // Invariant: sum of parts strictly equals original count
            assertThat(sum)
                    .as("Partition sum must equal total count: total=%d, parts=%d", total, parts)
                    .isEqualTo(total);

            // Invariant: balanced distribution (max - min <= 1)
            assertThat(maxPart - minPart).isLessThanOrEqualTo(1);
        }

        // Edge cases
        assertThat(StackSplitRatioCalculator.splitStack(0, 5)).containsExactly(0, 0, 0, 0, 0);
        assertThat(StackSplitRatioCalculator.splitStack(10, 0)).isEmpty();
        assertThat(StackSplitRatioCalculator.splitStack(10, -3)).isEmpty();
        assertThat(StackSplitRatioCalculator.splitStack(1, 4)).containsExactly(1, 0, 0, 0);
    }

    // =========================================================================
    // 3. SHULKER PREVIEW CACHE MULTITHREADING (50 THREADS) & MEMORY BOUNDS
    // =========================================================================

    /**
     * Test ShulkerPreviewCache concurrency across 50 threads and memory bounds under rapid eviction.
     * Asserts thread safety, zero deadlocks, and strict memory capacity cap under rapid churn.
     */
    @Test
    @DisplayName("Shulker Cache: 50 concurrent threads stress test with memory bounding and rapid eviction")
    void testShulkerPreviewCacheConcurrency50Threads() throws Exception {
        int threadCount = 50;
        int operationsPerThread = 100;
        int maxCapacity = 64;

        // Thread-safe wrapper over ShulkerPreviewCache LRU cache
        Map<String, List<ItemSlot>> cache = Collections.synchronizedMap(ShulkerPreviewCache.createCache(maxCapacity));

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startGun = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(threadCount);

        List<Throwable> exceptions = new CopyOnWriteArrayList<>();
        AtomicInteger totalInserts = new AtomicInteger(0);

        String[] itemPool = {"minecraft:diamond", "minecraft:iron_ingot", "minecraft:gold_ingot", "minecraft:shulker_shell"};

        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            executor.submit(() -> {
                try {
                    startGun.await();
                    Random threadRng = new Random(threadId * 1000L);

                    for (int op = 0; op < operationsPerThread; op++) {
                        // Create 27-slot shulker box
                        List<ItemSlot> boxContents = new ArrayList<>(27);
                        for (int s = 0; s < 27; s++) {
                            if (threadRng.nextBoolean()) {
                                String item = itemPool[threadRng.nextInt(itemPool.length)];
                                int count = 1 + threadRng.nextInt(64);
                                boxContents.add(new ItemSlot(s, item, count));
                            } else {
                                boxContents.add(new ItemSlot(s, null, 0));
                            }
                        }

                        // Compute SHA-256 hash
                        String hash = ShulkerPreviewCache.computeShulkerHash(boxContents);
                        assertThat(hash).isNotNull().hasSize(64);

                        // Concurrent write & read
                        cache.put(hash, boxContents);
                        totalInserts.incrementAndGet();

                        List<ItemSlot> retrieved = cache.get(hash);
                        if (retrieved != null) {
                            assertThat(retrieved).hasSize(27);
                        }

                        // Verify cache size does not exceed capacity under synchronized access
                        assertThat(cache.size())
                                .as("Cache size must never exceed maxCapacity %d", maxCapacity)
                                .isLessThanOrEqualTo(maxCapacity);
                    }
                } catch (Throwable ex) {
                    exceptions.add(ex);
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        // Start all threads simultaneously
        startGun.countDown();

        boolean completedInTime = finishLatch.await(20, TimeUnit.SECONDS);
        executor.shutdownNow();

        // Assertions
        assertThat(completedInTime).as("All 50 threads must complete within 20s (zero deadlocks)").isTrue();
        assertThat(exceptions).as("Zero exceptions allowed under multithreaded cache contention").isEmpty();
        assertThat(totalInserts.get()).isEqualTo(threadCount * operationsPerThread);

        // Memory bound strictly held
        assertThat(cache.size()).isLessThanOrEqualTo(maxCapacity);
    }

    @Test
    @DisplayName("Shulker Cache: SHA-256 deterministic hashing, collision resistance & null safety")
    void testShulkerHashDeterminismAndCollisionResistance() {
        // Null safety
        assertThat(ShulkerPreviewCache.computeShulkerHash(null)).isEqualTo("");

        // Empty list
        String emptyHash = ShulkerPreviewCache.computeShulkerHash(Collections.emptyList());
        assertThat(emptyHash).hasSize(64);

        // Determinism: same contents yield identical hash
        List<ItemSlot> boxA = List.of(
                new ItemSlot(0, "minecraft:netherite_block", 64),
                new ItemSlot(13, "minecraft:elytra", 1, "Flight Wings")
        );
        List<ItemSlot> boxB = List.of(
                new ItemSlot(0, "minecraft:netherite_block", 64),
                new ItemSlot(13, "minecraft:elytra", 1, "Flight Wings")
        );

        String hashA = ShulkerPreviewCache.computeShulkerHash(boxA);
        String hashB = ShulkerPreviewCache.computeShulkerHash(boxB);
        assertThat(hashA).isEqualTo(hashB);

        // Perturbation 1: Count change
        List<ItemSlot> boxCountDiff = List.of(
                new ItemSlot(0, "minecraft:netherite_block", 63),
                new ItemSlot(13, "minecraft:elytra", 1, "Flight Wings")
        );
        assertThat(ShulkerPreviewCache.computeShulkerHash(boxCountDiff)).isNotEqualTo(hashA);

        // Perturbation 2: Slot index change
        List<ItemSlot> boxSlotDiff = List.of(
                new ItemSlot(1, "minecraft:netherite_block", 64),
                new ItemSlot(13, "minecraft:elytra", 1, "Flight Wings")
        );
        assertThat(ShulkerPreviewCache.computeShulkerHash(boxSlotDiff)).isNotEqualTo(hashA);

        // Perturbation 3: Display name change
        List<ItemSlot> boxNameDiff = List.of(
                new ItemSlot(0, "minecraft:netherite_block", 64),
                new ItemSlot(13, "minecraft:elytra", 1, "Custom Wings")
        );
        assertThat(ShulkerPreviewCache.computeShulkerHash(boxNameDiff)).isNotEqualTo(hashA);
    }

    // =========================================================================
    // 4. SETTING BOUNDARY STRESS TEST (ALL 90 CONCRETE MODULES)
    // =========================================================================

    /**
     * Fuzz setting boundaries with extreme values (Double.NaN, MIN/MAX values, etc.)
     * to ensure zero uncaught exceptions across all 90 concrete Track 6 modules.
     */
    @Test
    @DisplayName("Setting Boundary Fuzzing: Extreme values (NaN, Inf, MIN/MAX, nulls) produce zero uncaught exceptions")
    void testSettingBoundaryFuzzingAll90Modules() {
        List<Module> allModules = InventoryModuleCatalog.createAllInstances();
        assertThat(allModules).hasSize(90);

        double[] extremeDoubles = {
                Double.NaN,
                Double.POSITIVE_INFINITY,
                Double.NEGATIVE_INFINITY,
                Double.MAX_VALUE,
                Double.MIN_VALUE,
                -Double.MAX_VALUE,
                -Double.MIN_VALUE,
                (double) Float.NaN,
                (double) Integer.MAX_VALUE,
                (double) Integer.MIN_VALUE,
                0.0,
                -0.0,
                1e-18,
                1e18
        };

        int settingsTested = 0;

        for (Module module : allModules) {
            assertThat(module.getCategory()).isEqualTo(Category.INVENTORY);

            for (Setting<?> setting : module.getSettings()) {
                settingsTested++;

                if (setting instanceof NumberSetting) {
                    NumberSetting ns = (NumberSetting) setting;
                    double defaultVal = ns.getDefaultValue();

                    for (double ext : extremeDoubles) {
                        ns.setValue(ext);
                        assertThat(ns.getValue()).isNotNull();
                        assertThat(Double.isNaN(ns.getValue())).isFalse();
                        assertThat(ns.getValue()).isGreaterThanOrEqualTo(ns.getMin());
                        assertThat(ns.getValue()).isLessThanOrEqualTo(ns.getMax());
                        assertThat(ns.getIntValue()).isNotNull();
                        assertThat(ns.getFloatValue()).isNotNull();
                    }

                    // Null safety
                    ns.setValue(null);
                    assertThat(ns.getValue()).isNotNull();

                    // JSON fuzzing
                    ns.fromJson(new JsonPrimitive(Double.NaN));
                    ns.fromJson(new JsonPrimitive("not_a_number"));
                    ns.fromJson(new JsonPrimitive(""));
                    ns.fromJson(new JsonPrimitive("   "));
                    ns.fromJson(new JsonPrimitive(Double.MAX_VALUE));
                    ns.fromJson(new JsonPrimitive(Double.MIN_VALUE));
                    ns.fromJson(JsonNull.INSTANCE);
                    ns.fromJson(new JsonObject());
                    ns.fromJson(new JsonArray());

                    assertThat(ns.getValue()).isNotNull();
                    assertThat(Double.isNaN(ns.getValue())).isFalse();

                    ns.reset();
                    assertThat(ns.getValue()).isEqualTo(defaultVal);

                } else if (setting instanceof BooleanSetting) {
                    BooleanSetting bs = (BooleanSetting) setting;
                    boolean defaultVal = bs.getDefaultValue();

                    bs.setValue(null);
                    assertThat(bs.getValue()).isNotNull();

                    bs.toggle();
                    bs.toggle();

                    bs.fromJson(new JsonPrimitive("true"));
                    bs.fromJson(new JsonPrimitive("false"));
                    bs.fromJson(new JsonPrimitive("invalid_string"));
                    bs.fromJson(new JsonPrimitive(12345));
                    bs.fromJson(JsonNull.INSTANCE);
                    bs.fromJson(new JsonObject());
                    bs.fromJson(new JsonArray());

                    assertThat(bs.getValue()).isNotNull();

                    bs.reset();
                    assertThat(bs.getValue()).isEqualTo(defaultVal);

                } else if (setting instanceof ModeSetting) {
                    ModeSetting ms = (ModeSetting) setting;
                    String defaultVal = ms.getDefaultValue();

                    ms.setValue(null);
                    assertThat(ms.getValue()).isNotNull();

                    ms.setValue("");
                    assertThat(ms.getValue()).isNotNull();

                    ms.setValue("   ");
                    assertThat(ms.getValue()).isNotNull();

                    ms.setValue("TOTALLY_UNKNOWN_MODE_xyz_999");
                    assertThat(ms.getModes()).contains(ms.getValue());

                    // Mode cycle test
                    for (int i = 0; i < ms.getModes().size() + 2; i++) {
                        ms.cycle();
                        assertThat(ms.getModes()).contains(ms.getValue());
                    }

                    ms.fromJson(new JsonPrimitive(""));
                    ms.fromJson(new JsonPrimitive("null"));
                    ms.fromJson(new JsonPrimitive("unknown"));
                    ms.fromJson(JsonNull.INSTANCE);
                    ms.fromJson(new JsonObject());

                    assertThat(ms.getModes()).contains(ms.getValue());

                    ms.reset();
                    assertThat(ms.getValue()).isEqualTo(defaultVal);

                } else if (setting instanceof ColorSetting) {
                    ColorSetting cs = (ColorSetting) setting;
                    int defaultVal = cs.getDefaultValue();

                    cs.setValue(null);
                    assertThat(cs.getValue()).isNotNull();

                    cs.setValue(0);
                    cs.setValue(-1);
                    cs.setValue(Integer.MAX_VALUE);
                    cs.setValue(Integer.MIN_VALUE);

                    cs.setChroma(true);
                    cs.setChromaSpeed(Double.NaN);
                    cs.getEffectiveRgba(System.currentTimeMillis());
                    cs.setChromaSpeed(Double.POSITIVE_INFINITY);
                    cs.getEffectiveRgba(Long.MAX_VALUE);
                    cs.getEffectiveRgba(Long.MIN_VALUE);

                    cs.fromJson(new JsonPrimitive(-1));
                    cs.fromJson(new JsonPrimitive("corrupt"));
                    cs.fromJson(JsonNull.INSTANCE);
                    cs.fromJson(new JsonObject());

                    assertThat(cs.getValue()).isNotNull();

                    cs.reset();
                    assertThat(cs.getValue()).isEqualTo(defaultVal);

                } else if (setting instanceof KeybindSetting) {
                    KeybindSetting ks = (KeybindSetting) setting;
                    int defaultVal = ks.getDefaultValue();

                    ks.setValue(null);
                    assertThat(ks.getValue()).isNotNull();

                    ks.setValue(0);
                    ks.setValue(KeybindSetting.KEY_NONE);
                    ks.setValue(Integer.MAX_VALUE);
                    ks.setValue(Integer.MIN_VALUE);

                    ks.fromJson(new JsonPrimitive(-1));
                    ks.fromJson(new JsonPrimitive("invalid"));
                    ks.fromJson(JsonNull.INSTANCE);
                    ks.fromJson(new JsonObject());

                    assertThat(ks.getValue()).isNotNull();

                    ks.reset();
                    assertThat(ks.getValue()).isEqualTo(defaultVal);
                }
            }
        }

        assertThat(settingsTested)
                .as("All 90 concrete modules must expose valid fuzzed settings")
                .isGreaterThanOrEqualTo(90);
    }

    // =========================================================================
    // 5. CONCURRENCY & DOMAIN BOUNDARY AUXILIARY STRESS
    // =========================================================================

    @Test
    @DisplayName("Architecture Concurrency: 20 simultaneous independent ModuleManagers registering Track 6 catalog")
    void testConcurrentIndependentModuleManagers() throws Exception {
        int threads = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);
        List<Throwable> errors = new CopyOnWriteArrayList<>();

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    EventBus bus = new EventBus();
                    ModuleManager mm = new ModuleManager(bus);
                    InventoryModuleCatalog.registerAll(mm);

                    assertThat(mm.getModules()).hasSize(90);
                    assertThat(mm.getModulesByCategory(Category.INVENTORY)).hasSize(90);

                    for (Module m : InventoryModuleCatalog.createAllInstances()) {
                        assertThat(mm.getModule(m.getId())).isNotNull();
                        assertThat(m.getSettings()).isNotEmpty();
                    }
                } catch (Throwable t) {
                    errors.add(t);
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean complete = latch.await(20, TimeUnit.SECONDS);
        executor.shutdownNow();

        assertThat(complete).isTrue();
        assertThat(errors).isEmpty();
    }

    @Test
    @DisplayName("DoubleChestMapper: Boundary slot indices and geometry mapping")
    void testDoubleChestMapperBoundaries() {
        assertThat(DoubleChestMapper.isDoubleChest(54)).isTrue();
        assertThat(DoubleChestMapper.isDoubleChest(27)).isFalse();
        assertThat(DoubleChestMapper.isDoubleChest(0)).isFalse();
        assertThat(DoubleChestMapper.isDoubleChest(-1)).isFalse();
        assertThat(DoubleChestMapper.isDoubleChest(55)).isFalse();

        // Primary half [0, 26]
        assertThat(DoubleChestMapper.mapSlotToHalf(0)).isEqualTo(0);
        assertThat(DoubleChestMapper.mapSlotToHalf(26)).isEqualTo(0);

        // Secondary half [27, 53]
        assertThat(DoubleChestMapper.mapSlotToHalf(27)).isEqualTo(1);
        assertThat(DoubleChestMapper.mapSlotToHalf(53)).isEqualTo(1);

        // Out of bounds
        assertThat(DoubleChestMapper.mapSlotToHalf(-1)).isEqualTo(-1);
        assertThat(DoubleChestMapper.mapSlotToHalf(54)).isEqualTo(-1);
        assertThat(DoubleChestMapper.mapSlotToHalf(Integer.MAX_VALUE)).isEqualTo(-1);
        assertThat(DoubleChestMapper.mapSlotToHalf(Integer.MIN_VALUE)).isEqualTo(-1);
    }

    @Test
    @DisplayName("RepairCostCalculator: Prior work penalty overflow protection and anvil repairs")
    void testRepairCostCalculatorBoundaries() {
        assertThat(RepairCostCalculator.calculatePriorWorkPenalty(0)).isEqualTo(0);
        assertThat(RepairCostCalculator.calculatePriorWorkPenalty(-10)).isEqualTo(0);
        assertThat(RepairCostCalculator.calculatePriorWorkPenalty(1)).isEqualTo(1);
        assertThat(RepairCostCalculator.calculatePriorWorkPenalty(2)).isEqualTo(3);
        assertThat(RepairCostCalculator.calculatePriorWorkPenalty(5)).isEqualTo(31);
        assertThat(RepairCostCalculator.calculatePriorWorkPenalty(30)).isEqualTo((1 << 30) - 1);
        assertThat(RepairCostCalculator.calculatePriorWorkPenalty(31)).isEqualTo(Integer.MAX_VALUE);
        assertThat(RepairCostCalculator.calculatePriorWorkPenalty(100)).isEqualTo(Integer.MAX_VALUE);

        // Remaining repairs
        assertThat(RepairCostCalculator.calculateRemainingRepairs(0, 1)).isEqualTo(6); // work: 0, 1, 2, 3, 4, 5 (< 40)
        assertThat(RepairCostCalculator.calculateRemainingRepairs(6, 1)).isEqualTo(0);
    }

    @Test
    @DisplayName("ContainerCapacityHUD: Boundary fill ratio calculations")
    void testContainerCapacityHUDBoundaries() {
        assertThat(ContainerCapacityHUD.calculateFillRatio(null, 54)).isEqualTo(0.0);
        assertThat(ContainerCapacityHUD.calculateFillRatio(Collections.emptyList(), 0)).isEqualTo(0.0);
        assertThat(ContainerCapacityHUD.calculateFillRatio(Collections.emptyList(), -1)).isEqualTo(0.0);

        List<ItemSlot> halfFull = List.of(
                new ItemSlot(0, "minecraft:stone", 64),
                new ItemSlot(1, "minecraft:dirt", 64),
                new ItemSlot(2, null, 0),
                new ItemSlot(3, null, 0)
        );
        assertThat(ContainerCapacityHUD.calculateFillRatio(halfFull, 4)).isEqualTo(0.5);
    }

    @Test
    @DisplayName("ShulkerNestDepthCounter: Recursive item counting null safety")
    void testShulkerNestDepthCounterNullSafety() {
        assertThat(ShulkerNestDepthCounter.calculateTotalItemCountRecursive(null)).isEqualTo(0);
        assertThat(ShulkerNestDepthCounter.calculateTotalItemCountRecursive(new ItemSlot(0, null, 0))).isEqualTo(0);
        assertThat(ShulkerNestDepthCounter.calculateTotalItemCountRecursive(new ItemSlot(0, "minecraft:shulker_box", 1))).isEqualTo(1);
    }

    // =========================================================================
    // HELPER METHODS
    // =========================================================================

    private static int[] generateRandomPermutation(int n, Random rng) {
        int[] p = new int[n];
        for (int i = 0; i < n; i++) p[i] = i;
        // Fisher-Yates shuffle
        for (int i = n - 1; i > 0; i--) {
            int j = rng.nextInt(i + 1);
            int tmp = p[i];
            p[i] = p[j];
            p[j] = tmp;
        }
        return p;
    }

    private static int countTotalDisjointCycles(int[] targets) {
        int n = targets.length;
        boolean[] visited = new boolean[n];
        int cycles = 0;
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                cycles++;
                int curr = i;
                while (!visited[curr]) {
                    visited[curr] = true;
                    curr = targets[curr];
                }
            }
        }
        return cycles;
    }

    private static void verifySwapExecutionRestoresOrder(int[] targets, List<SlotSwap> swaps) {
        // targets[i] specifies where the item currently at slot i belongs.
        // Let array[s] be the item currently placed at slot s.
        // If we initialize array such that array[targets[i]] = i,
        // applying swaps in sequence will restore array so that array[k] == k.
        // Alternatively, array[s] = targets[s]: applying swaps will sort array.
        int n = targets.length;
        int[] array = new int[n];
        // Calculate the inverse permutation
        for (int i = 0; i < n; i++) {
            array[targets[i]] = i;
        }

        for (SlotSwap s : swaps) {
            int tmp = array[s.fromSlot()];
            array[s.fromSlot()] = array[s.toSlot()];
            array[s.toSlot()] = tmp;
        }

        for (int i = 0; i < n; i++) {
            assertThat(array[i])
                    .as("Slot %d must be sorted after applying all planned cycle swaps", i)
                    .isEqualTo(i);
        }
    }

    private static int getMaxStackSizeFor(String id) {
        if (id.contains("totem") || id.contains("sword") || id.contains("bow")) return 1;
        if (id.contains("pearl") || id.contains("snowball") || id.contains("egg")) return 16;
        return 64;
    }

    private static Map<String, Integer> calculateTotalUniverse(ItemSlot[] chestA, ItemSlot[] chestB) {
        Map<String, Integer> tally = new HashMap<>();
        for (ItemSlot s : chestA) {
            if (s != null && !s.isEmpty() && s.itemId() != null && s.count() > 0) {
                tally.put(s.itemId(), tally.getOrDefault(s.itemId(), 0) + s.count());
            }
        }
        for (ItemSlot s : chestB) {
            if (s != null && !s.isEmpty() && s.itemId() != null && s.count() > 0) {
                tally.put(s.itemId(), tally.getOrDefault(s.itemId(), 0) + s.count());
            }
        }
        return tally;
    }

    private static int sumItemCounts(Map<String, Integer> tally) {
        int sum = 0;
        for (int c : tally.values()) {
            sum += c;
        }
        return sum;
    }

    private static void auditSlotBounds(ItemSlot[] chest) {
        for (int i = 0; i < chest.length; i++) {
            ItemSlot s = chest[i];
            if (s != null && !s.isEmpty()) {
                assertThat(s.count())
                        .as("Slot count cannot be negative at index %d", i)
                        .isGreaterThan(0);
                assertThat(s.count())
                        .as("Slot count %d exceeds maxStackSize %d at index %d", s.count(), s.maxStackSize(), i)
                        .isLessThanOrEqualTo(s.maxStackSize());
            }
        }
    }

    private static void simulateQuickTransfer(ItemSlot[] src, ItemSlot[] dst, Random rng) {
        int srcSlot = rng.nextInt(src.length);
        ItemSlot sourceItem = src[srcSlot];
        if (sourceItem == null || sourceItem.isEmpty()) return;

        // Try merge into existing partial stack in dst
        int remaining = sourceItem.count();
        for (int j = 0; j < dst.length && remaining > 0; j++) {
            ItemSlot targetSlot = dst[j];
            if (targetSlot != null && !targetSlot.isEmpty() && targetSlot.itemId().equals(sourceItem.itemId())) {
                int space = targetSlot.maxStackSize() - targetSlot.count();
                if (space > 0) {
                    int toAdd = Math.min(space, remaining);
                    dst[j] = new ItemSlot(j, targetSlot.itemId(), targetSlot.count() + toAdd, targetSlot.maxStackSize(), 0, 0, null);
                    remaining -= toAdd;
                }
            }
        }

        // Try put into first empty slot in dst
        for (int j = 0; j < dst.length && remaining > 0; j++) {
            ItemSlot targetSlot = dst[j];
            if (targetSlot == null || targetSlot.isEmpty()) {
                int toAdd = Math.min(sourceItem.maxStackSize(), remaining);
                dst[j] = new ItemSlot(j, sourceItem.itemId(), toAdd, sourceItem.maxStackSize(), 0, 0, null);
                remaining -= toAdd;
            }
        }

        // Update source slot
        if (remaining == 0) {
            src[srcSlot] = new ItemSlot(srcSlot, null, 0);
        } else {
            src[srcSlot] = new ItemSlot(srcSlot, sourceItem.itemId(), remaining, sourceItem.maxStackSize(), 0, 0, null);
        }
    }

    private static void simulateStackSplit(ItemSlot[] chest, Random rng) {
        int slotIdx = rng.nextInt(chest.length);
        ItemSlot s = chest[slotIdx];
        if (s == null || s.isEmpty() || s.count() <= 1) return;

        int splitAmount = 1 + rng.nextInt(s.count() - 1);

        // Find empty slot or partial matching slot
        for (int j = 0; j < chest.length; j++) {
            if (j == slotIdx) continue;
            ItemSlot dest = chest[j];
            if (dest == null || dest.isEmpty()) {
                chest[j] = new ItemSlot(j, s.itemId(), splitAmount, s.maxStackSize(), 0, 0, null);
                chest[slotIdx] = new ItemSlot(slotIdx, s.itemId(), s.count() - splitAmount, s.maxStackSize(), 0, 0, null);
                return;
            } else if (dest.itemId().equals(s.itemId()) && dest.count() + splitAmount <= dest.maxStackSize()) {
                chest[j] = new ItemSlot(j, dest.itemId(), dest.count() + splitAmount, dest.maxStackSize(), 0, 0, null);
                chest[slotIdx] = new ItemSlot(slotIdx, s.itemId(), s.count() - splitAmount, s.maxStackSize(), 0, 0, null);
                return;
            }
        }
    }

    private static void simulateStackConsolidation(ItemSlot[] chest) {
        List<ItemSlot> list = Arrays.asList(chest);
        List<StackConsolidation.ConsolidationMove> moves = StackConsolidation.planConsolidation(list, null);
        for (StackConsolidation.ConsolidationMove m : moves) {
            ItemSlot from = chest[m.fromSlot()];
            ItemSlot to = chest[m.toSlot()];
            int count = m.count();
            if (from != null && from.count() >= count) {
                int newFromCount = from.count() - count;
                chest[m.fromSlot()] = newFromCount > 0
                        ? new ItemSlot(m.fromSlot(), from.itemId(), newFromCount, from.maxStackSize(), 0, 0, null)
                        : new ItemSlot(m.fromSlot(), null, 0);
                int currentToCount = (to != null && !to.isEmpty()) ? to.count() : 0;
                chest[m.toSlot()] = new ItemSlot(m.toSlot(), from.itemId(), currentToCount + count, from.maxStackSize(), 0, 0, null);
            }
        }
    }

    private static void simulateBulkDepositMatching(ItemSlot[] src, ItemSlot[] dst) {
        Set<String> presentInDst = new HashSet<>();
        for (ItemSlot s : dst) {
            if (s != null && !s.isEmpty()) {
                presentInDst.add(s.itemId());
            }
        }

        for (int i = 0; i < src.length; i++) {
            ItemSlot s = src[i];
            if (s != null && !s.isEmpty() && presentInDst.contains(s.itemId())) {
                int remaining = s.count();
                // Merge into partial dst
                for (int j = 0; j < dst.length && remaining > 0; j++) {
                    ItemSlot d = dst[j];
                    if (d != null && !d.isEmpty() && d.itemId().equals(s.itemId())) {
                        int space = d.maxStackSize() - d.count();
                        if (space > 0) {
                            int toAdd = Math.min(space, remaining);
                            dst[j] = new ItemSlot(j, d.itemId(), d.count() + toAdd, d.maxStackSize(), 0, 0, null);
                            remaining -= toAdd;
                        }
                    }
                }
                // Place into empty dst
                for (int j = 0; j < dst.length && remaining > 0; j++) {
                    ItemSlot d = dst[j];
                    if (d == null || d.isEmpty()) {
                        int toAdd = Math.min(s.maxStackSize(), remaining);
                        dst[j] = new ItemSlot(j, s.itemId(), toAdd, s.maxStackSize(), 0, 0, null);
                        remaining -= toAdd;
                    }
                }
                src[i] = remaining > 0
                        ? new ItemSlot(i, s.itemId(), remaining, s.maxStackSize(), 0, 0, null)
                        : new ItemSlot(i, null, 0);
            }
        }
    }
}
