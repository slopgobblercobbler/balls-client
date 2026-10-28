package com.ballsclient.core.module.automation;

import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.ModuleManager;
import com.ballsclient.core.module.automation.schematic.*;
import com.ballsclient.core.module.automation.macro.*;
import com.ballsclient.core.module.automation.building.*;
import com.ballsclient.core.module.automation.farming.*;
import com.ballsclient.core.module.automation.redstone.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Central registry catalog for all 70 concrete Automation & Schematics Suite modules.
 * Symmetrically partitions modules across 5 cohesive subpackages under com.ballsclient.core.module.automation:
 * 1. schematic/ (14 modules)
 * 2. macro/ (14 modules)
 * 3. building/ (14 modules)
 * 4. farming/ (14 modules)
 * 5. redstone/ (14 modules)
 */
public final class AutomationModuleCatalog {
    private static final List<Supplier<Module>> SUPPLIERS = new ArrayList<>();

    static {
        // --- 1. Schematics & Hologram Projection (14 modules) ---
        register(LitematicaViewer::new);
        register(SpongeSchematicViewer::new);
        register(GhostPlacementOverlay::new);
        register(MaterialListDiff::new);
        register(LayerByLayerPrinter::new);
        register(SchematicRotateFlip::new);
        register(MissingBlockHighlighter::new);
        register(AutoSchematicAligner::new);
        register(SubChunkMeshBuilder::new);
        register(SchematicVerifier::new);
        register(SchematicWaypointAnchor::new);
        register(NbtTileEntityDiff::new);
        register(SchematicExportTool::new);
        register(SchematicPaletteSwapper::new);

        // --- 2. Macro Chords & Script Automation (14 modules) ---
        register(MacroChordTrigger::new);
        register(KeyHoldAutomator::new);
        register(ScriptSequencePlayer::new);
        register(ConditionLoopMacro::new);
        register(HotbarSlotCyclerMacro::new);
        register(ChatCommandQueue::new);
        register(QuickWarpMacro::new);
        register(MouseJitterHumanizer::new);
        register(AutoReconnectMacro::new);
        register(PacketRateThrottler::new);
        register(MacroRecordingEngine::new);
        register(AutoReplyChime::new);
        register(AfkActivityCycler::new);
        register(EquipmentLoadoutMacro::new);

        // --- 3. Building & Placement Helpers (14 modules) ---
        register(BridgeBuilderAssist::new);
        register(AutoWallExtruder::new);
        register(SlabPlacementHelper::new);
        register(StairRotationGuide::new);
        register(ScaffoldStabilizer::new);
        register(FastBreakSequencer::new);
        register(LinePlacementLock::new);
        register(CircleCylinderGuide::new);
        register(SphereDomeProjector::new);
        register(RoofPitchAssistant::new);
        register(PillarBuilderAssist::new);
        register(AirPocketSealer::new);
        register(DiagonalWallRuler::new);
        register(BlockReachVisualizer::new);

        // --- 4. Agricultural & Harvesting Automation (14 modules) ---
        register(CropYieldPredictor::new);
        register(StemCropCutter::new);
        register(NetherWartHarvester::new);
        register(CactusGrowthSequencer::new);
        register(BambooCulmShearer::new);
        register(SweetBerryPicker::new);
        register(GlowBerryVineHarvester::new);
        register(MushroomColonySpreader::new);
        register(AmethystClusterHarvester::new);
        register(SnifferEggHatcherTracker::new);
        register(TreeSaplingReplacer::new);
        register(AnimalBreederAutomation::new);
        register(VillagerCropReplanter::new);
        register(BonemealDispenserGuide::new);

        // --- 5. Redstone Telemetry & Diagnostics (14 modules) ---
        register(RedstoneSignalViewer::new);
        register(RepeaterTickCalculator::new);
        register(ComparatorSubtractionDebugger::new);
        register(ObserverPulseVisualizer::new);
        register(RedstoneWireLossMonitor::new);
        register(PistonTimingProfiler::new);
        register(RedstoneTorchBurnoutAlerter::new);
        register(HopperClockPeriodTimer::new);
        register(SculkSensorFrequencyHud::new);
        register(TargetBlockHitDetector::new);
        register(DaylightDetectorChart::new);
        register(DropperDispenserCounter::new);
        register(LecternPagePowerGuide::new);
        register(RedstoneDustCrossTalkAuditor::new);
    }

    private static void register(Supplier<Module> supplier) {
        SUPPLIERS.add(supplier);
    }

    public static void registerAll(ModuleManager manager) {
        if (manager == null) return;
        for (Supplier<Module> supplier : SUPPLIERS) {
            manager.register(supplier.get());
        }
    }

    public static List<Module> createAllInstances() {
        List<Module> list = new ArrayList<>(SUPPLIERS.size());
        for (Supplier<Module> supplier : SUPPLIERS) {
            list.add(supplier.get());
        }
        return Collections.unmodifiableList(list);
    }

    public static int getTotalModuleCount() {
        return SUPPLIERS.size();
    }

    private AutomationModuleCatalog() {
    }
}
