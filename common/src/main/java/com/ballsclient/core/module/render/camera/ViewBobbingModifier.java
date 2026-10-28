package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Adjusts camera and hand walking bobbing amplitude scale and oscillation speed.
 */
public class ViewBobbingModifier extends Module {
    private final NumberSetting bobbingScale = new NumberSetting("Scale", "Bobbing oscillation amplitude", 1.0, 0.0, 3.0, 0.1);
    private final NumberSetting bobbingSpeed = new NumberSetting("Speed", "Bobbing step frequency", 1.0, 0.1, 3.0, 0.1);

    public ViewBobbingModifier() {
        super("view-bobbing-modifier", "View Bobbing Modifier", "Fine-tunes first-person walking camera and hand bobbing motion", Category.RENDER);
        addSettings(bobbingScale, bobbingSpeed);
    }

    public double getBobbingScale() { return bobbingScale.getValue(); }
    public double getBobbingSpeed() { return bobbingSpeed.getValue(); }
}
