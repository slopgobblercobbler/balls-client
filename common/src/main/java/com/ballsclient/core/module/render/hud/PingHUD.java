package com.ballsclient.core.module.render.hud;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Real-time network latency monitor with jitter and spike alerts.
 */
public class PingHUD extends Module {
    private final NumberSetting spikeThresholdMs = new NumberSetting("SpikeThresholdMs", "Latency spike alert threshold in ms", 150.0, 50.0, 500.0, 10.0);
    private final BooleanSetting coloredByLatency = new BooleanSetting("ColoredByLatency", "Dynamically color ping by latency quality", true);
    private final ColorSetting textColor = new ColorSetting("TextColor", "Base text color", 0xFFFFFFFF);

    private int currentPing = 0;
    private int averagePing = 0;

    public PingHUD() {
        super("ping-hud", "Ping HUD", "Displays real-time round-trip latency to the multiplayer server", Category.RENDER);
        addSettings(spikeThresholdMs, coloredByLatency, textColor);
    }

    public void updatePing(int pingMs) {
        this.currentPing = Math.max(0, pingMs);
        if (this.averagePing == 0) {
            this.averagePing = this.currentPing;
        } else {
            this.averagePing = (int) Math.round((0.1 * this.currentPing) + (0.9 * this.averagePing));
        }
    }

    public boolean isSpiking() {
        return currentPing >= spikeThresholdMs.getValue();
    }

    public int getCurrentPing() { return currentPing; }
    public int getAveragePing() { return averagePing; }

    public int getPingColor() {
        if (!coloredByLatency.getValue()) return textColor.getValue();
        if (currentPing < 50) return 0xFF00FF00;      // Green
        if (currentPing < 120) return 0xFFFFFF00;     // Yellow
        if (currentPing < 200) return 0xFFFFAA00;     // Orange
        return 0xFFFF2222;                           // Red
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Render ping readout
    }
}
