package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Smooth cinematic zoom with mouse wheel dynamic FOV scaling and mouse sensitivity dampening.
 */
public class SmoothZoom extends Module {
    private final NumberSetting zoomFactor = new NumberSetting("ZoomFactor", "Magnification level", 4.0, 1.0, 10.0, 0.5);
    private final NumberSetting smoothSpeed = new NumberSetting("SmoothSpeed", "Easing transition speed", 8.0, 1.0, 20.0, 1.0);
    private final BooleanSetting mouseSensitivityCompensation = new BooleanSetting("MouseDampening", "Reduce mouse sensitivity proportionally to zoom", true);

    private double currentZoomProgress = 0.0;

    public SmoothZoom() {
        super("smooth-zoom", "Smooth Zoom", "Cinematic FOV zoom with smooth easing transitions", Category.RENDER);
        addSettings(zoomFactor, smoothSpeed, mouseSensitivityCompensation);
    }

    public void updateZoomProgress(float deltaSeconds) {
        double target = isEnabled() ? 1.0 : 0.0;
        double diff = target - currentZoomProgress;
        currentZoomProgress += diff * Math.min(1.0, deltaSeconds * smoothSpeed.getValue());
    }

    public double getEffectiveFovMultiplier(double baseFov) {
        if (currentZoomProgress <= 0.001) return 1.0;
        double targetFov = baseFov / zoomFactor.getValue();
        double interpolatedFov = baseFov - (currentZoomProgress * (baseFov - targetFov));
        return interpolatedFov / baseFov;
    }

    public double getZoomFactor() { return zoomFactor.getValue(); }
    public double getCurrentZoomProgress() { return currentZoomProgress; }
    public boolean isMouseSensitivityCompensation() { return mouseSensitivityCompensation.getValue(); }
}
