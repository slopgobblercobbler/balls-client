package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * Custom hand and weapon shaders (Galaxy texture, polygonal wireframe, or translucent ghost).
 */
public class HandChams extends Module {
    private final ModeSetting chamsType = new ModeSetting("Type", "Hand chams visual style", "GALAXY", "GALAXY", "WIREFRAME", "TRANSLUCENT");
    private final ColorSetting handColor = new ColorSetting("HandColor", "Base chams color tint", 0x995500AA);

    public HandChams() {
        super("hand-chams", "Hand Chams", "Applies galaxy, wireframe, or translucent materials to player hands and items", Category.RENDER);
        addSettings(chamsType, handColor);
    }

    public String getChamsType() { return chamsType.getValue(); }
    public int getHandColor() { return handColor.getValue(); }
}
