package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * High-dynamic range contrast and saturation curve boost for vivid low-light rendering.
 */
public class NightVisionEnhancer extends Module {
    private final NumberSetting contrastBoost = new NumberSetting("ContrastBoost", "Contrast curve steepness multiplier", 1.2, 1.0, 2.5, 0.1);
    private final NumberSetting saturation = new NumberSetting("Saturation", "Color saturation enhancement", 1.1, 1.0, 2.0, 0.05);

    public NightVisionEnhancer() {
        super("night-vision-enhancer", "Night Vision Enhancer", "HDR contrast and saturation boosting for vivid night vision", Category.RENDER);
        addSettings(contrastBoost, saturation);
    }

    public double getContrastBoost() { return contrastBoost.getValue(); }
    public double getSaturation() { return saturation.getValue(); }
}
