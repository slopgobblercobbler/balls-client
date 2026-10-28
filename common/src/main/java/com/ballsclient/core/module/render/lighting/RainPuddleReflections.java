package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Simulates dynamic reflective water puddle shaders and ripple waves on flat surfaces during storms.
 */
public class RainPuddleReflections extends Module {
    private final NumberSetting reflectivity = new NumberSetting("Reflectivity", "Puddle surface gloss and mirror reflectivity", 0.6, 0.1, 1.0, 0.05);
    private final NumberSetting rippleSpeed = new NumberSetting("RippleSpeed", "Raindrop disturbance wave animation rate", 1.0, 0.5, 3.0, 0.25);

    public RainPuddleReflections() {
        super("rain-puddle-reflections", "Rain Puddle Reflections", "Simulates dynamic mirror puddles and ripple effects during rain", Category.RENDER);
        addSettings(reflectivity, rippleSpeed);
    }

    public double getReflectivity() { return reflectivity.getValue(); }
    public double getRippleSpeed() { return rippleSpeed.getValue(); }
}
