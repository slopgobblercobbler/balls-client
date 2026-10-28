package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Custom solar disk scaling factor for altering the apparent size of the sun.
 */
public class SunSizeModifier extends Module {
    private final NumberSetting sizeMultiplier = new NumberSetting("Multiplier", "Solar disk diameter multiplier", 2.0, 0.2, 10.0, 0.2);

    public SunSizeModifier() {
        super("sun-size-modifier", "Sun Size Modifier", "Customizable size multiplier for the celestial sun disk", Category.RENDER);
        addSettings(sizeMultiplier);
    }

    public double getSizeMultiplier() { return sizeMultiplier.getValue(); }
}
