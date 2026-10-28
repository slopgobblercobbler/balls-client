package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Lowers empty fist and bare hand rendering off the bottom edge of the screen.
 */
public class MinimalHand extends Module {
    private final NumberSetting handOffset = new NumberSetting("HandOffset", "Downward lowering offset for empty hand", 0.4, 0.0, 1.0, 0.05);

    public MinimalHand() {
        super("minimal-hand", "Minimal Hand", "Lowers empty hand view model out of the center screen", Category.RENDER);
        addSettings(handOffset);
    }

    public double getHandOffset() { return handOffset.getValue(); }
}
