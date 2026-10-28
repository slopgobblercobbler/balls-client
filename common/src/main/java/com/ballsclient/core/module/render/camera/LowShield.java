package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Offhand shield lowered even further with transparency when blocking attacks.
 */
public class LowShield extends Module {
    private final NumberSetting transparency = new NumberSetting("Transparency", "Shield transparency while blocking", 0.6, 0.1, 1.0, 0.05);
    private final NumberSetting yOffset = new NumberSetting("YOffset", "Vertical lowering distance", -0.5, -1.0, 0.0, 0.05);

    public LowShield() {
        super("low-shield", "Low Shield", "Lowers shield even further and applies transparency when raised", Category.RENDER);
        addSettings(transparency, yOffset);
    }

    public double getTransparency() { return transparency.getValue(); }
    public double getYOffset() { return yOffset.getValue(); }
}
