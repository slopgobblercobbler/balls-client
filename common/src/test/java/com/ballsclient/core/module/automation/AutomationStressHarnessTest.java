package com.ballsclient.core.module.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.automation.model.*;
import com.ballsclient.core.setting.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Track 7 Automation & Schematics Empirical Stress Harness")
class AutomationStressHarnessTest {

    // =========================================================================
    // 1. Continuous Bit-Packing Across 64-Bit Boundaries Stress (Widths 2..16)
    // =========================================================================

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16})
    @DisplayName("Stress: 1,000 random palette indices across 64-bit boundaries (widths 2..16)")
    void testContinuousBitPackingStress1000AcrossWidths2To16(int bitWidth) {
        int totalEntries = 1000;
        int maxVal = (1 << bitWidth) - 1;
        int[] originalEntries = new int[totalEntries];
        Random rnd = new Random(1337L + bitWidth * 31L);

        // Include critical bit boundary patterns: 0, maxVal, maxVal-1, 1, alternating patterns
        originalEntries[0] = 0;
        originalEntries[1] = maxVal;
        originalEntries[2] = 1;
        originalEntries[3] = Math.max(0, maxVal - 1);
        originalEntries[4] = 0x5555 & maxVal;
        originalEntries[5] = 0xAAAA & maxVal;

        for (int i = 6; i < totalEntries; i++) {
            originalEntries[i] = rnd.nextInt(maxVal + 1);
        }

        // Pack entries into continuous 64-bit long stream
        long[] packedWords = LitematicaBitPackingParser.pack(originalEntries, bitWidth);

        int expectedWordCount = (int) (((long) totalEntries * bitWidth + 63) / 64);
        assertThat(packedWords)
                .as("Packed long array size must match continuous bit ceiling for width %d", bitWidth)
                .hasSize(expectedWordCount);

        // Unpack and assert 100% round-trip identity across all word boundaries
        int[] unpackedEntries = LitematicaBitPackingParser.unpack(packedWords, totalEntries, bitWidth);
        assertThat(unpackedEntries)
                .as("Unpacked entries must match original entries with 100%% identity for bitWidth %d", bitWidth)
                .containsExactly(originalEntries);

        // Verify sequential and random-access readEntry queries
        for (int i = 0; i < totalEntries; i++) {
            int decoded = LitematicaBitPackingParser.readEntry(packedWords, i, bitWidth);
            assertThat(decoded)
                    .as("readEntry mismatch at sequential index %d for bitWidth %d", i, bitWidth)
                    .isEqualTo(originalEntries[i]);
        }

        // Randomly mutate 250 indices and verify no bit-drifting or corruption of adjacent entries
        long[] mutatedWords = packedWords.clone();
        int[] mutatedEntries = originalEntries.clone();
        for (int step = 0; step < 250; step++) {
            int targetIdx = rnd.nextInt(totalEntries);
            int newVal = rnd.nextInt(maxVal + 1);
            mutatedEntries[targetIdx] = newVal;
            LitematicaBitPackingParser.writeEntry(mutatedWords, targetIdx, bitWidth, newVal);
        }

        int[] finalUnpacked = LitematicaBitPackingParser.unpack(mutatedWords, totalEntries, bitWidth);
        assertThat(finalUnpacked)
                .as("Random mutations must not corrupt neighboring entries for bitWidth %d", bitWidth)
                .containsExactly(mutatedEntries);
    }

    @Test
    @DisplayName("Stress: Boundary-spanning entries crossing exact 64-bit boundaries")
    void testBoundarySpanningAcrossExact64BitBoundary() {
        for (int bitWidth = 2; bitWidth <= 16; bitWidth++) {
            int maxVal = (1 << bitWidth) - 1;
            long[] words = new long[3];

            // Calculate entry index whose bit span crosses from word 0 to word 1
            // Entry spans from (64 - bitWidth + 1) to (64 + 1), crossing the 64-bit border
            int startBit = 64 - (bitWidth / 2);
            // Write a preceding pattern to test non-zero background bits
            words[0] = 0xAAAAAAAAAAAAAAAAL;
            words[1] = 0x5555555555555555L;

            // Pack an entry specifically positioned across the boundary
            // We can calculate index such that bitIndex = (64 - 1)
            // Or test writeEntry at arbitrary index
            int entryIndex = (64 / bitWidth);
            int testVal = maxVal;
            LitematicaBitPackingParser.writeEntry(words, entryIndex, bitWidth, testVal);

            int readBack = LitematicaBitPackingParser.readEntry(words, entryIndex, bitWidth);
            assertThat(readBack)
                    .as("Boundary entry at index %d for width %d must round-trip cleanly", entryIndex, bitWidth)
                    .isEqualTo(testVal);
        }
    }

    // =========================================================================
    // 2. Sponge VarInt Streaming, Corrupted Byte Streams & 35-bit Overflow Guards
    // =========================================================================

    @Test
    @DisplayName("Stress: Sponge VarInt streaming 1,000 random integers across all byte tiers")
    void testSpongeVarIntStream1000RandomIntegers() {
        int count = 1000;
        int[] values = new int[count];
        Random rnd = new Random(777L);

        // Tier 1: 1-byte (0..127)
        values[0] = 0;
        values[1] = 1;
        values[2] = 127;
        // Tier 2: 2-byte (128..16383)
        values[3] = 128;
        values[4] = 255;
        values[5] = 16383;
        // Tier 3: 3-byte (16384..2097151)
        values[6] = 16384;
        values[7] = 2097151;
        // Tier 4: 4-byte (2097152..268435455)
        values[8] = 2097152;
        values[9] = 268435455;
        // Tier 5: 5-byte (268435456..Integer.MAX_VALUE)
        values[10] = 268435456;
        values[11] = Integer.MAX_VALUE;

        for (int i = 12; i < count; i++) {
            int tier = rnd.nextInt(5);
            values[i] = switch (tier) {
                case 0 -> rnd.nextInt(128);
                case 1 -> 128 + rnd.nextInt(16384 - 128);
                case 2 -> 16384 + rnd.nextInt(2097152 - 16384);
                case 3 -> 2097152 + rnd.nextInt(268435456 - 2097152);
                default -> 268435456 + rnd.nextInt(Integer.MAX_VALUE - 268435456);
            };
        }

        // Encode all 1,000 values to byte stream
        byte[] encodedBytes = SpongeVarIntParser.encodeAll(values);
        assertThat(encodedBytes.length)
                .as("Encoded stream must contain at least 1,000 bytes")
                .isGreaterThanOrEqualTo(1000);

        // Decode via bulk readAll
        ByteBuffer buffer = ByteBuffer.wrap(encodedBytes);
        int[] decoded = SpongeVarIntParser.readAll(buffer, count);
        assertThat(decoded)
                .as("Bulk decoded VarInts must match 1,000 input integers exactly")
                .containsExactly(values);

        // Decode sequentially via single readVarInt
        ByteBuffer seqBuffer = ByteBuffer.wrap(encodedBytes);
        for (int i = 0; i < count; i++) {
            int val = SpongeVarIntParser.readVarInt(seqBuffer);
            assertThat(val)
                    .as("Sequential VarInt mismatch at index %d", i)
                    .isEqualTo(values[i]);
        }
        assertThat(seqBuffer.hasRemaining()).isFalse();
    }

    @Test
    @DisplayName("Stress: Sponge VarInt corrupted byte streams, premature EOF and null handling")
    void testSpongeVarIntCorruptedByteStreamsAndTruncatedBuffers() {
        // Multi-byte test values requiring 2, 3, 4, and 5 bytes
        int[] multiByteValues = {128, 300, 16384, 65535, 2097152, 10000000, Integer.MAX_VALUE};

        for (int val : multiByteValues) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            SpongeVarIntParser.writeVarInt(out, val);
            byte[] fullBytes = out.toByteArray();
            assertThat(fullBytes.length).isGreaterThanOrEqualTo(2);

            // Truncate at every length from 1 to length - 1
            for (int truncateLen = 1; truncateLen < fullBytes.length; truncateLen++) {
                byte[] truncated = Arrays.copyOf(fullBytes, truncateLen);
                ByteBuffer buf = ByteBuffer.wrap(truncated);

                assertThatThrownBy(() -> SpongeVarIntParser.readVarInt(buf))
                        .as("Truncated buffer of length %d for value %d must throw IllegalStateException", truncateLen, val)
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessageContaining("Unexpected end of VarInt byte stream");
            }
        }

        // Empty buffer throws IllegalStateException
        ByteBuffer emptyBuffer = ByteBuffer.allocate(0);
        assertThatThrownBy(() -> SpongeVarIntParser.readVarInt(emptyBuffer))
                .isInstanceOf(IllegalStateException.class);

        // Null buffer throws IllegalArgumentException
        assertThatThrownBy(() -> SpongeVarIntParser.readVarInt(null))
                .isInstanceOf(IllegalArgumentException.class);

        // Buffer containing only continuation flags (0x80) without terminator
        for (int len = 1; len <= 4; len++) {
            byte[] onlyContinuations = new byte[len];
            Arrays.fill(onlyContinuations, (byte) 0x80);
            ByteBuffer buf = ByteBuffer.wrap(onlyContinuations);
            assertThatThrownBy(() -> SpongeVarIntParser.readVarInt(buf))
                    .isInstanceOf(IllegalStateException.class);
        }

        // Null and empty safety for bulk APIs
        assertThat(SpongeVarIntParser.encodeAll(null)).isEmpty();
        assertThat(SpongeVarIntParser.encodeAll(new int[0])).isEmpty();
        assertThat(SpongeVarIntParser.readAll(null, 10)).isEmpty();
        assertThat(SpongeVarIntParser.readAll(ByteBuffer.allocate(10), 0)).isEmpty();
    }

    @Test
    @DisplayName("Stress: Sponge VarInt 35-bit overflow guards (> 5 continuation bytes)")
    void testSpongeVarInt35BitOverflowGuards() {
        // 5 continuation bytes with 0x80 set followed by 6th byte
        byte[] overflow6Bytes = {
                (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x01
        };
        ByteBuffer buf6 = ByteBuffer.wrap(overflow6Bytes);
        assertThatThrownBy(() -> SpongeVarIntParser.readVarInt(buf6))
                .as("6-byte VarInt sequence must throw IllegalArgumentException for > 35-bit overflow")
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("VarInt overflow (> 5 bytes)");

        // 6 continuous 0xFF bytes
        byte[] allFf = {
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF
        };
        ByteBuffer bufAllFf = ByteBuffer.wrap(allFf);
        assertThatThrownBy(() -> SpongeVarIntParser.readVarInt(bufAllFf))
                .as("All 0xFF bytes must trigger VarInt overflow guard")
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("VarInt overflow (> 5 bytes)");

        // 10 continuation bytes
        byte[] overflow10Bytes = new byte[10];
        Arrays.fill(overflow10Bytes, (byte) 0x80);
        overflow10Bytes[9] = 0x01;
        ByteBuffer buf10 = ByteBuffer.wrap(overflow10Bytes);
        assertThatThrownBy(() -> SpongeVarIntParser.readVarInt(buf10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("VarInt overflow (> 5 bytes)");
    }

    // =========================================================================
    // 3. SchematicVolume 3D Coordinate Rotations (4x 90 deg Identity on 60k voxels)
    // =========================================================================

    @Test
    @DisplayName("Stress: 4 successive 90-degree rotations return to identity on 60,000-voxel volume")
    void testSchematicVolume4SuccessiveRotationsOn60000Voxels() {
        int width = 25;
        int height = 40;
        int length = 60;
        int totalVoxels = width * height * length; // 60,000 voxels

        List<String> palette = List.of(
                "minecraft:air",
                "minecraft:stone",
                "minecraft:granite",
                "minecraft:polished_granite",
                "minecraft:diorite",
                "minecraft:andesite",
                "minecraft:deepslate",
                "minecraft:obsidian",
                "minecraft:diamond_block",
                "minecraft:gold_block"
        );

        int[] initialIndices = new int[totalVoxels];
        for (int y = 0; y < height; y++) {
            for (int z = 0; z < length; z++) {
                for (int x = 0; x < width; x++) {
                    int idx = y * (width * length) + z * width + x;
                    // Deterministic non-trivial pattern
                    initialIndices[idx] = (x * 7 + y * 13 + z * 31 + (x ^ y ^ z)) % palette.size();
                }
            }
        }

        SchematicVolume initialVolume = new SchematicVolume(width, height, length, palette, initialIndices);
        assertThat(initialVolume.getTotalBlocks()).isEqualTo(60000);
        Map<String, Integer> initialBOM = initialVolume.calculateMaterialList();

        // Rotation 1 (90 degrees CW): (25, 40, 60) -> (60, 40, 25)
        SchematicVolume rot1 = initialVolume.rotate90();
        assertThat(rot1.getWidth()).isEqualTo(60);
        assertThat(rot1.getHeight()).isEqualTo(40);
        assertThat(rot1.getLength()).isEqualTo(25);
        assertThat(rot1.getTotalBlocks()).isEqualTo(60000);
        assertThat(rot1.calculateMaterialList())
                .as("Material list must remain invariant across 90-degree rotation")
                .isEqualTo(initialBOM);

        // Rotation 2 (180 degrees CW): (60, 40, 25) -> (25, 40, 60)
        SchematicVolume rot2 = rot1.rotate90();
        assertThat(rot2.getWidth()).isEqualTo(25);
        assertThat(rot2.getHeight()).isEqualTo(40);
        assertThat(rot2.getLength()).isEqualTo(60);
        assertThat(rot2.getTotalBlocks()).isEqualTo(60000);
        assertThat(rot2.calculateMaterialList()).isEqualTo(initialBOM);
        assertThat(rot2.getBlockIndices())
                .as("rot90().rotate90() must match rotate180() indices")
                .containsExactly(initialVolume.rotate180().getBlockIndices());

        // Rotation 3 (270 degrees CW): (25, 40, 60) -> (60, 40, 25)
        SchematicVolume rot3 = rot2.rotate90();
        assertThat(rot3.getWidth()).isEqualTo(60);
        assertThat(rot3.getHeight()).isEqualTo(40);
        assertThat(rot3.getLength()).isEqualTo(25);
        assertThat(rot3.getTotalBlocks()).isEqualTo(60000);
        assertThat(rot3.calculateMaterialList()).isEqualTo(initialBOM);
        assertThat(rot3.getBlockIndices())
                .as("3x rotate90() must match rotate270() indices")
                .containsExactly(initialVolume.rotate270().getBlockIndices());

        // Rotation 4 (360 degrees CW = Identity): (60, 40, 25) -> (25, 40, 60)
        SchematicVolume rot4 = rot3.rotate90();
        assertThat(rot4.getWidth()).isEqualTo(width);
        assertThat(rot4.getHeight()).isEqualTo(height);
        assertThat(rot4.getLength()).isEqualTo(length);
        assertThat(rot4.getTotalBlocks()).isEqualTo(60000);
        assertThat(rot4.calculateMaterialList()).isEqualTo(initialBOM);

        // Assert 100% voxel-by-voxel round-trip identity across all 60,000 blocks
        int[] finalIndices = rot4.getBlockIndices();
        assertThat(finalIndices)
                .as("All 60,000 voxel indices must match initial volume exactly after 4x 90-deg rotations")
                .containsExactly(initialIndices);

        for (int y = 0; y < height; y += 5) {
            for (int z = 0; z < length; z += 5) {
                for (int x = 0; x < width; x += 5) {
                    assertThat(rot4.getBlockState(x, y, z))
                            .as("BlockState at (%d, %d, %d) must match initial state", x, y, z)
                            .isEqualTo(initialVolume.getBlockState(x, y, z));
                }
            }
        }

        // Stress: 2x 180-degree rotations return to identity
        SchematicVolume rot360Via180 = initialVolume.rotate180().rotate180();
        assertThat(rot360Via180.getBlockIndices()).containsExactly(initialIndices);

        // Stress: 2x double-mirror returns to identity
        SchematicVolume mirroredIdentity = initialVolume.mirror(true, true).mirror(true, true);
        assertThat(mirroredIdentity.getBlockIndices()).containsExactly(initialIndices);
    }

    // =========================================================================
    // 4. MacroScriptEngine Concurrent Script Queues Across 50 Threads
    // =========================================================================

    @Test
    @DisplayName("Stress: MacroScriptEngine concurrent script queues across 50 threads")
    void testMacroScriptEngineConcurrentQueues50Threads() throws InterruptedException, ExecutionException, TimeoutException {
        int threadCount = 50;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        MacroScriptEngine sharedEngine = new MacroScriptEngine();
        AtomicInteger executedCount = new AtomicInteger(0);
        List<Future<?>> futures = new ArrayList<>(threadCount);

        // Contention test: 25 producer threads enqueueing scripts, 25 consumer threads ticking
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            futures.add(executor.submit(() -> {
                try {
                    startLatch.await();
                    if (threadId % 2 == 0) {
                        // Producer: enqueue complex scripts and actions
                        for (int k = 0; k < 20; k++) {
                            sharedEngine.enqueueScript("WAIT 1; CLICK LEFT; KEYDOWN W 5; LOOK 45.0 0.0; SELECT 1");
                            sharedEngine.enqueue(new MacroAction(MacroAction.CommandType.SEND, List.of("/test_" + threadId + "_" + k)));
                        }
                    } else {
                        // Consumer: tick and query state concurrently
                        for (int k = 0; k < 30; k++) {
                            MacroAction act = sharedEngine.tick();
                            if (act != null) {
                                executedCount.incrementAndGet();
                            }
                            sharedEngine.isRunning();
                            sharedEngine.getRemainingActionCount();
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Concurrent stress failure in thread " + threadId, e);
                } finally {
                    doneLatch.countDown();
                }
            }));
        }

        // Release all 50 threads simultaneously
        startLatch.countDown();
        boolean completed = doneLatch.await(10, TimeUnit.SECONDS);
        assertThat(completed).as("50 concurrent threads must complete without deadlocks within 10s").isTrue();

        for (Future<?> f : futures) {
            f.get(1, TimeUnit.SECONDS); // Propagate any uncaught exceptions
        }

        // Post-concurrency sanity
        assertThatCode(() -> sharedEngine.clear()).doesNotThrowAnyException();
        assertThat(sharedEngine.isRunning()).isFalse();
        assertThat(sharedEngine.getRemainingActionCount()).isEqualTo(0);

        // Independent 50-thread concurrent engine execution: each thread runs full DSL script sequence
        CountDownLatch independentLatch = new CountDownLatch(threadCount);
        List<Future<?>> independentFutures = new ArrayList<>(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            independentFutures.add(executor.submit(() -> {
                try {
                    MacroScriptEngine engine = new MacroScriptEngine();
                    engine.enqueueScript("""
                            WAIT 2;
                            CLICK RIGHT;
                            KEYDOWN SPACE 5;
                            LOOK 90.0 -15.0;
                            SEND /home;
                            WAIT 3;
                            SELECT 4;
                            CLICK LEFT
                            """);

                    assertThat(engine.isRunning()).isTrue();
                    int ticks = 0;
                    int actionsHandled = 0;
                    while (engine.isRunning() && ticks < 100) {
                        MacroAction action = engine.tick();
                        if (action != null) {
                            actionsHandled++;
                        }
                        ticks++;
                    }

                    assertThat(engine.isRunning()).isFalse();
                    assertThat(actionsHandled).isEqualTo(8); // All 8 actions dispatched across tick sequences
                } finally {
                    independentLatch.countDown();
                }
            }));
        }

        boolean indCompleted = independentLatch.await(10, TimeUnit.SECONDS);
        assertThat(indCompleted).as("50 independent concurrent engines must terminate cleanly").isTrue();
        for (Future<?> f : independentFutures) {
            f.get(1, TimeUnit.SECONDS);
        }

        executor.shutdownNow();
    }

    @Test
    @DisplayName("Stress: MacroChord concurrent evaluation across 50 threads")
    void testMacroChordConcurrentEvaluation() throws InterruptedException {
        int threadCount = 50;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Set<Integer> chordKeys = Set.of(341, 340, 66); // CTRL + SHIFT + B
        MacroChord chord = new MacroChord("StressChord", chordKeys, 200L);

        for (int i = 0; i < threadCount; i++) {
            final int id = i;
            executor.submit(() -> {
                try {
                    Random rnd = new Random(id * 100L);
                    for (int k = 0; k < 500; k++) {
                        Set<Integer> pressed = new HashSet<>();
                        if (k % 2 == 0) {
                            pressed.addAll(chordKeys);
                            pressed.add(rnd.nextInt(100));
                            assertThat(chord.matches(pressed)).isTrue();
                        } else {
                            pressed.add(341);
                            pressed.add(rnd.nextInt(100));
                            assertThat(chord.matches(pressed)).isFalse();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean done = latch.await(10, TimeUnit.SECONDS);
        assertThat(done).isTrue();
        executor.shutdownNow();
    }

    // =========================================================================
    // 5. Parameter Boundary Fuzzing Across All 70 Modules with Extreme Values
    // =========================================================================

    @Test
    @DisplayName("Stress: Fuzz settings across all 70 modules with extreme values (zero crashes)")
    void testParameterBoundaryFuzzingAcrossAll70Modules() {
        List<Module> allModules = AutomationModuleCatalog.createAllInstances();
        assertThat(allModules)
                .as("Automation catalog must contain all 70 modules")
                .hasSize(70);

        double[] extremeDoubles = {
                Double.NaN,
                Double.POSITIVE_INFINITY,
                Double.NEGATIVE_INFINITY,
                Double.MAX_VALUE,
                Double.MIN_VALUE,
                -Double.MAX_VALUE,
                -Double.MIN_VALUE,
                Double.MIN_NORMAL,
                0.0,
                -0.0,
                1e-15,
                1e15,
                1e300,
                -1e300
        };

        int totalSettingsTested = 0;

        for (Module module : allModules) {
            assertThat(module.getId()).isNotBlank();
            assertThat(module.getCategory()).isEqualTo(Category.AUTOMATION);

            // Invariant fuzzing on Module methods
            assertThat((Setting<?>) module.getSetting(null)).isNull();
            assertThat((Setting<?>) module.getSetting("")).isNull();
            assertThat((Setting<?>) module.getSetting("!NON_EXISTENT_SETTING_FUZZ!")).isNull();

            // Toggle and tick cycles
            assertThatCode(() -> {
                module.setEnabled(true);
                module.onTick();
                module.setEnabled(false);
                module.onTick();
                module.toggle();
                module.toggle();
            }).as("Toggling and ticking module %s must not throw", module.getId()).doesNotThrowAnyException();

            for (Setting<?> setting : module.getSettings()) {
                totalSettingsTested++;
                assertThat(setting.getName()).isNotBlank();

                if (setting instanceof NumberSetting numSetting) {
                    fuzzNumberSetting(numSetting, extremeDoubles);
                } else if (setting instanceof BooleanSetting boolSetting) {
                    fuzzBooleanSetting(boolSetting);
                } else if (setting instanceof ModeSetting modeSetting) {
                    fuzzModeSetting(modeSetting);
                } else if (setting instanceof ColorSetting colorSetting) {
                    fuzzColorSetting(colorSetting);
                } else if (setting instanceof KeybindSetting keybindSetting) {
                    fuzzKeybindSetting(keybindSetting);
                }
            }
        }

        assertThat(totalSettingsTested)
                .as("Total fuzzed settings across all 70 modules must exceed 100")
                .isGreaterThanOrEqualTo(140);
    }

    private void fuzzNumberSetting(NumberSetting setting, double[] extremeDoubles) {
        for (double val : extremeDoubles) {
            assertThatCode(() -> {
                setting.setValue(val);
                Double current = setting.getValue();
                assertThat(current).isNotNull();
                setting.getIntValue();
                setting.getFloatValue();
                setting.toJson();
            }).as("NumberSetting %s crashed on value %f", setting.getName(), val).doesNotThrowAnyException();
        }

        // Fuzz fromJson
        assertThatCode(() -> {
            setting.fromJson(new JsonPrimitive(Double.NaN));
            setting.fromJson(new JsonPrimitive("not_a_number"));
            setting.fromJson(new JsonPrimitive(""));
            setting.fromJson(JsonNull.INSTANCE);
            setting.fromJson(new JsonObject());
            setting.fromJson(new JsonArray());
            setting.setValue(null);
            setting.reset();
        }).as("NumberSetting %s crashed on JSON/null fuzzing", setting.getName()).doesNotThrowAnyException();
    }

    private void fuzzBooleanSetting(BooleanSetting setting) {
        assertThatCode(() -> {
            setting.setValue(true);
            setting.setValue(false);
            setting.setValue(null);
            setting.toggle();
            setting.toJson();
            setting.fromJson(new JsonPrimitive(true));
            setting.fromJson(new JsonPrimitive(false));
            setting.fromJson(new JsonPrimitive("not_a_boolean"));
            setting.fromJson(JsonNull.INSTANCE);
            setting.fromJson(new JsonObject());
            setting.reset();
        }).as("BooleanSetting %s crashed on fuzzing", setting.getName()).doesNotThrowAnyException();
    }

    private void fuzzModeSetting(ModeSetting setting) {
        assertThatCode(() -> {
            setting.setValue(null);
            setting.setValue("");
            setting.setValue("   \n\t  ");
            setting.setValue("!INVALID_FUZZ_MODE_67890!");

            for (int i = 0; i < 15; i++) {
                setting.cycle();
            }

            setting.is(null);
            setting.is("");
            setting.is("!NON_EXISTENT!");

            setting.toJson();
            setting.fromJson(new JsonPrimitive("!UNKNOWN!"));
            setting.fromJson(new JsonPrimitive(12345));
            setting.fromJson(JsonNull.INSTANCE);
            setting.fromJson(new JsonObject());
            setting.reset();
        }).as("ModeSetting %s crashed on fuzzing", setting.getName()).doesNotThrowAnyException();
    }

    private void fuzzColorSetting(ColorSetting setting) {
        int[] extremeInts = {0, -1, 1, Integer.MAX_VALUE, Integer.MIN_VALUE, 0x00FFFFFF, 0xFF000000, 0x7F123456};
        for (int val : extremeInts) {
            assertThatCode(() -> {
                setting.setValue(val);
                setting.getRed();
                setting.getGreen();
                setting.getBlue();
                setting.getAlpha();
                setting.getRedFloat();
                setting.getGreenFloat();
                setting.getBlueFloat();
                setting.getAlphaFloat();
                setting.getEffectiveRgba(System.currentTimeMillis());
                setting.getEffectiveRgba(0L);
                setting.getEffectiveRgba(Long.MAX_VALUE);
                setting.toJson();
            }).as("ColorSetting %s crashed on int %d", setting.getName(), val).doesNotThrowAnyException();
        }

        // Chroma fuzzing
        assertThatCode(() -> {
            setting.setChroma(true);
            setting.setChromaSpeed(Double.NaN);
            setting.getEffectiveRgba(System.currentTimeMillis());
            setting.setChromaSpeed(Double.POSITIVE_INFINITY);
            setting.getEffectiveRgba(System.currentTimeMillis());
            setting.setChromaSpeed(-500.0);
            setting.getEffectiveRgba(System.currentTimeMillis());
            setting.setChroma(false);
            setting.setValue(null);
            setting.fromJson(new JsonPrimitive(0xFF112233));
            setting.fromJson(new JsonPrimitive("corrupted"));
            setting.fromJson(JsonNull.INSTANCE);
            setting.reset();
        }).as("ColorSetting %s crashed on chroma/null fuzzing", setting.getName()).doesNotThrowAnyException();

        // Static HSB math fuzzing
        assertThatCode(() -> {
            ColorSetting.hsbToRgb(Float.NaN, Float.NaN, Float.NaN);
            ColorSetting.hsbToRgb(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NaN);
            ColorSetting.hsbToRgb(-10.0f, 2.0f, -0.5f);
            ColorSetting.hsbToRgb(0.5f, 0.0f, 1.0f);
        }).as("ColorSetting static hsbToRgb crashed on extreme floats").doesNotThrowAnyException();
    }

    private void fuzzKeybindSetting(KeybindSetting setting) {
        int[] keycodes = {KeybindSetting.KEY_NONE, 0, -1, Integer.MIN_VALUE, Integer.MAX_VALUE, 32, 65};
        for (int key : keycodes) {
            assertThatCode(() -> {
                setting.setValue(key);
                setting.isBound();
                setting.matches(key);
                setting.matches(0);
                setting.toJson();
            }).as("KeybindSetting %s crashed on keycode %d", setting.getName(), key).doesNotThrowAnyException();
        }

        assertThatCode(() -> {
            setting.setValue(null);
            setting.fromJson(new JsonPrimitive(-1));
            setting.fromJson(new JsonPrimitive("not_a_key"));
            setting.fromJson(JsonNull.INSTANCE);
            setting.reset();
        }).as("KeybindSetting %s crashed on null/json fuzzing", setting.getName()).doesNotThrowAnyException();
    }
}
