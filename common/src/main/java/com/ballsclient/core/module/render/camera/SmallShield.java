package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Lowers and scales down offhand shields so they do not obstruct half the first-person screen.
 */
public class SmallShield extends Module {
    private final NumberSetting yOffset = new NumberSetting("YOffset", "Vertical lowering offset", -0.3, -1.0, 0.0, 0.05);
    private final NumberSetting scale = new NumberSetting("Scale", "Shield model scale multiplier", 0.7, 0.3, 1.0, 0.05);

    public SmallShield() {
        super("small-shield", "Small Shield", "Lowers offhand shield model to clear first-person vision", Category.RENDER);
        addSettings(yOffset, scale);
    }

    public double getYOffset() { return yOffset.getValue(); }
    public double getScale() { return scale.getValue(); }
}
