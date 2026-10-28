package com.ballsclient.core.module.player.utility;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * AirBubblePreserver — Submerged oxygen supply monitor alerting before drowning damage begins.
 */
public class AirBubblePreserver extends Module {
    private final NumberSetting emergencyAirThreshold = new NumberSetting("EmergencyAirThreshold", "Remaining air ticks to trigger emergency warning", 60.0, 20.0, 150.0, 5.0);
    private final BooleanSetting autoSurface = new BooleanSetting("AutoSurface", "Apply upward water propulsion to breach surface", false);
    private final BooleanSetting alertLowAir = new BooleanSetting("AlertLowAir", "Audible oxygen alert when under 3 bubbles", true);

    public AirBubblePreserver() {
        super("air-bubble-preserver", "Air Bubble Preserver", "Submerged oxygen supply monitor alerting before drowning damage begins", Category.PLAYER);
        addSettings(emergencyAirThreshold, autoSurface, alertLowAir);
    }

    public NumberSetting getEmergencyAirThreshold() {
        return emergencyAirThreshold;
    }

    public BooleanSetting getAutoSurface() {
        return autoSurface;
    }

    public BooleanSetting getAlertLowAir() {
        return alertLowAir;
    }

    public static boolean isDrowningRisk(int currentAirTicks, double threshold) {
        return currentAirTicks <= threshold;
    }
}
