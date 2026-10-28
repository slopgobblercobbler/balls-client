package com.ballsclient.core.module.render.hud;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * JVM heap memory telemetry HUD displaying used, allocated, and maximum memory.
 */
public class MemoryHUD extends Module {
    private final BooleanSetting showMax = new BooleanSetting("ShowMax", "Show maximum configured heap memory", true);
    private final ColorSetting warningColor = new ColorSetting("WarningColor", "Color when memory exceeds threshold", 0xFFFF4444);
    private final NumberSetting alertThresholdPercent = new NumberSetting("AlertThreshold", "Memory warning threshold percentage", 85.0, 50.0, 95.0, 5.0);

    private long usedMemoryBytes = 0;
    private long totalMemoryBytes = 0;
    private long maxMemoryBytes = 0;

    public MemoryHUD() {
        super("memory-hud", "Memory HUD", "Real-time JVM heap usage and garbage collection monitor", Category.RENDER);
        addSettings(showMax, warningColor, alertThresholdPercent);
    }

    public void sampleJvmMemory() {
        Runtime runtime = Runtime.getRuntime();
        this.totalMemoryBytes = runtime.totalMemory();
        this.maxMemoryBytes = runtime.maxMemory();
        this.usedMemoryBytes = totalMemoryBytes - runtime.freeMemory();
    }

    public long getUsedMb() {
        return usedMemoryBytes / (1024 * 1024);
    }

    public long getTotalMb() {
        return totalMemoryBytes / (1024 * 1024);
    }

    public long getMaxMb() {
        return maxMemoryBytes / (1024 * 1024);
    }

    public double getUsedPercent() {
        if (maxMemoryBytes <= 0) return 0.0;
        return ((double) usedMemoryBytes / maxMemoryBytes) * 100.0;
    }

    public boolean isExceedingThreshold() {
        return getUsedPercent() >= alertThresholdPercent.getValue();
    }

    public String formatDisplayText() {
        if (showMax.getValue()) {
            return String.format("Mem: %dMB / %dMB (%.0f%%)", getUsedMb(), getMaxMb(), getUsedPercent());
        } else {
            return String.format("Mem: %dMB (%.0f%%)", getUsedMb(), getUsedPercent());
        }
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Render memory HUD
    }
}
