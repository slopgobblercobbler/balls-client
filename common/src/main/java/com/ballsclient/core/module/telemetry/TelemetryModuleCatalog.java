package com.ballsclient.core.module.telemetry;

import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.ModuleManager;
import com.ballsclient.core.module.telemetry.bandwidth.*;
import com.ballsclient.core.module.telemetry.diagnostics.*;
import com.ballsclient.core.module.telemetry.packet.*;
import com.ballsclient.core.module.telemetry.ping.*;
import com.ballsclient.core.module.telemetry.tps.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * TelemetryModuleCatalog — Central registry catalog for all 50 concrete Network & Telemetry Suite modules.
 * Symmetrically partitions modules across 5 cohesive subpackages under com.ballsclient.core.module.telemetry:
 * 1. packet/ (10 modules) — Low-level packet inspection, buffering, filtering, and protocol integrity
 * 2. ping/ (10 modules) — Round-trip latency, RFC 3550 jitter, stability index, and ping graphs
 * 3. tps/ (10 modules) — Server tick calculation, EMA smoothing, tick freeze, and time dilation
 * 4. bandwidth/ (10 modules) — Inbound/outbound throughput, compression ratios, and quota alerting
 * 5. diagnostics/ (10 modules) — Frametime profiling, 1% lows, crash logging, and master diagnostic HUD
 */
public final class TelemetryModuleCatalog {
    private static final List<Supplier<Module>> SUPPLIERS = new ArrayList<>();

    static {
        // --- 1. packet/ (10 modules) ---
        register(PacketLogger::new);
        register(PacketRateLimiter::new);
        register(PacketDumper::new);
        register(PacketFilter::new);
        register(PacketSequenceTracker::new);
        register(UnexpectedPacketDetector::new);
        register(PacketBurstBuffer::new);
        register(KeepAliveAlerter::new);
        register(CustomPayloadMonitor::new);
        register(PacketDelayEstimator::new);

        // --- 2. ping/ (10 modules) ---
        register(PingMonitor::new);
        register(PingStabilityIndex::new);
        register(JitterTracker::new);
        register(PingSpikeAlerter::new);
        register(MultiServerPingComparator::new);
        register(PingHistoryGraph::new);
        register(ServerHopLatencyTracker::new);
        register(EchoPacketTimer::new);
        register(PingQuantileHistogram::new);
        register(NetworkSmoothnessGauge::new);

        // --- 3. tps/ (10 modules) ---
        register(TpsMeter::new);
        register(EmaTpsSmoother::new);
        register(WorldTimeDriftDetector::new);
        register(ServerLagSpikeWarning::new);
        register(TickFreezeDetector::new);
        register(TickHealthIndicator::new);
        register(AverageTickTimeGauge::new);
        register(TpsHistoryChart::new);
        register(TickSkipCounter::new);
        register(TimeDilationMonitor::new);

        // --- 4. bandwidth/ (10 modules) ---
        register(BandwidthMeter::new);
        register(InboundThroughputGraph::new);
        register(OutboundThroughputGraph::new);
        register(PacketCompressionRatio::new);
        register(ChunkDataTransferMeter::new);
        register(BandwidthBudgetAlerter::new);
        register(NetworkTrafficPeakDetector::new);
        register(SocketQueueMonitor::new);
        register(EntityBandwidthProfiler::new);
        register(ProtocolOverheadMeter::new);

        // --- 5. diagnostics/ (10 modules) ---
        register(FrametimeProfiler::new);
        register(OnePercentLowTracker::new);
        register(ChunkLoadProfiler::new);
        register(DisconnectReasonLogger::new);
        register(ServerBrandIdentifier::new);
        register(PortalGodModeHelper::new);
        register(ResourcePackAudit::new);
        register(NetworkCrashReporter::new);
        register(TelemetryEventLogger::new);
        register(ClientDiagnosticHub::new);
    }

    private static void register(Supplier<Module> supplier) {
        SUPPLIERS.add(supplier);
    }

    /**
     * Registers all 50 Network & Telemetry modules into the target ModuleManager.
     */
    public static void registerAll(ModuleManager manager) {
        if (manager == null) {
            return;
        }
        for (Supplier<Module> supplier : SUPPLIERS) {
            manager.register(supplier.get());
        }
    }

    /**
     * Instantiates a fresh instance of each of the 50 Network & Telemetry modules.
     */
    public static List<Module> createAllInstances() {
        List<Module> list = new ArrayList<>(SUPPLIERS.size());
        for (Supplier<Module> supplier : SUPPLIERS) {
            list.add(supplier.get());
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * Returns the exact total module count registered in this catalog (50).
     */
    public static int getTotalModuleCount() {
        return SUPPLIERS.size();
    }

    private TelemetryModuleCatalog() {
    }
}
