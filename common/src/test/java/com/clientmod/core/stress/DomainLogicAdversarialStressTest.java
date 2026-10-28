package com.clientmod.core.stress;

import com.clientmod.core.config.ConfigManager;
import com.clientmod.core.config.ModConfig;
import com.clientmod.core.movement.AutoSprintController;
import com.clientmod.core.movement.MacroExecutionEngine;
import com.clientmod.core.telemetry.TelemetryTracker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("Domain Logic Adversarial Stress Test Suite")
class DomainLogicAdversarialStressTest {

    // =========================================================================
    // 1. ConfigManager Stress Tests
    // =========================================================================
    @Nested
    @DisplayName("ConfigManager Stress Tests")
    class ConfigManagerTests {

        @Test
        @DisplayName("Corrupt JSON: Truncated syntax falls back to default safely")
        void testTruncatedSyntaxFallback(@TempDir Path tempDir) throws IOException {
            Path file = tempDir.resolve("truncated.json");
            Files.writeString(file, "{\"enabled\": true, \"zoomFactor\":");

            ConfigManager manager = new ConfigManager(file);
            assertThatCode(manager::load).doesNotThrowAnyException();

            ModConfig config = manager.getConfig();
            assertThat(config).isNotNull();
            assertThat(config.enabled).isTrue();
            assertThat(config.zoomFactor).isEqualTo(0.25);
        }

        @Test
        @DisplayName("Corrupt JSON: Empty file (0 bytes) falls back to default")
        void testEmptyFileFallback(@TempDir Path tempDir) throws IOException {
            Path file = tempDir.resolve("empty.json");
            Files.createFile(file);

            ConfigManager manager = new ConfigManager(file);
            assertThatCode(manager::load).doesNotThrowAnyException();

            ModConfig config = manager.getConfig();
            assertThat(config).isNotNull();
            assertThat(config.enabled).isTrue();
        }

        @Test
        @DisplayName("Corrupt JSON: Whitespace only file falls back to default")
        void testWhitespaceOnlyFallback(@TempDir Path tempDir) throws IOException {
            Path file = tempDir.resolve("whitespace.json");
            Files.writeString(file, "   \n\t  \r\n   ");

            ConfigManager manager = new ConfigManager(file);
            assertThatCode(manager::load).doesNotThrowAnyException();

            ModConfig config = manager.getConfig();
            assertThat(config).isNotNull();
            assertThat(config.enabled).isTrue();
        }

        @Test
        @DisplayName("Corrupt JSON: Completely invalid tokens (random binary junk)")
        void testBinaryJunkFallback(@TempDir Path tempDir) throws IOException {
            Path file = tempDir.resolve("junk.json");
            Files.write(file, new byte[]{(byte) 0xDE, (byte) 0xAD, (byte) 0xBE, (byte) 0xEF, 0x00, 0x12, 0x34});

            ConfigManager manager = new ConfigManager(file);
            assertThatCode(manager::load).doesNotThrowAnyException();

            ModConfig config = manager.getConfig();
            assertThat(config).isNotNull();
            assertThat(config.enabled).isTrue();
        }

        @Test
        @DisplayName("Corrupt JSON: Array instead of object falls back to default")
        void testJsonArrayFallback(@TempDir Path tempDir) throws IOException {
            Path file = tempDir.resolve("array.json");
            Files.writeString(file, "[1, 2, 3, {\"enabled\": false}]");

            ConfigManager manager = new ConfigManager(file);
            assertThatCode(manager::load).doesNotThrowAnyException();

            ModConfig config = manager.getConfig();
            assertThat(config).isNotNull();
            assertThat(config.enabled).isTrue();
        }

        @Test
        @DisplayName("Corrupt JSON: Incompatible data types (string for double)")
        void testTypeMismatchFallback(@TempDir Path tempDir) throws IOException {
            Path file = tempDir.resolve("typemismatch.json");
            Files.writeString(file, "{\"zoomFactor\": \"invalid_double_not_a_number\"}");

            ConfigManager manager = new ConfigManager(file);
            assertThatCode(manager::load).doesNotThrowAnyException();

            ModConfig config = manager.getConfig();
            assertThat(config).isNotNull();
            // Should fallback to default zoomFactor (0.25)
            assertThat(config.zoomFactor).isEqualTo(0.25);
        }

