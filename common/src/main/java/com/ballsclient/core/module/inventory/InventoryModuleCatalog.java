package com.ballsclient.core.module.inventory;

import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.ModuleManager;
import com.ballsclient.core.module.inventory.sorting.*;
import com.ballsclient.core.module.inventory.container.*;
import com.ballsclient.core.module.inventory.transfer.*;
import com.ballsclient.core.module.inventory.vacuum.*;
import com.ballsclient.core.module.inventory.management.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Central registry catalog for all 85+ (exactly 90 concrete) Inventory & Storage Suite modules.
 * Symmetrically partitions modules across 5 cohesive subpackages under com.ballsclient.core.module.inventory:
 * 1. sorting/ (18 modules)
 * 2. container/ (18 modules)
 * 3. transfer/ (18 modules)
 * 4. vacuum/ (18 modules)
 * 5. management/ (18 modules)
 */
public final class InventoryModuleCatalog {
    private static final List<Supplier<Module>> SUPPLIERS = new ArrayList<>();

    static {
        // --- 1. Sorting Mechanics (18 modules) ---
        register(CycleSort::new);
        register(LexicographicalSort::new);
        register(RarityTierSort::new);
        register(DurabilitySort::new);
        register(StackConsolidation::new);
        register(ChestBalanceSort::new);
        register(LockedSlotPreserver::new);
        register(AlphabeticalNameSort::new);
        register(BlockTypeCategorizer::new);
        register(QuantityDescSort::new);
        register(CombatLoadoutSort::new);
        register(FoodNutritionSort::new);
        register(ToolTierProgressionSort::new);
        register(ArmorPieceSort::new);
        register(OreGemCompactSort::new);
        register(PotionDurationSort::new);
        register(EnchantmentDensitySort::new);
        register(CustomSlotTemplateSort::new);

        // --- 2. Container & Storage Introspection (18 modules) ---
        register(ShulkerPreviewCache::new);
        register(DoubleChestMapper::new);
        register(EnderChestViewer::new);
        register(HopperTransferProfiler::new);
        register(FurnaceFuelBalancer::new);
        register(BrewingIngredientTracker::new);
        register(BarrelQuickPeek::new);
        register(DispenserPatternValidator::new);
        register(ContainerSearchHighlighter::new);
        register(ShulkerNestDepthCounter::new);
        register(ContainerCapacityHUD::new);
        register(TrappedChestDiscriminator::new);
        register(MinecartContainerTracker::new);
        register(LoomPatternPreviewCache::new);
        register(SmithingTemplatePreviewer::new);
        register(AnvilRepairPredictor::new);
        register(GrindstoneDisenchantPreviewer::new);
        register(CrafterMatrixValidator::new);

        // --- 3. Atomic Item Transfer & Batching (18 modules) ---
        register(QuickTransferBatching::new);
        register(AtomicShiftClickSequencer::new);
        register(BulkDepositMatching::new);
        register(DumpNonGearJunk::new);
        register(AutoLootHighValue::new);
        register(QuickDropCategory::new);
        register(OffhandQuickSwap::new);
        register(HotbarRefillOnConsumption::new);
        register(ArmorAutoEquip::new);
        register(ContainerVacuumDump::new);
        register(SmartBlockReplenisher::new);
        register(ThrowAwayTrashDumper::new);
        register(FastWithdrawalByQuery::new);
        register(DragTransferDistributor::new);
        register(SwapHotbarRowSequencer::new);
        register(ContainerBalanceTransfer::new);
        register(SingleItemDeposit::new);
        register(EmergencyHotbarPuller::new);

        // --- 4. Gravitational Vacuum & Floor Attraction (18 modules) ---
        register(BlackHoleVacuum::new);
        register(ItemSuctionVector::new);
        register(ExperienceOrbOptimizer::new);
        register(MagneticItemCollector::new);
        register(GroundDespawnPriorityPickup::new);
        register(FilterBasedAutoPick::new);
        register(ThrowAwayPreventer::new);
        register(PickupDelayEliminator::new);
        register(ItemClusterCentroid::new);
        register(InventorySpacePrecheck::new);
        register(ValuablesMagneticPriority::new);
        register(ProjectileRetrievalMagnet::new);
        register(VacuumConeDirectional::new);
        register(NetherLavaItemShield::new);
        register(ExplosionResidueCollector::new);
        register(CactusProximityShield::new);
        register(VoidBoundaryVacuumSavior::new);
        register(MobDropCollectorAura::new);

        // --- 5. Inventory Management & Conservation (18 modules) ---
        register(ItemConservationValidator::new);
        register(DuplicateToolCleaner::new);
        register(ArmorDurabilitySwitcher::new);
        register(TotemReplenishmentQueue::new);
        register(PotionPouchRestocker::new);
        register(ArrowQuiverPriority::new);
        register(RepairCostCalculator::new);
        register(ItemCountHUDDisplay::new);
        register(BundleStorageVisualizer::new);
        register(AutoRestockBuildingBlocks::new);
        register(WeaponEnchantLoadoutSelector::new);
        register(FireworkDurationTracker::new);
        register(FoodSaturationOptimizer::new);
        register(ArmorSwapHotkeyBinder::new);
        register(DurabilityDangerAlerter::new);
        register(InventoryFullnessChime::new);
        register(StackSplitRatioCalculator::new);
        register(TrashBlacklistManager::new);
    }

    private static void register(Supplier<Module> supplier) {
        SUPPLIERS.add(supplier);
    }

    public static void registerAll(ModuleManager manager) {
        if (manager == null) return;
        for (Supplier<Module> s : SUPPLIERS) {
            manager.register(s.get());
        }
    }

    public static List<Module> createAllInstances() {
        List<Module> list = new ArrayList<>(SUPPLIERS.size());
        for (Supplier<Module> s : SUPPLIERS) {
            list.add(s.get());
        }
        return Collections.unmodifiableList(list);
    }

    public static int getTotalModuleCount() {
        return SUPPLIERS.size();
    }

    private InventoryModuleCatalog() {}
}
