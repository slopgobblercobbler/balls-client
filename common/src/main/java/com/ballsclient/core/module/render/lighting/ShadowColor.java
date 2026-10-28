package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Customizes entity and block drop shadow tinting and opacity.
 */
public class ShadowColor extends Module {
    private final ColorSetting shadowColor = new ColorSetting("ShadowColor", "Shadow color tint", 0x80332255);
    private final NumberSetting shadowAlpha = new NumberSetting("ShadowAlpha", "Drop shadow opacity factor", 0.5, 0.0, 1.0, 0.05);

    public ShadowColor() {
        super("shadow-color", "Shadow Color", "Customizes drop shadow color and transparency", Category.RENDER);
        addSettings(shadowColor, shadowAlpha);
    }

    public int getShadowColor() { return shadowColor.getValue(); }
    public double getShadowAlpha() { return shadowAlpha.getValue(); }
}
