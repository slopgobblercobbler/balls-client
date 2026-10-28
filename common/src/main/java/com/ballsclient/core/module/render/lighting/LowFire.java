package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Lowers on-screen burning fire overlay height so camera vision remains unobstructed during combat.
 */
public class LowFire extends Module {
    private final NumberSetting fireHeight = new NumberSetting("FireHeight", "Vertical height multiplier of fire texture", 0.3, 0.0, 1.0, 0.05);
    private final NumberSetting fireAlpha = new NumberSetting("FireAlpha", "Opacity of flame particles", 0.7, 0.1, 1.0, 0.05);

    public LowFire() {
        super("low-fire", "Low Fire", "Lowers first-person on-fire texture height to prevent vision obstruction", Category.RENDER);
        addSettings(fireHeight, fireAlpha);
    }

    public double getFireHeight() { return fireHeight.getValue(); }
    public double getFireAlpha() { return fireAlpha.getValue(); }
}
