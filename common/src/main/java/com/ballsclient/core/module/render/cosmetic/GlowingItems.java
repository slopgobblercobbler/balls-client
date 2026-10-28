package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;

/**
 * Renders a luminous glowing aura shader around high-value dropped items (Diamonds, Netherite).
 */
public class GlowingItems extends Module {
    private final BooleanSetting valuableOnly = new BooleanSetting("ValuableOnly", "Only apply glow aura to high-tier minerals and equipment", true);
    private final ColorSetting glowColor = new ColorSetting("GlowColor", "Item luminous outline color", 0xFF00FFFF);

    public GlowingItems() {
        super("glowing-items", "Glowing Items", "Pulsating luminous glow outlines around valuable ground items", Category.RENDER);
        addSettings(valuableOnly, glowColor);
    }

    public boolean isValuableOnly() { return valuableOnly.getValue(); }
    public int getGlowColor() { return glowColor.getValue(); }
}
