package com.ballsclient.core.module.telemetry.model;

import java.util.Arrays;

/**
 * PingStatistics — Computes rolling round-trip latency statistics, RFC 3550 jitter,
 * quantile histograms (P50, P90, P95, P99), and connection stability index score.
 */
public class PingStatistics {
    public static final int DEFAULT_CAPACITY = 60;

    private final double[] samples;
    private final int capacity;
    private int head = 0;
    private int size = 0;
    private double runningSum = 0.0;
    private double rfc3550Jitter = 0.0;
    private double lastSample = -1.0;

    public PingStatistics() {
        this(DEFAULT_CAPACITY);
    }

    public PingStatistics(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must be at least 1");
        }
        this.capacity = capacity;
        this.samples = new double[capacity];
    }

    /**
     * Records a round-trip latency sample in milliseconds.
     * Computes RFC 3550 jitter and updates rolling statistics.
     * Negative values are ignored.
     */
    public synchronized void recordSample(double latencyMs) {
        if (latencyMs < 0.0) {
            return;
        }

        if (lastSample >= 0.0) {
            double delta = Math.abs(latencyMs - lastSample);
            // RFC 3550: J_i = J_{i-1} + (|D(i-1, i)| - J_{i-1}) / 16.0
            rfc3550Jitter += (delta - rfc3550Jitter) / 16.0;
        }
        lastSample = latencyMs;

        if (size == capacity) {
            runningSum -= samples[head];
        } else {
            size++;
        }

        samples[head] = latencyMs;
        runningSum += latencyMs;
        head = (head + 1) % capacity;
    }

    public synchronized double getMean() {
        if (size == 0) {
            return 0.0;
        }
        return runningSum / (double) size;
    }

    public synchronized double getMin() {
        if (size == 0) {
            return 0.0;
        }
        double min = Double.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            if (samples[i] < min) {
                min = samples[i];
            }
        }
        return min;
    }

    public synchronized double getMax() {
        if (size == 0) {
            return 0.0;
        }
        double max = 0.0;
        for (int i = 0; i < size; i++) {
            if (samples[i] > max) {
                max = samples[i];
            }
        }
        return max;
    }

    public synchronized double getMedian() {
        if (size == 0) {
            return 0.0;
        }
        double[] sorted = getValidSamples();
        Arrays.sort(sorted);
        if (size % 2 == 1) {
            return sorted[size / 2];
        } else {
            return (sorted[(size / 2) - 1] + sorted[size / 2]) / 2.0;
        }
    }

    /**
     * Extracts quantile for percentile in range [0.0, 1.0].
     */
    public synchronized double getQuantile(double percentile) {
        if (size == 0) {
            return 0.0;
        }
        double clamped = Math.max(0.0, Math.min(1.0, percentile));
        double[] sorted = getValidSamples();
        Arrays.sort(sorted);

        int index = (int) Math.floor(clamped * (size - 1));
        index = Math.max(0, Math.min(size - 1, index));
        return sorted[index];
    }

    public synchronized double getP50() {
        return getQuantile(0.50);
    }

    public synchronized double getP90() {
        return getQuantile(0.90);
    }

    public synchronized double getP95() {
        return getQuantile(0.95);
    }

    public synchronized double getP99() {
        return getQuantile(0.99);
    }

    /**
     * Computes Mean Absolute Consecutive Difference (MACD) across chronological samples.
     */
    public synchronized double getMeanAbsoluteConsecutiveDifference() {
        if (size <= 1) {
            return 0.0;
        }
        double[] chronological = getValidSamples();
        double sumDiff = 0.0;
        for (int i = 0; i < size - 1; i++) {
            sumDiff += Math.abs(chronological[i + 1] - chronological[i]);
        }
        return sumDiff / (double) (size - 1);
    }

    /**
     * Computes population standard deviation of buffered latency samples.
     */
    public synchronized double getStandardDeviation() {
        if (size <= 1) {
            return 0.0;
        }
        double mean = getMean();
        double sumSq = 0.0;
        for (int i = 0; i < size; i++) {
            double diff = samples[i] - mean;
            sumSq += diff * diff;
        }
        return Math.sqrt(sumSq / (double) size);
    }

    /**
     * Computes connection stability index score (0.0 to 100.0%).
     * 100% represents zero latency variance; score decays with coefficient of variation.
     */
    public synchronized double getStabilityIndex() {
        if (size <= 1) {
            return 100.0;
        }
        double mean = getMean();
        if (mean <= 0.0001) {
            return 100.0;
        }
        double sd = getStandardDeviation();
        double cv = sd / mean;
        double score = 100.0 * (1.0 - cv);
        return Math.max(0.0, Math.min(100.0, score));
    }

    public synchronized double getRfc3550Jitter() {
        return rfc3550Jitter;
    }

    public synchronized double[] getValidSamples() {
        double[] valid = new double[size];
        if (size == 0) {
            return valid;
        }
        if (size < capacity) {
            System.arraycopy(samples, 0, valid, 0, size);
        } else {
            int rightLength = capacity - head;
            System.arraycopy(samples, head, valid, 0, rightLength);
            System.arraycopy(samples, 0, valid, rightLength, head);
        }
        return valid;
    }

    public int getCapacity() {
        return capacity;
    }

    public synchronized int getSize() {
        return size;
    }

    public synchronized void reset() {
        Arrays.fill(samples, 0.0);
        head = 0;
        size = 0;
        runningSum = 0.0;
        rfc3550Jitter = 0.0;
        lastSample = -1.0;
    }
}
