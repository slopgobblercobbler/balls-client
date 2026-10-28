package com.ballsclient.core.module.movement.locomotion;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Dynamic porpoising velocity bursts when swimming in water.
 */
public class Dolphin extends Module {
    private final NumberSetting burstSpeed = new NumberSetting("BurstSpeed", "Peak porpoising upward velocity impulse", 0.45, 0.1, 1.5, 0.05);
    private final NumberSetting pitchFactor = new NumberSetting("PitchFactor", "Camera pitch influence on swim trajectory", 1.2, 0.5, 2.5, 0.1);
    private final BooleanSetting autoSurface = new BooleanSetting("AutoSurface", "Direct trajectory towards water surface", true);

    public Dolphin() {
        super("dolphin", "Dolphin", "Dynamic porpoising velocity bursts when swimming in water", Category.MOVEMENT);
        addSettings(burstSpeed, pitchFactor, autoSurface);
    }

    public double getBurstSpeed() { return burstSpeed.getValue(); }
    public double getPitchFactor() { return pitchFactor.getValue(); }
    public boolean isAutoSurface() { return autoSurface.getValue(); }

    public double calculatePorpoiseVelocity(long ticks, boolean inWater) {
        if (!isEnabled() || !inWater) return 0.0;
        return Math.sin(ticks * 0.2) * getBurstSpeed();
    }
}
