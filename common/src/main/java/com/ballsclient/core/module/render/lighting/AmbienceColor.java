package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Global ambient world light colorization (Vaporwave purple, Cyberpunk neon, Blood Moon).
 */
public class AmbienceColor extends Module {
    private final ColorSetting ambientColor = new ColorSetting("AmbientColor", "Global world lighting tint", 0xFFFFAA33);
    private final NumberSetting intensity = new NumberSetting("Intensity", "Colorization blend strength", 0.4, 0.0, 1.0, 0.05);

    public AmbienceColor() {
        super("ambience-color", "Ambience Color", "Global ambient light tinting across all blocks and entities", Category.RENDER);
        addSettings(ambientColor, intensity);
    }

    public int getAmbientColor() { return ambientColor.getValue(); }
    public double getIntensity() { return intensity.getValue(); }
}
