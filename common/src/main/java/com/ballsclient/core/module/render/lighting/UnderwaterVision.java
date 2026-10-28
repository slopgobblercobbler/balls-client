package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Clears dense underwater murky blue fog for crystal-clear sub-aquatic exploration.
 */
public class UnderwaterVision extends Module {
    private final BooleanSetting clearWater = new BooleanSetting("ClearWater", "Completely remove murky water fog", true);
    private final NumberSetting waterAlpha = new NumberSetting("WaterAlpha", "Water surface translucency alpha", 0.1, 0.0, 1.0, 0.05);

    public UnderwaterVision() {
        super("underwater-vision", "Underwater Vision", "Clears underwater murkiness for crystal clear ocean vision", Category.RENDER);
        addSettings(clearWater, waterAlpha);
    }

    public boolean isClearWater() { return clearWater.getValue(); }
    public double getWaterAlpha() { return waterAlpha.getValue(); }
}