        @Test
        @DisplayName("Partial JSON: Object with explicit null fields")
        void testExplicitNullFields(@TempDir Path tempDir) throws IOException {
            Path file = tempDir.resolve("nullfields.json");
            Files.writeString(file, "{\"sortOrder\": null, \"enabled\": true}");

            ConfigManager manager = new ConfigManager(file);
            assertThatCode(manager::load).doesNotThrowAnyException();

            ModConfig config = manager.getConfig();
            assertThat(config).isNotNull();
            // Note: sortOrder will be null because Gson deserializes null into String
            assertThat(config.enabled).isTrue();

            // copy() must not throw NPE when sortOrder is null
            assertThatCode(config::copy).doesNotThrowAnyException();
            ModConfig copy = config.copy();
            assertThat(copy.sortOrder).isNull();
        }

        @Test
        @DisplayName("Concurrency: 50 threads concurrently saving and loading ConfigManager")
        void testConcurrentSaveAndLoad(@TempDir Path tempDir) throws Exception {
            Path file = tempDir.resolve("concurrent.json");
            ConfigManager manager = new ConfigManager(file);
            manager.load();

            int threadCount = 50;
            ExecutorService pool = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(1);
            List<Future<Boolean>> futures = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {
                final int idx = i;
                futures.add(pool.submit(() -> {
                    latch.await();
                    for (int j = 0; j < 50; j++) {
                        if (idx % 2 == 0) {
                            ModConfig c = manager.getConfig().copy();
                            c.zoomFactor = 0.1 * (j % 5);
                            c.fullbright = (j % 2 == 0);
                            manager.setConfig(c);
                        } else {
                            manager.load();
                            ModConfig c = manager.getConfig();
                            assertThat(c).isNotNull();
                        }
                    }
                    return true;
                }));
            }

            latch.countDown();
            for (Future<Boolean> f : futures) {
                assertThat(f.get(10, TimeUnit.SECONDS)).isTrue();
            }
            pool.shutdown();

            // Final state must be valid and readable
            ConfigManager verifyManager = new ConfigManager(file);
            verifyManager.load();
            assertThat(verifyManager.getConfig()).isNotNull();
        }

        @Test
        @DisplayName("Atomic write: No partially written file read under heavy load")
        void testAtomicFileReplacementUnderLoad(@TempDir Path tempDir) throws Exception {
            Path file = tempDir.resolve("atomic.json");
            ConfigManager manager = new ConfigManager(file);
            manager.save();

            ExecutorService pool = Executors.newFixedThreadPool(16);
            AtomicBoolean stop = new AtomicBoolean(false);
            AtomicInteger parseErrors = new AtomicInteger(0);

            // Writer thread
            Future<?> writer = pool.submit(() -> {
                int count = 0;
                while (!stop.get()) {
                    ModConfig cfg = new ModConfig();
                    cfg.zoomFactor = (count % 100) / 100.0;
                    cfg.sortOrder = "ORDER_" + count;
                    manager.setConfig(cfg);
                    count++;
                }
            });

            // Reader threads reading directly from disk
            List<Future<?>> readers = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
                readers.add(pool.submit(() -> {
                    while (!stop.get()) {
                        try {
                            if (Files.exists(file)) {
                                String content = Files.readString(file);
                                if (!content.isEmpty() && (!content.startsWith("{") || !content.trim().endsWith("}"))) {
                                    parseErrors.incrementAndGet();
                                }
                            }
                        } catch (IOException ignored) {
                            // On Windows, brief file lock during move might happen
                        }
                    }
                }));
            }

            Thread.sleep(1000);
            stop.set(true);
            writer.get(5, TimeUnit.SECONDS);
            for (Future<?> r : readers) {
                r.get(5, TimeUnit.SECONDS);
            }
            pool.shutdown();

            assertThat(parseErrors.get()).isEqualTo(0);
        }

        @Test
        @DisplayName("Listeners: 50 concurrent listeners register without race condition")
        void testConcurrentListenerRegistration(@TempDir Path tempDir) throws Exception {
            Path file = tempDir.resolve("listeners.json");
            ConfigManager manager = new ConfigManager(file);
            AtomicInteger callCount = new AtomicInteger(0);

            int listenerCount = 50;
            ExecutorService pool = Executors.newFixedThreadPool(10);
            List<Future<?>> futures = new ArrayList<>();
            for (int i = 0; i < listenerCount; i++) {
                futures.add(pool.submit(() -> {
                    manager.registerListener(cfg -> callCount.incrementAndGet());
                }));
            }
            for (Future<?> f : futures) {
                f.get();
            }
            pool.shutdown();

            manager.save();
            assertThat(callCount.get()).isEqualTo(listenerCount);
        }

