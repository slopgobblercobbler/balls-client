package com.ballsclient.core.module.render.hud;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Real-time framerate and 1% low frame time performance HUD.
 */
public class FPSHUD extends Module {
    private final BooleanSetting showOnePercentLow = new BooleanSetting("Show1%Low", "Calculate and display 1% low FPS", true);
    private final BooleanSetting showGraph = new BooleanSetting("ShowGraph", "Display rolling frametime sparkline mini-graph", false);
    private final ColorSetting textColor = new ColorSetting("TextColor", "Text readout color", 0xFFFFFFFF);

    private final ArrayDeque<Long> frameTimesNanos = new ArrayDeque<>();
    private static final int MAX_SAMPLES = 100;

    public FPSHUD() {
        super("fps-hud", "FPS HUD", "Displays rolling average FPS and 1% low frametimes", Category.RENDER);
        addSettings(showOnePercentLow, showGraph, textColor);
    }

    public synchronized void recordFrame(long frameTimeNanos) {
        if (frameTimeNanos <= 0) return;
        frameTimesNanos.addLast(frameTimeNanos);
        while (frameTimesNanos.size() > MAX_SAMPLES) {
            frameTimesNanos.pollFirst();
        }
    }

    public synchronized double getAverageFps() {
        if (frameTimesNanos.isEmpty()) return 0.0;
        long totalNanos = 0;
        for (long nanos : frameTimesNanos) {
            totalNanos += nanos;
        }
        double avgNanos = (double) totalNanos / frameTimesNanos.size();
        return 1_000_000_000.0 / avgNanos;
    }

    public synchronized double getOnePercentLowFps() {
        if (frameTimesNanos.size() < 10) return getAverageFps();
        List<Long> sorted = new ArrayList<>(frameTimesNanos);
        Collections.sort(sorted);
        int lowIndex = Math.max(0, (int) Math.ceil(sorted.size() * 0.99) - 1);
        long p99Nanos = sorted.get(lowIndex);
        return 1_000_000_000.0 / p99Nanos;
    }

    public String formatDisplayText() {
        if (showOnePercentLow.getValue()) {
            return String.format("FPS: %.0f (1%% Low: %.0f)", getAverageFps(), getOnePercentLowFps());
        } else {
            return String.format("FPS: %.0f", getAverageFps());
        }
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Render HUD text
    }
}
