package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;

/**
 * Translucent custom crystal shading and wireframe lattice overlay on End Crystals.
 */
public class CrystalChams extends Module {
    private final ColorSetting crystalColor = new ColorSetting("CrystalColor", "Crystal body translucency tint", 0x99FF00FF);
    private final BooleanSetting wireframe = new BooleanSetting("Wireframe", "Render geometric wireframe cage", true);

    public CrystalChams() {
        super("crystal-chams", "Crystal Chams", "Translucent crystal body and wireframe cage overlays for End Crystals", Category.RENDER);
        addSettings(crystalColor, wireframe);
    }

    public int getCrystalColor() { return crystalColor.getValue(); }
    public boolean isWireframe() { return wireframe.getValue(); }
}
