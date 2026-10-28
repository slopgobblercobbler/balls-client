package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;

/**
 * Gives own client player model a luminescent celestial outline glow.
 */
public class SelfGlowCosmetic extends Module {
    private final ColorSetting glowColor = new ColorSetting("GlowColor", "Outline luminescence tint", 0xFFFF00FF);
    private final BooleanSetting pulse = new BooleanSetting("Pulse", "Oscillate outline luminance periodically", true);

    public SelfGlowCosmetic() {
        super("self-glow-cosmetic", "Self Glow Cosmetic", "Luminescent celestial outline glow on own player model", Category.RENDER);
        addSettings(glowColor, pulse);
    }

    public int getGlowColor() { return glowColor.getValue(); }
    public boolean isPulse() { return pulse.getValue(); }
}
