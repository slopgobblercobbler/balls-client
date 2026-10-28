package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Transforms opaque orange lava fog into transparent warm amber for navigating Nether lava seas.
 */
public class LavaVision extends Module {
    private final NumberSetting lavaTransparency = new NumberSetting("Transparency", "Clear vision transparency factor in lava", 0.6, 0.1, 1.0, 0.05);
    private final ColorSetting amberTint = new ColorSetting("AmberTint", "Sub-surface amber illumination color", 0xFFFF8800);

    public LavaVision() {
        super("lava-vision", "Lava Vision", "Clears opaque lava fog into translucent amber for navigating lava oceans", Category.RENDER);
        addSettings(lavaTransparency, amberTint);
    }

    public double getLavaTransparency() { return lavaTransparency.getValue(); }
    public int getAmberTint() { return amberTint.getValue(); }
}
