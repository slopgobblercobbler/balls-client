package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Accumulates previous frames using GL blend states for cinematic motion blur.
 */
public class MotionBlur extends Module {
    private final NumberSetting blurAmount = new NumberSetting("BlurAmount", "Frame accumulation persistence (0.0 to 0.95)", 0.5, 0.0, 0.95, 0.05);
    private final NumberSetting sampleCount = new NumberSetting("Samples", "Accumulation buffer sample count", 6.0, 2.0, 16.0, 1.0);

    public MotionBlur() {
        super("motion-blur", "Motion Blur", "Cinematic frame accumulation motion blur shader", Category.RENDER);
        addSettings(blurAmount, sampleCount);
    }

    public double getBlurAmount() { return blurAmount.getValue(); }
    public int getSampleCount() { return sampleCount.getIntValue(); }
}
