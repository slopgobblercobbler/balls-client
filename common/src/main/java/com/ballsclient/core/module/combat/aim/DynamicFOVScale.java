package com.ballsclient.core.module.combat.aim;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Dynamic FOV Scale — Modulates field of view dynamically based on weapon draw charge and target proximity for enhanced situational awareness
 */
public class DynamicFOVScale extends Module {
    private final NumberSetting baseFov = new NumberSetting("BaseFov", "Baseline resting FOV", 90.0, 60.0, 110.0, 1.0);
    private final NumberSetting zoomMultiplier = new NumberSetting("ZoomMultiplier", "Zoom scaling multiplier when drawing bow", 0.85, 0.5, 1.5, 0.05);
    private final BooleanSetting scaleWithBow = new BooleanSetting("ScaleWithBow", "Zoom in when drawing bow", true);

    public DynamicFOVScale() {
        super("dynamic-fov-scale", "Dynamic FOV Scale", "Modulates field of view dynamically based on weapon draw charge and target proximity for enhanced situational awareness", Category.COMBAT);
        addSettings(baseFov, zoomMultiplier, scaleWithBow);
    }

    public double getBaseFov() {
        return baseFov.getValue();
    }

    public double getZoomMultiplier() {
        return zoomMultiplier.getValue();
    }

    public boolean isScaleWithBow() {
        return scaleWithBow.getValue();
    }

    public double calculateFov(double baseFov, double bowCharge, double targetDistance, boolean bowActive) {
        double scale = 1.0;
        if (bowActive) scale -= 0.15 * Math.min(1.0, Math.max(0.0, bowCharge));
        if (targetDistance < 4.0 && targetDistance > 0.0) scale += 0.05 * (1.0 - targetDistance / 4.0);
        return baseFov * scale;
    }
}