        @Test
        @DisplayName("ConfigManager: null configPath does not throw on load or save")
        void testNullPathSafety() {
            ConfigManager manager = new ConfigManager(null);
            assertThatCode(manager::load).doesNotThrowAnyException();
            assertThatCode(manager::save).doesNotThrowAnyException();
            assertThat(manager.getConfig()).isNotNull();
        }

        @Test
        @DisplayName("ConfigManager: One throwing listener does not prevent other listeners from executing")
        void testListenerExceptionIsolation(@TempDir Path tempDir) {
            Path file = tempDir.resolve("faulty_listeners.json");
            ConfigManager manager = new ConfigManager(file);
            AtomicBoolean healthyListenerExecuted = new AtomicBoolean(false);

            // Register faulty listener first
            manager.registerListener(cfg -> {
                throw new RuntimeException("Simulated faulty listener crash");
            });

            // Register healthy listener second
            manager.registerListener(cfg -> {
                healthyListenerExecuted.set(true);
            });

            // Save should notify both without crashing
            assertThatCode(manager::save).doesNotThrowAnyException();
            assertThat(healthyListenerExecuted.get()).isTrue();
        }
    }

    // =========================================================================
    // 2. TelemetryTracker Stress Tests
    // =========================================================================
    @Nested
    @DisplayName("TelemetryTracker Stress Tests")
    class TelemetryTrackerTests {

        @Test
        @DisplayName("Frame timing: Zero and negative durations are safely ignored")
        void testZeroAndNegativeFrameTimes() {
            TelemetryTracker tracker = new TelemetryTracker();

            tracker.recordFrame(0);
            tracker.recordFrame(-1);
            tracker.recordFrame(-1_000_000_000L);
            tracker.recordFrame(Long.MIN_VALUE);

            assertThat(tracker.getAverageFps()).isEqualTo(0.0);
            assertThat(tracker.getOnePercentLowFps()).isEqualTo(0.0);
        }

        @Test
        @DisplayName("Frame timing: Extreme spike (10,000ms / 10s) does not crash or produce negative FPS")
        void testExtremeSpikeFrame() {
            TelemetryTracker tracker = new TelemetryTracker();

            // 60 normal frames (16.6ms)
            for (int i = 0; i < 60; i++) {
                tracker.recordFrame(16_666_666L);
            }
            // Extreme 10-second spike
            tracker.recordFrame(10_000_000_000L);

            double avgFps = tracker.getAverageFps();
            double onePercent = tracker.getOnePercentLowFps();

            assertThat(avgFps).isGreaterThan(0.0).isLessThan(60.0);
            assertThat(onePercent).isGreaterThan(0.0).isLessThan(1.0);
        }

        @Test
        @DisplayName("Frame timing: 10,000 frames rapid insertion maintains MAX_FRAME_SAMPLES bounded window")
        void testFrameQueueBoundedMemory() {
            TelemetryTracker tracker = new TelemetryTracker();

            for (int i = 0; i < 10_000; i++) {
                tracker.recordFrame(16_666_666L);
            }

            // Exactly 60 FPS should be computed across the bounded window of 120 samples
            assertThat(tracker.getAverageFps()).isBetween(59.9, 60.1);
            assertThat(tracker.getOnePercentLowFps()).isBetween(59.9, 60.1);
        }

        @Test
        @DisplayName("1% Low: Empty window (< 10 frames) cleanly falls back to average FPS")
        void testOnePercentLowWithFewFrames() {
            TelemetryTracker tracker = new TelemetryTracker();

            for (int i = 0; i < 9; i++) {
                tracker.recordFrame(16_666_666L);
            }

            double avgFps = tracker.getAverageFps();
            double lowFps = tracker.getOnePercentLowFps();

            assertThat(lowFps).isEqualTo(avgFps);
            assertThat(lowFps).isBetween(59.9, 60.1);
        }

        @Test
        @DisplayName("1% Low: Identical frames result in averageFps == onePercentLowFps")
        void testOnePercentLowIdenticalFrames() {
            TelemetryTracker tracker = new TelemetryTracker();

            for (int i = 0; i < 120; i++) {
                tracker.recordFrame(20_000_000L); // 50 FPS
            }

            assertThat(tracker.getAverageFps()).isBetween(49.9, 50.1);
            assertThat(tracker.getOnePercentLowFps()).isBetween(49.9, 50.1);
        }

        @Test
        @DisplayName("TPS: Packet with identical timestamp (millisDelta == 0) does not divide by zero")
        void testTpsZeroMillisDelta() {
            TelemetryTracker tracker = new TelemetryTracker();

            tracker.recordTimePacket(1000L, 5000L);
            // Second packet at exact same timestamp
            assertThatCode(() -> tracker.recordTimePacket(1020L, 5000L)).doesNotThrowAnyException();

            // Default TPS should not become NaN or Infinity
            assertThat(tracker.getEstimatedTps()).isEqualTo(20.0);
        }

        @Test
        @DisplayName("TPS: Backwards world time (e.g. /time set 0) does not produce negative TPS")
        void testTpsBackwardsWorldTime() {
            TelemetryTracker tracker = new TelemetryTracker();

            tracker.recordTimePacket(10000L, 1000L);
            // Time set to 0
            tracker.recordTimePacket(0L, 2000L);

            assertThat(tracker.getEstimatedTps()).isEqualTo(20.0);

            // Subsequent normal packet advances from 0 to 20
            tracker.recordTimePacket(20L, 3000L);
            assertThat(tracker.getEstimatedTps()).isBetween(19.0, 20.0);
        }

        @Test
        @DisplayName("TPS: Massive tick surge (10,000 ticks in 1 sec) clamps to 20.0 max TPS")
        void testTpsClampingOnTickSurge() {
            TelemetryTracker tracker = new TelemetryTracker();

            tracker.recordTimePacket(1000L, 1000L);
            // 10,000 ticks catchup in 1000ms
            tracker.recordTimePacket(11000L, 2000L);

            assertThat(tracker.getEstimatedTps()).isLessThanOrEqualTo(20.0);
            assertThat(tracker.getEstimatedTps()).isEqualTo(20.0);
        }

        @Test
        @DisplayName("TPS: Missed packets / severe server freeze (0 ticks in 10,000ms) drops TPS via EMA")
        void testTpsSevereFreeze() {
            TelemetryTracker tracker = new TelemetryTracker();

            tracker.recordTimePacket(1000L, 1000L);
            // Freeze for 10 seconds, only 10 ticks elapsed
            tracker.recordTimePacket(1010L, 11000L); // 1 tick per sec = 1.0 TPS instant

            // EMA: 0.2 * 1.0 + 0.8 * 20.0 = 0.2 + 16.0 = 16.2
            assertThat(tracker.getEstimatedTps()).isBetween(16.1, 16.3);
        }

        @Test
        @DisplayName("Concurrency: Multi-threaded simultaneous frame and packet recording")
        void testConcurrentTelemetryAccess() throws Exception {
            TelemetryTracker tracker = new TelemetryTracker();
            ExecutorService pool = Executors.newFixedThreadPool(16);
            CountDownLatch latch = new CountDownLatch(1);
            List<Future<?>> futures = new ArrayList<>();

            for (int i = 0; i < 8; i++) {
                final int idx = i;
                futures.add(pool.submit(() -> {
                    latch.await();
                    for (int j = 0; j < 500; j++) {
                        tracker.recordFrame(16_666_666L + (idx * 1000L));
                        double avg = tracker.getAverageFps();
                        double low = tracker.getOnePercentLowFps();
                        assertThat(avg).isGreaterThan(0.0);
                        assertThat(low).isGreaterThan(0.0);
                    }
                    return null;
                }));
            }

            for (int i = 0; i < 8; i++) {
                futures.add(pool.submit(() -> {
                    latch.await();
                    for (int j = 0; j < 500; j++) {
                        tracker.recordTimePacket(1000L + (j * 20L), 10_000L + (j * 1000L));
                        double tps = tracker.getEstimatedTps();
                        assertThat(tps).isBetween(0.0, 20.0);
                    }
                    return null;
                }));
            }

            latch.countDown();
            for (Future<?> f : futures) {
                f.get(10, TimeUnit.SECONDS);
            }
            pool.shutdown();
        }

        @Test
        @DisplayName("TPS: Relative timestamp starting at 0 ignores packet until nowMillis > 0")
        void testTpsZeroEpochPacketHandling() {
            TelemetryTracker tracker = new TelemetryTracker();

            // Packet 1 at relative t=0
            tracker.recordTimePacket(1000L, 0L);
            assertThat(tracker.getEstimatedTps()).isEqualTo(20.0);

            // Packet 2 at relative t=1000 with 10 ticks elapsed (would be 10 TPS)
            // But because lastTimePacketMillis was 0 and line 59 requires lastTimePacketMillis > 0,
            // this packet is skipped!
            tracker.recordTimePacket(1010L, 1000L);
            assertThat(tracker.getEstimatedTps()).isEqualTo(20.0); // Stays 20.0 (skipped)

            // Packet 3 at relative t=2000 with 10 ticks elapsed
            // Now lastTimePacketMillis is 1000 > 0, so it calculates instant TPS = 10.0!
            tracker.recordTimePacket(1020L, 2000L);
            // EMA: 0.2 * 10.0 + 0.8 * 20.0 = 2.0 + 16.0 = 18.0
            assertThat(tracker.getEstimatedTps()).isBetween(17.9, 18.1);
        }

        @Test
        @DisplayName("TPS: Multiple packets with 0 ticks advance correctly update TPS downwards")
        void testTpsConsecutiveZeroTickPackets() {
            TelemetryTracker tracker = new TelemetryTracker();

            tracker.recordTimePacket(1000L, 1000L);

            // Server frozen: 3 packets arrive 1 sec apart with 0 ticks advanced
            tracker.recordTimePacket(1000L, 2000L); // 0.2*0 + 0.8*20 = 16.0
            assertThat(tracker.getEstimatedTps()).isBetween(15.9, 16.1);

            tracker.recordTimePacket(1000L, 3000L); // 0.2*0 + 0.8*16 = 12.8
            assertThat(tracker.getEstimatedTps()).isBetween(12.7, 12.9);

            tracker.recordTimePacket(1000L, 4000L); // 0.2*0 + 0.8*12.8 = 10.24
            assertThat(tracker.getEstimatedTps()).isBetween(10.2, 10.3);
        }
    }

    // =========================================================================
    // 3. AutoSprintController Stress Tests
    // =========================================================================
    @Nested
    @DisplayName("AutoSprintController Stress Tests")
    class AutoSprintControllerTests {

        @Test
        @DisplayName("Rapid state toggling: 10,000 rapid enable/disable cycles remain consistent")
        void testRapidStateToggling() {
            AutoSprintController controller = new AutoSprintController();

            for (int i = 0; i < 10_000; i++) {
                boolean enable = (i % 2 == 0);
                controller.setEnabled(enable);
                assertThat(controller.isEnabled()).isEqualTo(enable);

                controller.onTick(true);
                assertThat(controller.shouldSprint()).isEqualTo(enable);

                controller.onTick(false);
                assertThat(controller.shouldSprint()).isFalse();
            }
        }

        @Test
        @DisplayName("State Invariant: Disabling AutoSprint immediately cancels active sprint")
        void testDisablingCancelsSprintImmediately() {
            AutoSprintController controller = new AutoSprintController();
            controller.setEnabled(true);
            controller.onTick(true);
            assertThat(controller.shouldSprint()).isTrue();

            // Disable without calling onTick
            controller.setEnabled(false);
            // Invariant: shouldSprint must be false immediately
            assertThat(controller.shouldSprint()).isFalse();
        }

        @Test
        @DisplayName("State Invariant: Enabling does not activate sprint without forward pressed")
        void testEnablingDoesNotSprintWithoutForward() {
            AutoSprintController controller = new AutoSprintController();
            controller.onTick(false);

            controller.setEnabled(true);
            assertThat(controller.shouldSprint()).isFalse();

            controller.onTick(false);
            assertThat(controller.shouldSprint()).isFalse();

            controller.onTick(true);
            assertThat(controller.shouldSprint()).isTrue();
        }
    }

    // =========================================================================
    // 4. MacroExecutionEngine Stress Tests
    // =========================================================================
    @Nested
    @DisplayName("MacroExecutionEngine Stress Tests")
    class MacroExecutionEngineTests {

        @Test
        @DisplayName("Queue handling: null actions are safely discarded")
        void testNullActionHandling() {
            MacroExecutionEngine engine = new MacroExecutionEngine();
            assertThatCode(() -> engine.queueAction(null)).doesNotThrowAnyException();
            assertThat(engine.getQueueSize()).isEqualTo(0);

            // Tick with empty queue does not throw
            assertThatCode(engine::onTick).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Queue Stress: 50,000 actions queued and drained")
        void testLargeQueueThroughput() {
            MacroExecutionEngine engine = new MacroExecutionEngine();
            int actionCount = 50_000;
            AtomicInteger executed = new AtomicInteger(0);

            for (int i = 0; i < actionCount; i++) {
                engine.queueAction(executed::incrementAndGet);
            }
            assertThat(engine.getQueueSize()).isEqualTo(actionCount);

            // Drain all actions
            for (int i = 0; i < actionCount; i++) {
                engine.onTick();
            }

            assertThat(executed.get()).isEqualTo(actionCount);
            assertThat(engine.getQueueSize()).isEqualTo(0);
        }

        @Test
        @DisplayName("Delay Handling: Negative delay is clamped to zero")
        void testNegativeTickDelay() {
            MacroExecutionEngine engine = new MacroExecutionEngine();
            AtomicBoolean executed = new AtomicBoolean(false);

            engine.setTickDelay(-10);
            engine.queueAction(() -> executed.set(true));

            // Should execute immediately on tick since delay clamped to 0
            engine.onTick();
            assertThat(executed.get()).isTrue();
        }

        @Test
        @DisplayName("Clear: Purging engine resets delay and empties queue")
        void testEngineClear() {
            MacroExecutionEngine engine = new MacroExecutionEngine();
            AtomicBoolean executed = new AtomicBoolean(false);

            engine.queueAction(() -> executed.set(true));
            engine.setTickDelay(10);
            assertThat(engine.getQueueSize()).isEqualTo(1);

            engine.clear();
            assertThat(engine.getQueueSize()).isEqualTo(0);

            // Ticking now does not execute cleared action
            engine.onTick();
            assertThat(executed.get()).isFalse();
        }

        @Test
        @DisplayName("Reentrancy: Action that enqueues another action during execution")
        void testReentrantActionQueuing() {
            MacroExecutionEngine engine = new MacroExecutionEngine();
            AtomicInteger counter = new AtomicInteger(0);

            engine.queueAction(() -> {
                counter.incrementAndGet();
                engine.queueAction(counter::incrementAndGet);
            });

            engine.onTick();
            assertThat(counter.get()).isEqualTo(1);
            assertThat(engine.getQueueSize()).isEqualTo(1);

            engine.onTick();
            assertThat(counter.get()).isEqualTo(2);
            assertThat(engine.getQueueSize()).isEqualTo(0);
        }

        @Test
        @DisplayName("Concurrency: Multi-threaded producers enqueuing while tick loop drains")
        void testConcurrentQueueAndDrain() throws Exception {
            MacroExecutionEngine engine = new MacroExecutionEngine();
            int producerThreads = 10;
            int actionsPerProducer = 1000;
            AtomicInteger executed = new AtomicInteger(0);

            ExecutorService pool = Executors.newFixedThreadPool(producerThreads + 1);
            CountDownLatch latch = new CountDownLatch(1);
            List<Future<?>> futures = new ArrayList<>();

            for (int i = 0; i < producerThreads; i++) {
                futures.add(pool.submit(() -> {
                    latch.await();
                    for (int j = 0; j < actionsPerProducer; j++) {
                        engine.queueAction(executed::incrementAndGet);
                    }
                    return null;
                }));
            }

            latch.countDown();
            for (Future<?> f : futures) {
                f.get(10, TimeUnit.SECONDS);
            }

            // Drain all queued actions
            while (engine.getQueueSize() > 0) {
                engine.onTick();
            }

            pool.shutdown();
            assertThat(executed.get()).isEqualTo(producerThreads * actionsPerProducer);
        }

        @Test
        @DisplayName("Exception Handling: Uncaught exception in MacroAction bubbles out of onTick")
        void testMacroActionExceptionBubbling() {
            MacroExecutionEngine engine = new MacroExecutionEngine();
            engine.queueAction(() -> {
                throw new IllegalStateException("Simulated macro action crash");
            });

            // Action is queued
            assertThat(engine.getQueueSize()).isEqualTo(1);

            // onTick does not swallow the exception; it bubbles up to caller
            org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, engine::onTick);

            // Note: After throwing, the failed action was already polled and removed from queue
            assertThat(engine.getQueueSize()).isEqualTo(0);
        }
    }
}
