package com.ballsclient.core.module.render.hud;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * Real-time locomotion speed telemetry monitor with peak speed recall.
 */
public class SpeedometerHUD extends Module {
    private final ModeSetting units = new ModeSetting("Units", "Speed measurement units", "BPS", "BPS", "KMH");
    private final BooleanSetting showPeak = new BooleanSetting("ShowPeak", "Record and display session peak speed", true);
    private final ColorSetting textColor = new ColorSetting("TextColor", "Speed readout color", 0xFFFFFFFF);

    private double currentSpeedBps = 0.0;
    private double peakSpeedBps = 0.0;
    private double prevX = 0, prevY = 0, prevZ = 0;
    private boolean hasPrevPos = false;

    public SpeedometerHUD() {
        super("speedometer-hud", "Speedometer HUD", "Measures real-time player locomotion velocity", Category.RENDER);
        addSettings(units, showPeak, textColor);
    }

    public void updatePosition(double x, double y, double z, double tickDeltaSeconds) {
        if (!hasPrevPos) {
            prevX = x;
            prevY = y;
            prevZ = z;
            hasPrevPos = true;
            return;
        }
        double dx = x - prevX;
        double dy = y - prevY;
        double dz = z - prevZ;
        prevX = x;
        prevY = y;
        prevZ = z;

        if (tickDeltaSeconds > 0) {
            double dist = Math.sqrt(dx * dx + dz * dz);
            currentSpeedBps = dist / tickDeltaSeconds;
            if (currentSpeedBps > peakSpeedBps) {
                peakSpeedBps = currentSpeedBps;
            }
        }
    }

    public double getCurrentSpeed() {
        if (units.is("KMH")) {
            return currentSpeedBps * 3.6;
        }
        return currentSpeedBps;
    }

    public double getPeakSpeed() {
        if (units.is("KMH")) {
            return peakSpeedBps * 3.6;
        }
        return peakSpeedBps;
    }

    public String formatDisplayText() {
        String unitStr = units.is("KMH") ? "km/h" : "m/s";
        if (showPeak.getValue()) {
            return String.format("Speed: %.2f %s (Peak: %.2f)", getCurrentSpeed(), unitStr, getPeakSpeed());
        }
        return String.format("Speed: %.2f %s", getCurrentSpeed(), unitStr);
    }

    public void resetPeak() {
        peakSpeedBps = 0.0;
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Render speedometer text
    }
}
