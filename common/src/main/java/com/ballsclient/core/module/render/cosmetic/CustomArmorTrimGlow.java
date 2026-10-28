package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Neon pulsating luminescence for diamond and netherite armor trims in low-light environments.
 */
public class CustomArmorTrimGlow extends Module {
    private final ColorSetting neonColor = new ColorSetting("NeonColor", "Emissive trim glow color", 0xFF00FFFF);
    private final NumberSetting pulseRate = new NumberSetting("PulseRate", "Trim glow pulse frequency", 1.0, 0.1, 4.0, 0.1);

    public CustomArmorTrimGlow() {
        super("custom-armor-trim-glow", "Custom Armor Trim Glow", "Luminescent pulsating glow on diamond and netherite armor trims", Category.RENDER);
        addSettings(neonColor, pulseRate);
    }

    public int getPulsedGlowColor(long timeMillis) {
        double sin = Math.sin(timeMillis * 0.003 * pulseRate.getValue());
        float factor = (float) (0.6 + 0.4 * sin);
        int base = neonColor.getValue();
        int a = (int) (neonColor.getAlpha() * factor);
        return (a << 24) | (base & 0x00FFFFFF);
    }

    public int getNeonColor() { return neonColor.getValue(); }
}
