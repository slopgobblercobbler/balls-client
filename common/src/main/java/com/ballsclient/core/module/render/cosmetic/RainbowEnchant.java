package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Cycles enchantment glint texture through animated chroma rainbow colors instead of default purple.
 */
public class RainbowEnchant extends Module {
    private final NumberSetting speed = new NumberSetting("Speed", "Chroma color cycle frequency", 1.0, 0.1, 5.0, 0.1);
    private final NumberSetting brightness = new NumberSetting("Brightness", "Glint texture illumination brightness", 0.8, 0.1, 1.0, 0.05);

    public RainbowEnchant() {
        super("rainbow-enchant", "Rainbow Enchant", "Cycles enchantment glint through animated chroma rainbow colors", Category.RENDER);
        addSettings(speed, brightness);
    }

    public int getChromaGlintColor(long timeMillis) {
        float hue = (float) (((timeMillis * speed.getValue() * 0.001)) % 1.0);
        return ColorSetting.hsbToRgb(hue, 0.8f, brightness.getFloatValue());
    }

    public double getSpeed() { return speed.getValue(); }
    public double getBrightness() { return brightness.getValue(); }
}
