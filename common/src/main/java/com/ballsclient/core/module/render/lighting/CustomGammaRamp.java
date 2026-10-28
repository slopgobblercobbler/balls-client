package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Programmatic sRGB tone-curve modifier manipulating gamma exponent and black floor.
 */
public class CustomGammaRamp extends Module {
    private final NumberSetting gammaExponent = new NumberSetting("GammaExponent", "Tone-curve power exponent", 1.0, 0.1, 5.0, 0.1);
    private final NumberSetting blackPoint = new NumberSetting("BlackPoint", "Minimum luminance black level", 0.0, 0.0, 0.5, 0.02);

    public CustomGammaRamp() {
        super("custom-gamma-ramp", "Custom Gamma Ramp", "Manipulates programmatic display gamma exponent and black floor", Category.RENDER);
        addSettings(gammaExponent, blackPoint);
    }

    public float applyRamp(float inputLuminance) {
        float bp = blackPoint.getFloatValue();
        float clamped = Math.max(0.0f, Math.min(1.0f, inputLuminance));
        float adjusted = bp + (1.0f - bp) * clamped;
        return (float) Math.pow(adjusted, 1.0 / gammaExponent.getValue());
    }

    public double getGammaExponent() { return gammaExponent.getValue(); }
    public double getBlackPoint() { return blackPoint.getValue(); }
}
