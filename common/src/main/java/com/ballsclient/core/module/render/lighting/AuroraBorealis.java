package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Procedurally animated northern lights ribbons dancing across night skies in snowy biomes.
 */
public class AuroraBorealis extends Module {
    private final NumberSetting waveSpeed = new NumberSetting("WaveSpeed", "Ribbon animation wave speed", 1.0, 0.1, 4.0, 0.1);
    private final ColorSetting ribbonColor = new ColorSetting("RibbonColor", "Aurora emerald wave color", 0xFF00FF88);
    private final NumberSetting ribbonHeight = new NumberSetting("RibbonHeight", "Vertical altitude of sky curtains", 40.0, 10.0, 100.0, 5.0);

    public AuroraBorealis() {
        super("aurora-borealis", "Aurora Borealis", "Procedural animated northern lights curtains dancing across night skies", Category.RENDER);
        addSettings(waveSpeed, ribbonColor, ribbonHeight);
    }

    public double getWaveSpeed() { return waveSpeed.getValue(); }
    public int getRibbonColor() { return ribbonColor.getValue(); }
    public double getRibbonHeight() { return ribbonHeight.getValue(); }
}
