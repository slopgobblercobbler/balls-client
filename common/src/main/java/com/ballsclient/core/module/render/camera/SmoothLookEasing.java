package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Smooth exponential easing filter for mouse look deltas, eliminating jitter.
 */
public class SmoothLookEasing extends Module {
    private final NumberSetting smoothFactor = new NumberSetting("SmoothFactor", "Mouse look interpolation factor", 0.5, 0.1, 1.0, 0.05);

    public SmoothLookEasing() {
        super("smooth-look-easing", "Smooth Look Easing", "Interpolates raw mouse delta values for cinematic smoothness", Category.RENDER);
        addSettings(smoothFactor);
    }

    public double getSmoothFactor() { return smoothFactor.getValue(); }
}
