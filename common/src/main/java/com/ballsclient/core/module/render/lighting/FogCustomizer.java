package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Modifies world fog start distance, end distance, density, and color palette.
 */
public class FogCustomizer extends Module {
    private final NumberSetting fogStart = new NumberSetting("FogStart", "Distance in blocks where fog begins", 10.0, 0.0, 100.0, 5.0);
    private final NumberSetting fogEnd = new NumberSetting("FogEnd", "Distance in blocks where fog reaches 100% density", 120.0, 10.0, 500.0, 10.0);
    private final ColorSetting fogColor = new ColorSetting("FogColor", "Fog color tint", 0xFFCCCCCC);
    private final BooleanSetting disableFog = new BooleanSetting("DisableFog", "Completely eliminate horizon fog for infinite visibility", false);

    public FogCustomizer() {
        super("fog-customizer", "Fog Customizer", "Modifies world fog start/end distances, density, and color", Category.RENDER);
        addSettings(fogStart, fogEnd, fogColor, disableFog);
    }

    public double getFogStart() { return fogStart.getValue(); }
    public double getFogEnd() { return fogEnd.getValue(); }
    public int getFogColor() { return fogColor.getValue(); }
    public boolean isDisableFog() { return disableFog.getValue(); }
}
