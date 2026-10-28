package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Smoothly interpolates other players' head pitch and yaw angles to eliminate packet snapping.
 */
public class HeadRotator extends Module {
    private final NumberSetting smoothingFactor = new NumberSetting("Smoothing", "Interpolation responsiveness (0.05 to 0.95)", 0.3, 0.05, 0.95, 0.05);

    public HeadRotator() {
        super("head-rotator", "Head Rotator", "Interpolates entity head rotations smoothly to eliminate network jitter", Category.RENDER);
        addSettings(smoothingFactor);
    }

    public float interpolateHeadYaw(float currentYaw, float targetYaw) {
        float diff = (targetYaw - currentYaw + 180 + 360) % 360 - 180;
        return currentYaw + diff * smoothingFactor.getFloatValue();
    }

    public double getSmoothingFactor() { return smoothingFactor.getValue(); }
}
