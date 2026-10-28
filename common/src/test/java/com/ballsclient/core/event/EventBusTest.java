package com.ballsclient.core.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("EventBus Prioritized Dispatch & Cancellation Test Suite")
class EventBusTest {

    @Test
    @DisplayName("Dispatch Order: Prioritized execution order (HIGHEST -> HIGH -> NORMAL -> LOW -> LOWEST -> MONITOR)")
    void testPriorityOrderDispatch() {
        EventBus bus = new EventBus();
        List<String> executionOrder = new ArrayList<>();

        Object listener = new Object() {
            @Subscribe(priority = EventPriority.LOW)
            void onLow(TickEvent e) { executionOrder.add("LOW"); }

            @Subscribe(priority = EventPriority.HIGHEST)
            void onHighest(TickEvent e) { executionOrder.add("HIGHEST"); }

            @Subscribe(priority = EventPriority.HIGH)
            void onHigh(TickEvent e) { executionOrder.add("HIGH"); }

            @Subscribe(priority = EventPriority.NORMAL)
            void onNormal(TickEvent e) { executionOrder.add("NORMAL"); }

            @Subscribe(priority = EventPriority.LOWEST)
            void onLowest(TickEvent e) { executionOrder.add("LOWEST"); }

            @Subscribe(priority = EventPriority.MONITOR)
            void onMonitor(TickEvent e) { executionOrder.add("MONITOR"); }
        };

        bus.register(listener);
        bus.post(new TickEvent(TickEvent.Phase.POST));

        assertThat(executionOrder).containsExactly(
                "HIGHEST", "HIGH", "NORMAL", "LOW", "LOWEST", "MONITOR"
        );
    }

    @Test
    @DisplayName("Cancellation: Cancelling event skips lower priority handlers but still notifies MONITOR")
    void testCancellationCutoff() {
        EventBus bus = new EventBus();
        List<String> executed = new ArrayList<>();

        Object listener = new Object() {
            @Subscribe(priority = EventPriority.HIGHEST)
            void onHighest(KeyEvent e) {
                executed.add("HIGHEST");
                e.setCancelled(true); // Intercept and cancel
            }

            @Subscribe(priority = EventPriority.HIGH)
            void onHigh(KeyEvent e) {
                executed.add("HIGH"); // Should be skipped
            }

            @Subscribe(priority = EventPriority.NORMAL)
            void onNormal(KeyEvent e) {
                executed.add("NORMAL"); // Should be skipped
            }

            @Subscribe(priority = EventPriority.MONITOR)
            void onMonitor(KeyEvent e) {
                executed.add("MONITOR"); // Must still execute
            }
        };

        bus.register(listener);
        KeyEvent event = new KeyEvent(65, 0, KeyEvent.ACTION_PRESS, 0);
        bus.post(event);

        assertThat(event.isCancelled()).isTrue();
        assertThat(executed).containsExactly("HIGHEST", "MONITOR");
    }

    @Test
    @DisplayName("Deregistration: Unregistered listeners no longer receive posted events")
    void testListenerDeregistration() {
        EventBus bus = new EventBus();
        AtomicInteger callCount = new AtomicInteger(0);

        Object listener = new Object() {
            @Subscribe
            void onTick(TickEvent e) {
                callCount.incrementAndGet();
            }
        };

        bus.register(listener);
        bus.post(new TickEvent(TickEvent.Phase.PRE));
        assertThat(callCount.get()).isEqualTo(1);

        bus.unregister(listener);
        bus.post(new TickEvent(TickEvent.Phase.PRE));
        assertThat(callCount.get()).isEqualTo(1); // Not called again
    }

    @Test
    @DisplayName("Exception Isolation: Failing subscriber does not prevent execution of other listeners")
    void testExceptionIsolation() {
        EventBus bus = new EventBus();
        AtomicInteger successCounter = new AtomicInteger(0);

        Object faultyListener = new Object() {
            @Subscribe(priority = EventPriority.HIGHEST)
            void onHighest(TickEvent e) {
                throw new RuntimeException("Simulated listener crash");
            }
        };

        Object healthyListener = new Object() {
            @Subscribe(priority = EventPriority.NORMAL)
            void onNormal(TickEvent e) {
                successCounter.incrementAndGet();
            }
        };

        bus.register(faultyListener);
        bus.register(healthyListener);

        assertThatCode(() -> bus.post(new TickEvent(TickEvent.Phase.POST)))
                .doesNotThrowAnyException();

        assertThat(successCounter.get()).isEqualTo(1);
    }

    @Test
    @DisplayName("Concurrency: Concurrent event posting and handler registrations do not throw CME")
    void testConcurrentEventDispatch() throws Exception {
        EventBus bus = new EventBus();
        int threadCount = 10;
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger totalEventsHandled = new AtomicInteger(0);

        List<Object> listeners = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            listeners.add(new Object() {
                @Subscribe
                void onTick(TickEvent e) {
                    totalEventsHandled.incrementAndGet();
                }
            });
        }

        for (Object l : listeners) {
            bus.register(l);
        }

        List<java.util.concurrent.Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            futures.add(pool.submit(() -> {
                try {
                    latch.await();
                    for (int j = 0; j < 50; j++) {
                        bus.post(new TickEvent(TickEvent.Phase.PRE));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }));
        }

        latch.countDown();
        for (java.util.concurrent.Future<?> f : futures) {
            f.get(5, TimeUnit.SECONDS);
        }
        pool.shutdown();

        assertThat(totalEventsHandled.get()).isEqualTo(threadCount * 50 * listeners.size());
    }
}
