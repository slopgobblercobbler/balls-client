package com.ballsclient.core.module.player;

import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.ModuleManager;
import com.ballsclient.core.module.player.world.*;
import com.ballsclient.core.module.player.interaction.*;
import com.ballsclient.core.module.player.mining.*;
import com.ballsclient.core.module.player.utility.*;
import com.ballsclient.core.module.player.automation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Central registry catalog for all 85+ (90 concrete) Player & World Interaction modules.
 * Partitions modules across 5 symmetrical subpackages (world, interaction, mining, utility, automation).
 */
public final class PlayerModuleCatalog {
    private static final List<Supplier<Module>> SUPPLIERS = new ArrayList<>();

    static {
        // --- 1. World & Dimensional Mechanics (18 modules) ---
        register(NetherPortalSync::new);
        register(SlimeChunkMatrix::new);
        register(VoidSafetyRing::new);
        register(OreVeinXRayPulse::new);
        register(ChunkBorderDisplay::new);
        register(BiomeLocator::new);
        register(StructureBoundingBox::new);
        register(StrongholdTrigHelper::new);
        register(LightLevelAnalyzer::new);
        register(WorldBorderGuard::new);
        register(BedrockBreakerSync::new);
        register(SpawnChunkMapper::new);
        register(CollisionBoxViewer::new);
        register(DimensionHopHelper::new);
        register(LightningStrikeRadar::new);
        register(MeteorologicalClock::new);
        register(DaylightCycleTracker::new);
        register(SubChunkCuller::new);

        // --- 2. Interaction & Block Placement (18 modules) ---
        register(FastPlace::new);
        register(GhostHand::new);
        register(LiquidInteract::new);
        register(ReachExpander::new);
        register(ScaffoldAssist::new);
        register(AutoMount::new);
        register(MiddleClickFriend::new);
        register(HandRestock::new);
        register(EntityInteractionFilter::new);
        register(MultiBlockInteract::new);
        register(InteractAngleLock::new);
        register(NoInteractGlitch::new);
        register(SafeContainerOpen::new);
        register(SilentInteract::new);
        register(ItemFrameLocker::new);
        register(BlockRotator::new);
        register(VillagerTradeOptimizer::new);
        register(SignTextAutoPaste::new);

        // --- 3. Mining & Excavation Kinematics (18 modules) ---
        register(FastBreak::new);
        register(AutoTool::new);
        register(AutoMine::new);
        register(NukerHelper::new);
        register(VeinMinerHelper::new);
        register(DeepslateSpeedUp::new);
        register(InstamineThresholdIndicator::new);
        register(ExplosiveMiningAlerter::new);
        register(SilkTouchSwitcher::new);
        register(DurabilitySaver::new);
        register(TunnelBorePilot::new);
        register(BedMiningHelper::new);
        register(PickaxeSwitchGlitchFix::new);
        register(BlockBreakTelemetry::new);
        register(MiningExhaustionReducer::new);
        register(TorchPlacerAssistant::new);
        register(OreVeinCostCalculator::new);
        register(UnbreakableFilter::new);

        // --- 4. Utility & Survival Failsafes (18 modules) ---
        register(AutoFish::new);
        register(AutoEat::new);
        register(AutoReconnect::new);
        register(InstantRespawn::new);
        register(FakePlayer::new);
        register(AutoDrop::new);
        register(InventoryMove::new);
        register(AFKDriftNeutralizer::new);
        register(AntiCrammingAlert::new);
        register(DeathCoordinateLogger::new);
        register(AirBubblePreserver::new);
        register(ItemDespawnTimer::new);
        register(PortalBypassUtility::new);
        register(AutoToolMender::new);
        register(BeaconEffectSelector::new);
        register(TotemPopCounter::new);
        register(AnvilExperienceOptimizer::new);
        register(EndCrystalSafetySensor::new);

        // --- 5. Automation & Macro Harvesting (18 modules) ---
        register(AutoFarm::new);
        register(TreeCapitatorHelper::new);
        register(AutoBreed::new);
        register(AutoShear::new);
        register(BoneMealAura::new);
        register(SugarcaneHarvester::new);
        register(CakeEater::new);
        register(AutoSign::new);
        register(AutoComposter::new);
        register(HoneyHarvester::new);
        register(KelpCutter::new);
        register(TurtleEggProtector::new);
        register(FlowerPotPlanter::new);
        register(AutoSmelter::new);
        register(CocoaBeanHarvester::new);
        register(ChorusFruitCollector::new);
        register(SculkCatalystFarmer::new);
        register(PotionBrewerScheduler::new);
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

    private PlayerModuleCatalog() {}
}
