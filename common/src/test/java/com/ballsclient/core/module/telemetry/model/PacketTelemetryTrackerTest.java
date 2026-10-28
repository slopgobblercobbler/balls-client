package com.ballsclient.core.module.telemetry.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

@DisplayName("PacketTelemetryTracker Domain Model Test")
class PacketTelemetryTrackerTest {

    @Test
    @DisplayName("Multi-Threaded Concurrency: 8 threads record 5,000 packets with zero loss")
    void testConcurrentPacketRecording() throws InterruptedException {
        PacketTelemetryTracker tracker = new PacketTelemetryTracker();
        int threadCount = 8;
        int packetsPerThread = 5000;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            executor.submit(() -> {
                try {
                    startLatch.await();
                    for (int i = 0; i < packetsPerThread; i++) {
                        String packetType = "PacketType" + (threadId % 4);
                        tracker.recordInbound(packetType, 64L);
                        tracker.recordOutbound(packetType, 32L);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        boolean finished = doneLatch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        assertThat(finished).isTrue();
        long expectedPackets = (long) threadCount * packetsPerThread;
        assertThat(tracker.getTotalInboundPackets()).isEqualTo(expectedPackets);
        assertThat(tracker.getTotalOutboundPackets()).isEqualTo(expectedPackets);
        assertThat(tracker.getTotalInboundBytes()).isEqualTo(expectedPackets * 64L);
        assertThat(tracker.getTotalOutboundBytes()).isEqualTo(expectedPackets * 32L);
    }

    @Test
    @DisplayName("Sliding Window Rate Accuracy: 100 packets across 500ms calculates to ~200 pkts/sec")
    void testSlidingWindowRate() {
        PacketTelemetryTracker tracker = new PacketTelemetryTracker();
        long baseTime = 1_000_000L;

        // Record 100 packets spaced uniformly over 500ms (1 packet every 5ms)
        for (int i = 0; i < 100; i++) {
            long timestamp = baseTime + (i * 5L);
            tracker.recordInbound("TestPacket", 50L, timestamp);
        }

        // Window query at baseTime + 500ms
        double packetRate = tracker.getInboundPacketRate(baseTime + 500L);
        assertThat(packetRate).isCloseTo(200.0, offset(25.0));

        double byteRate = tracker.getInboundByteRate(baseTime + 500L);
        assertThat(byteRate).isCloseTo(10_000.0, offset(1500.0));
    }

    @Test
    @DisplayName("Top Packet Ranking: Accurately sorts top contributors descending")
    void testTopPacketRanking() {
        PacketTelemetryTracker tracker = new PacketTelemetryTracker();

        for (int i = 0; i < 500; i++) {
            tracker.recordInbound("PlayerMoveC2SPacket", 32L);
        }
        for (int i = 0; i < 100; i++) {
            tracker.recordInbound("KeepAliveC2SPacket", 16L);
        }
        for (int i = 0; i < 10; i++) {
            tracker.recordInbound("ChatMessageC2SPacket", 128L);
        }

        List<Map.Entry<String, PacketTelemetryTracker.PacketTypeStats>> topByCount =
                tracker.getTopInboundPacketsByCount(3);
        assertThat(topByCount).hasSize(3);
        assertThat(topByCount.get(0).getKey()).isEqualTo("PlayerMoveC2SPacket");
        assertThat(topByCount.get(0).getValue().getCount()).isEqualTo(500L);
        assertThat(topByCount.get(1).getKey()).isEqualTo("KeepAliveC2SPacket");
        assertThat(topByCount.get(1).getValue().getCount()).isEqualTo(100L);
        assertThat(topByCount.get(2).getKey()).isEqualTo("ChatMessageC2SPacket");
        assertThat(topByCount.get(2).getValue().getCount()).isEqualTo(10L);

        List<Map.Entry<String, PacketTelemetryTracker.PacketTypeStats>> topByBytes =
                tracker.getTopInboundPacketsByBytes(3);
        assertThat(topByBytes.get(0).getKey()).isEqualTo("PlayerMoveC2SPacket"); // 500 * 32 = 16000 bytes
        assertThat(topByBytes.get(1).getKey()).isEqualTo("KeepAliveC2SPacket");  // 100 * 16 = 1600 bytes
        assertThat(topByBytes.get(2).getKey()).isEqualTo("ChatMessageC2SPacket"); // 10 * 128 = 1280 bytes
    }

    @Test
    @DisplayName("Reset State: Clears all maps, rates, and accumulators to zero")
    void testResetState() {
        PacketTelemetryTracker tracker = new PacketTelemetryTracker();
        tracker.recordInbound("ChunkDataPacket", 1024L);
        tracker.recordOutbound("TeleportConfirmPacket", 16L);

        assertThat(tracker.getTotalInboundPackets()).isEqualTo(1L);
        assertThat(tracker.getTotalOutboundPackets()).isEqualTo(1L);

        tracker.reset();
        assertThat(tracker.getTotalInboundPackets()).isEqualTo(0L);
        assertThat(tracker.getTotalInboundBytes()).isEqualTo(0L);
        assertThat(tracker.getTotalOutboundPackets()).isEqualTo(0L);
        assertThat(tracker.getTotalOutboundBytes()).isEqualTo(0L);
        assertThat(tracker.getInboundStats()).isEmpty();
        assertThat(tracker.getOutboundStats()).isEmpty();
    }
}
