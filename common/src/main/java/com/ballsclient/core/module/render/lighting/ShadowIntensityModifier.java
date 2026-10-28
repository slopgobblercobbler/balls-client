package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Modifies global shadow darkness multiplier across terrain, entities, and models.
 */
public class ShadowIntensityModifier extends Module {
    private final NumberSetting darknessFactor = new NumberSetting("Darkness", "Shadow darkness multiplier", 1.0, 0.0, 2.0, 0.1);

    public ShadowIntensityModifier() {
        super("shadow-intensity-modifier", "Shadow Intensity Modifier", "Controls global darkness depth of terrain and entity shadows", Category.RENDER);
        addSettings(darknessFactor);
    }

    public double getDarknessFactor() { return darknessFactor.getValue(); }
}
