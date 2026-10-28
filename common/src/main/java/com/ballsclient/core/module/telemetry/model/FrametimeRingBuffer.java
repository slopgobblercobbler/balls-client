package com.ballsclient.core.module.telemetry.model;

import java.util.Arrays;

/**
 * FrametimeRingBuffer — High-performance O(1) circular buffer for nano-second frametime samples.
 * Computes rolling average FPS, 1% / 0.1% worst frametimes, and frametime standard deviation.
 */
public class FrametimeRingBuffer {
    public static final int DEFAULT_CAPACITY = 240;
    private static final double NANOS_PER_SECOND = 1_000_000_000.0;

    private final long[] buffer;
    private final int capacity;
    private int head = 0;
    private int size = 0;
    private long runningSumNanos = 0L;

    public FrametimeRingBuffer() {
        this(DEFAULT_CAPACITY);
    }

    public FrametimeRingBuffer(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must be at least 1");
        }
        this.capacity = capacity;
        this.buffer = new long[capacity];
    }

    /**
     * Records a single frame duration in nanoseconds. O(1) time complexity.
     * Non-positive values are ignored.
     */
    public synchronized void recordFrame(long durationNanos) {
        if (durationNanos <= 0) {
            return;
        }

        if (size == capacity) {
            runningSumNanos -= buffer[head];
        } else {
            size++;
        }

        buffer[head] = durationNanos;
        runningSumNanos += durationNanos;
        head = (head + 1) % capacity;
    }

    /**
     * Computes the mean frame time in nanoseconds across all current buffered samples.
     */
    public synchronized double getAverageFrametimeNanos() {
        if (size == 0) {
            return 0.0;
        }
        return (double) runningSumNanos / (double) size;
    }

    /**
     * Computes the rolling average frames per second (FPS).
     */
    public synchronized double getAverageFps() {
        if (size == 0) {
            return 0.0;
        }
        double avgNanos = getAverageFrametimeNanos();
        if (avgNanos <= 0.0) {
            return 0.0;
        }
        return NANOS_PER_SECOND / avgNanos;
    }

    /**
     * Computes the 1% low FPS, derived from the average of the worst (slowest) 1% frames.
     */
    public synchronized double getOnePercentLowFps() {
        return getPercentileLowFps(0.01);
    }

    /**
     * Computes the 0.1% low FPS, derived from the average of the worst (slowest) 0.1% frames.
     */
    public synchronized double getPointOnePercentLowFps() {
        return getPercentileLowFps(0.001);
    }

    /**
     * Helper to compute low FPS for a given fraction (e.g. 0.01 for 1%).
     */
    private double getPercentileLowFps(double fraction) {
        if (size == 0) {
            return 0.0;
        }
        long[] valid = getValidSamples();
        Arrays.sort(valid);

        int k = Math.max(1, (int) Math.ceil(size * fraction));
        if (k > size) {
            k = size;
        }

        long worstSum = 0L;
        for (int i = size - k; i < size; i++) {
            worstSum += valid[i];
        }

        double worstAvgNanos = (double) worstSum / (double) k;
        if (worstAvgNanos <= 0.0) {
            return 0.0;
        }
        return NANOS_PER_SECOND / worstAvgNanos;
    }

    /**
     * Calculates the population standard deviation of buffered frametimes in nanoseconds.
     */
    public synchronized double getStandardDeviationNanos() {
        if (size <= 1) {
            return 0.0;
        }
        double mean = getAverageFrametimeNanos();
        double sumSquaredDiff = 0.0;
        for (int i = 0; i < size; i++) {
            double diff = (double) buffer[i] - mean;
            sumSquaredDiff += diff * diff;
        }
        return Math.sqrt(sumSquaredDiff / (double) size);
    }

    /**
     * Returns a copy of all current valid samples in chronological order.
     */
    public synchronized long[] getValidSamples() {
        long[] valid = new long[size];
        if (size == 0) {
            return valid;
        }
        if (size < capacity) {
            System.arraycopy(buffer, 0, valid, 0, size);
        } else {
            // Buffer is full: oldest is at 'head', newest is at 'head - 1'
            int rightLength = capacity - head;
            System.arraycopy(buffer, head, valid, 0, rightLength);
            System.arraycopy(buffer, 0, valid, rightLength, head);
        }
        return valid;
    }

    public int getCapacity() {
        return capacity;
    }

    public synchronized int getSize() {
        return size;
    }

    public synchronized long getRunningSumNanos() {
        return runningSumNanos;
    }

    public synchronized void clear() {
        Arrays.fill(buffer, 0L);
        head = 0;
        size = 0;
        runningSumNanos = 0L;
    }
}
