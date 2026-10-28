package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Gamma override and lightmap texture brightness amplifier ensuring 100% visibility in caves.
 */
public class Fullbright extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", "Fullbright technique", "GAMMA", "GAMMA", "LIGHTMAP");
    private final NumberSetting brightness = new NumberSetting("Brightness", "Illumination multiplier level", 10.0, 1.0, 15.0, 0.5);

    public Fullbright() {
        super("fullbright", "Fullbright", "Overrides world luminance and lightmaps for maximum visibility in dark caves", Category.RENDER);
        addSettings(mode, brightness);
    }

    public String getMode() { return mode.getValue(); }
    public double getBrightness() { return brightness.getValue(); }
}
