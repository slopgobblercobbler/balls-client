package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;

/**
 * Dimension-specific sky and horizon color grading for Overworld, Nether, and End.
 */
public class DimensionAtmosphere extends Module {
    private final ColorSetting overworldColor = new ColorSetting("Overworld", "Overworld horizon haze color", 0xFF88CCFF);
    private final ColorSetting netherColor = new ColorSetting("Nether", "Nether crimson volcanic fog color", 0xFF550000);
    private final ColorSetting endColor = new ColorSetting("End", "End void dark matter glow color", 0xFF220033);

    public DimensionAtmosphere() {
        super("dimension-atmosphere", "Dimension Atmosphere", "Custom sky and fog color grading tailored per dimension", Category.RENDER);
        addSettings(overworldColor, netherColor, endColor);
    }

    public int getOverworldColor() { return overworldColor.getValue(); }
    public int getNetherColor() { return netherColor.getValue(); }
    public int getEndColor() { return endColor.getValue(); }
}
