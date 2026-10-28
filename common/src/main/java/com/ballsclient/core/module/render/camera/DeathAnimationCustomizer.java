package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;

/**
 * Customizes entity death animations (smooth fade-out, instant vaporization, or shattered crumble).
 */
public class DeathAnimationCustomizer extends Module {
    private final ModeSetting style = new ModeSetting("Style", "Death animation style", "FADE", "FADE", "VAPORIZE", "SHATTER");

    public DeathAnimationCustomizer() {
        super("death-animation-customizer", "Death Animation Customizer", "Custom visual demise animations for mobs and players", Category.RENDER);
        addSettings(style);
    }

    public String getStyle() { return style.getValue(); }
}
