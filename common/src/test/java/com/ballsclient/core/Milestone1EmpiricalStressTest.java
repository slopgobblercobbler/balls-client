package com.ballsclient.core;

import com.ballsclient.core.event.EventBus;
import com.ballsclient.core.event.EventPriority;
import com.ballsclient.core.event.PacketEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.setting.KeybindSetting;
import com.ballsclient.core.util.KeyUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("Milestone 1 Empirical Stress Test Suite")
class Milestone1EmpiricalStressTest {

    // -------------------------------------------------------------------------
    // Objective 1: KeyUtils extreme and boundary keycodes
    // -------------------------------------------------------------------------

    @ParameterizedTest(name = "Keycode {0} -> Expected Display ''{1}''")
    @CsvSource({
            "-1, NONE",
            "0, NONE",
            "1, KEY_1",
            "32, SPACE",
            "65, A",
            "344, RSHIFT",
            "10000, KEY_10000",
            "-2147483648, KEY_-2147483648",
            "2147483647, KEY_2147483647"
    })
    void testMandatedBoundaryKeycodes(int keyCode, String expectedName) {
        assertThat(KeyUtils.getKeyName(keyCode)).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("KeyUtils: Null keycode returns NONE without NPE")
    void testNullKeycode() {
        assertThat(KeyUtils.getKeyName(null)).isEqualTo("NONE");
    }

    @ParameterizedTest(name = "Negative keycode {0} -> KEY_{0}")
    @ValueSource(ints = {-2, -3, -10, -100, -9999})
    void testNegativeKeycodes(int negativeCode) {
        assertThat(KeyUtils.getKeyName(negativeCode)).isEqualTo("KEY_" + negativeCode);
    }

    @Test
    @DisplayName("KeyUtils: Printable ASCII and boundary ranges (31, 32, 126, 127)")
    void testAsciiBoundaries() {
        assertThat(KeyUtils.getKeyName(31)).isEqualTo("KEY_31");
        assertThat(KeyUtils.getKeyName(32)).isEqualTo("SPACE");
        assertThat(KeyUtils.getKeyName(33)).isEqualTo("!");
        assertThat(KeyUtils.getKeyName(126)).isEqualTo("~");
        assertThat(KeyUtils.getKeyName(127)).isEqualTo("KEY_127");
    }

    @Test
    @DisplayName("KeyUtils: Complete F-key sequence F1 through F25 (GLFW 290..314)")
    void testAllFunctionKeys() {
        for (int i = 1; i <= 25; i++) {
            assertThat(KeyUtils.getKeyName(289 + i)).isEqualTo("F" + i);
        }
    }

    @Test
    @DisplayName("KeyUtils: All Numpad keys (GLFW 320..336)")
    void testAllNumpadKeys() {
        for (int i = 0; i <= 9; i++) {
            assertThat(KeyUtils.getKeyName(320 + i)).isEqualTo("NUMPAD_" + i);
        }
        assertThat(KeyUtils.getKeyName(330)).isEqualTo("NUMPAD_DECIMAL");
        assertThat(KeyUtils.getKeyName(331)).isEqualTo("NUMPAD_DIVIDE");
        assertThat(KeyUtils.getKeyName(332)).isEqualTo("NUMPAD_MULTIPLY");
        assertThat(KeyUtils.getKeyName(333)).isEqualTo("NUMPAD_SUBTRACT");
        assertThat(KeyUtils.getKeyName(334)).isEqualTo("NUMPAD_ADD");
        assertThat(KeyUtils.getKeyName(335)).isEqualTo("NUMPAD_ENTER");
        assertThat(KeyUtils.getKeyName(336)).isEqualTo("NUMPAD_EQUAL");
    }

    @Test
    @DisplayName("KeyUtils: SDL Scancode aliases (224..231)")
    void testSdlScancodeAliases() {
        assertThat(KeyUtils.getKeyName(224)).isEqualTo("LCONTROL");
        assertThat(KeyUtils.getKeyName(225)).isEqualTo("LSHIFT");
        assertThat(KeyUtils.getKeyName(226)).isEqualTo("LALT");
        assertThat(KeyUtils.getKeyName(227)).isEqualTo("LGUI");
        assertThat(KeyUtils.getKeyName(228)).isEqualTo("RCONTROL");
        assertThat(KeyUtils.getKeyName(229)).isEqualTo("RSHIFT");
        assertThat(KeyUtils.getKeyName(230)).isEqualTo("RALT");
        assertThat(KeyUtils.getKeyName(231)).isEqualTo("RGUI");
    }

    // -------------------------------------------------------------------------
    // Objective 2: KeybindSetting state transitions (null, unbound, bound)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("KeybindSetting: Initial state with KEY_NONE (-1)")
    void testInitialUnboundState() {
        KeybindSetting setting = new KeybindSetting("MenuKey", "Menu toggle", KeybindSetting.KEY_NONE);

        assertThat(setting.getValue()).isEqualTo(-1);
        assertThat(setting.isBound()).isFalse();
        assertThat(setting.getDisplayName()).isEqualTo("NONE");
        assertThat(setting.matches(-1)).isFalse();
        assertThat(setting.matches(0)).isFalse();
        assertThat(setting.matches(344)).isFalse();
    }

    @Test
    @DisplayName("KeybindSetting: Unbound state with 0")
    void testZeroUnboundState() {
        KeybindSetting setting = new KeybindSetting("MenuKey", "Menu toggle", 0);

        assertThat(setting.getValue()).isEqualTo(0);
        assertThat(setting.isBound()).isFalse();
        assertThat(setting.getDisplayName()).isEqualTo("NONE");
        assertThat(setting.matches(0)).isFalse();
        assertThat(setting.matches(-1)).isFalse();
    }

    @Test
    @DisplayName("KeybindSetting: Null state handling and Setting.setValue(null) safety")
    void testNullState() throws Exception {
        KeybindSetting setting = new KeybindSetting("MenuKey", "Menu toggle", 65);
        assertThat(setting.isBound()).isTrue();

        // 1. Setting.setValue(null) is safely ignored by base Setting contract
        setting.setValue(null);
        assertThat(setting.getValue()).isEqualTo(65); // Preserves existing value

        // 2. Direct null value state (e.g. if uninitialized or injected)
        java.lang.reflect.Field valueField = com.ballsclient.core.setting.Setting.class.getDeclaredField("value");
        valueField.setAccessible(true);
        valueField.set(setting, null);

        assertThat(setting.getValue()).isNull();
        assertThat(setting.isBound()).isFalse();
        assertThat(setting.getDisplayName()).isEqualTo("NONE");
        assertThat(setting.matches(65)).isFalse();
        assertThat(setting.matches(0)).isFalse();
        assertThat(setting.matches(-1)).isFalse();
    }

    @Test
    @DisplayName("KeybindSetting: Bound state matching and display transitions")
    void testBoundStateTransitions() {
        KeybindSetting setting = new KeybindSetting("Zoom", "Zoom key", 67); // C
        assertThat(setting.isBound()).isTrue();
        assertThat(setting.getDisplayName()).isEqualTo("C");
        assertThat(setting.matches(67)).isTrue();
        assertThat(setting.matches(68)).isFalse();

        // Rebind to RSHIFT (344)
        AtomicBoolean listenerFired = new AtomicBoolean(false);
        setting.onChange(val -> listenerFired.set(true));

        setting.setValue(344);
        assertThat(listenerFired.get()).isTrue();
        assertThat(setting.isBound()).isTrue();
        assertThat(setting.getDisplayName()).isEqualTo("RSHIFT");
        assertThat(setting.matches(344)).isTrue();
        assertThat(setting.matches(67)).isFalse();

        // Rebind to SPACE (32)
        setting.setValue(32);
        assertThat(setting.isBound()).isTrue();
        assertThat(setting.getDisplayName()).isEqualTo("SPACE");
        assertThat(setting.matches(32)).isTrue();

        // Rebind to extreme key (10000)
        setting.setValue(10000);
        assertThat(setting.isBound()).isTrue();
        assertThat(setting.getDisplayName()).isEqualTo("KEY_10000");
        assertThat(setting.matches(10000)).isTrue();

        // Reset to unbound (-1)
        setting.setValue(KeybindSetting.KEY_NONE);
        assertThat(setting.isBound()).isFalse();
        assertThat(setting.getDisplayName()).isEqualTo("NONE");
        assertThat(setting.matches(-1)).isFalse();
    }

    @Test
    @DisplayName("KeybindSetting: JSON serialization round-trip and resilient error recovery")
    void testJsonSerializationResilience() {
        KeybindSetting setting = new KeybindSetting("Bind", "Test", 344);
        assertThat(setting.toJson()).isEqualTo(new JsonPrimitive(344));

        setting.fromJson(new JsonPrimitive(82)); // R
        assertThat(setting.getValue()).isEqualTo(82);
        assertThat(setting.getDisplayName()).isEqualTo("R");

        // Malformed or null JSON should not throw or corrupt existing state
        setting.fromJson(null);
        assertThat(setting.getValue()).isEqualTo(82);

        setting.fromJson(JsonNull.INSTANCE);
        assertThat(setting.getValue()).isEqualTo(82);

        setting.fromJson(new JsonObject());
        assertThat(setting.getValue()).isEqualTo(82);

        setting.fromJson(new JsonArray());
        assertThat(setting.getValue()).isEqualTo(82);

        setting.fromJson(new JsonPrimitive("not-a-number"));
        assertThat(setting.getValue()).isEqualTo(82);
    }

    // -------------------------------------------------------------------------
    // Objective 3: PacketEvent dispatch, cancellation, and payload retrieval
    // -------------------------------------------------------------------------

    static class MockPacket {
        final String id;
        final int sequence;

        MockPacket(String id, int sequence) {
            this.id = id;
            this.sequence = sequence;
        }
    }

    @Test
    @DisplayName("PacketEvent: Payload retrieval with exact type, supertype, mismatch, and null handling")
    void testPacketPayloadRetrieval() {
        MockPacket packet = new MockPacket("test_payload", 42);
        PacketEvent.Receive rx = new PacketEvent.Receive(packet);

        // Exact type
        assertThat(rx.getPacket()).isSameAs(packet);
        assertThat(rx.getPacket(MockPacket.class)).isSameAs(packet);
        assertThat(rx.getPacket(MockPacket.class).id).isEqualTo("test_payload");
        assertThat(rx.getPacket(MockPacket.class).sequence).isEqualTo(42);

        // Supertype Object.class
        assertThat(rx.getPacket(Object.class)).isSameAs(packet);

        // Mismatched types
        assertThat(rx.getPacket(String.class)).isNull();
        assertThat(rx.getPacket(Integer.class)).isNull();

        // Null class query does not throw NPE
        Class<?> nullClazz = null;
        assertThat((Object) rx.getPacket(nullClazz)).isNull();

        // Null packet payload does not throw NPE on typed query
        PacketEvent.Send txNull = new PacketEvent.Send(null);
        assertThat(txNull.getPacket()).isNull();
        assertThat(txNull.getPacket(MockPacket.class)).isNull();
        assertThat(txNull.getPacket(Object.class)).isNull();
        assertThat((Object) txNull.getPacket(nullClazz)).isNull();
    }

    @Test
    @DisplayName("PacketEvent: Base PacketEvent subscriber receives both Receive and Send events")
    void testBasePacketEventPolymorphicDispatch() {
        EventBus bus = new EventBus();
        List<String> receivedEvents = new ArrayList<>();

        Object polymorphicListener = new Object() {
            @Subscribe
            void onAnyPacket(PacketEvent e) {
                if (e instanceof PacketEvent.Receive) {
                    receivedEvents.add("BASE_RX:" + e.getPacket());
                } else if (e instanceof PacketEvent.Send) {
                    receivedEvents.add("BASE_TX:" + e.getPacket());
                }
            }

            @Subscribe
            void onReceiveOnly(PacketEvent.Receive e) {
                receivedEvents.add("SPECIFIC_RX:" + e.getPacket());
            }

            @Subscribe
            void onSendOnly(PacketEvent.Send e) {
                receivedEvents.add("SPECIFIC_TX:" + e.getPacket());
            }
        };

        bus.register(polymorphicListener);

        bus.post(new PacketEvent.Receive("incoming_1"));
        bus.post(new PacketEvent.Send("outgoing_1"));

        assertThat(receivedEvents).containsExactlyInAnyOrder(
                "BASE_RX:incoming_1",
                "SPECIFIC_RX:incoming_1",
                "BASE_TX:outgoing_1",
                "SPECIFIC_TX:outgoing_1"
        );
    }

    @Test
    @DisplayName("PacketEvent: events subpackage adapter class backwards compatibility")
    void testEventsAdapterSubpackageCompatibility() {
        EventBus bus = new EventBus();
        AtomicInteger count = new AtomicInteger(0);

        Object listener = new Object() {
            @Subscribe
            void onCoreReceive(PacketEvent.Receive e) {
                count.incrementAndGet();
            }
        };

        bus.register(listener);

        // Dispatch instance of events.PacketEvent.Receive (subclass of core PacketEvent.Receive)
        com.ballsclient.core.event.events.PacketEvent.Receive adaptedRx =
                new com.ballsclient.core.event.events.PacketEvent.Receive("adapted_inbound");

        bus.post(adaptedRx);
        assertThat(count.get()).isEqualTo(1);
    }

    @Test
    @DisplayName("PacketEvent: Cancellation cutoff at HIGHEST skips NORMAL but still notifies MONITOR")
    void testPacketEventCancellationPriorities() {
        EventBus bus = new EventBus();
        List<String> auditLog = new ArrayList<>();

        Object priorityInterceptor = new Object() {
            @Subscribe(priority = EventPriority.HIGHEST)
            void onHighest(PacketEvent.Receive e) {
                auditLog.add("HIGHEST_RX");
                e.setCancelled(true);
            }

            @Subscribe(priority = EventPriority.HIGH)
            void onHigh(PacketEvent.Receive e) {
                auditLog.add("HIGH_RX");
            }

            @Subscribe(priority = EventPriority.NORMAL)
            void onNormal(PacketEvent.Receive e) {
                auditLog.add("NORMAL_RX");
            }

            @Subscribe(priority = EventPriority.LOW)
            void onLow(PacketEvent.Receive e) {
                auditLog.add("LOW_RX");
            }

            @Subscribe(priority = EventPriority.LOWEST)
            void onLowest(PacketEvent.Receive e) {
                auditLog.add("LOWEST_RX");
            }

            @Subscribe(priority = EventPriority.MONITOR)
            void onMonitor(PacketEvent.Receive e) {
                auditLog.add("MONITOR_RX:cancelled=" + e.isCancelled());
            }
        };

        bus.register(priorityInterceptor);

        PacketEvent.Receive rx = new PacketEvent.Receive("intercept_target");
        bus.post(rx);

        assertThat(rx.isCancelled()).isTrue();
        // HIGH, NORMAL, LOW, LOWEST must be skipped; MONITOR must execute
        assertThat(auditLog).containsExactly("HIGHEST_RX", "MONITOR_RX:cancelled=true");
    }

    // -------------------------------------------------------------------------
    // Concurrency & High Throughput Stress Harness
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Concurrency: 16 threads dispatching 80,000 PacketEvents under concurrent listener churn")
    void testHighConcurrencyPacketDispatchStress() throws Exception {
        EventBus bus = new EventBus();
        int threadCount = 16;
        int eventsPerThread = 5000;
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);

        AtomicInteger totalRxReceived = new AtomicInteger(0);
        AtomicInteger totalTxSent = new AtomicInteger(0);
        AtomicInteger totalMonitored = new AtomicInteger(0);

        Object stableListener = new Object() {
            @Subscribe(priority = EventPriority.NORMAL)
            void onReceive(PacketEvent.Receive e) {
                totalRxReceived.incrementAndGet();
            }

            @Subscribe(priority = EventPriority.NORMAL)
            void onSend(PacketEvent.Send e) {
                totalTxSent.incrementAndGet();
            }

            @Subscribe(priority = EventPriority.MONITOR)
            void onMonitor(PacketEvent e) {
                totalMonitored.incrementAndGet();
            }
        };

        bus.register(stableListener);

        List<Future<?>> futures = new ArrayList<>();
        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            futures.add(pool.submit(() -> {
                try {
                    startLatch.await();
                    for (int i = 0; i < eventsPerThread; i++) {
                        if ((i & 1) == 0) {
                            bus.post(new PacketEvent.Receive("rx_" + threadId + "_" + i));
                        } else {
                            bus.post(new PacketEvent.Send("tx_" + threadId + "_" + i));
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }));
        }

        startLatch.countDown();
        for (Future<?> f : futures) {
            f.get(15, TimeUnit.SECONDS);
        }
        pool.shutdown();

        int expectedTotalEvents = threadCount * eventsPerThread;
        assertThat(totalRxReceived.get()).isEqualTo(expectedTotalEvents / 2);
        assertThat(totalTxSent.get()).isEqualTo(expectedTotalEvents / 2);
        assertThat(totalMonitored.get()).isEqualTo(expectedTotalEvents);
    }
}
