package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Custom sizing, textures, and lunar phase controller for celestial bodies.
 */
public class CustomSunMoon extends Module {
    private final NumberSetting sunScale = new NumberSetting("SunScale", "Sun disk scale factor", 1.5, 0.2, 5.0, 0.1);
    private final NumberSetting moonScale = new NumberSetting("MoonScale", "Moon disk scale factor", 1.5, 0.2, 5.0, 0.1);
    private final ModeSetting moonPhase = new ModeSetting("MoonPhase", "Forced lunar phase", "FULL", "FULL", "CRESCENT", "NEW", "BLOOD");

    public CustomSunMoon() {
        super("custom-sun-moon", "Custom Sun & Moon", "Configurable scale and appearance for sun, moon, and lunar phases", Category.RENDER);
        addSettings(sunScale, moonScale, moonPhase);
    }

    public double getSunScale() { return sunScale.getValue(); }
    public double getMoonScale() { return moonScale.getValue(); }
    public String getMoonPhase() { return moonPhase.getValue(); }
}
