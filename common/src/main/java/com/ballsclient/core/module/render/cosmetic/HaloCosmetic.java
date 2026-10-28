package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Luminous floating angelic halo hovering and bobbing above player head.
 */
public class HaloCosmetic extends Module {
    private final ColorSetting haloColor = new ColorSetting("HaloColor", "Halo ring emissive tint", 0xFFFFD700);
    private final NumberSetting bobSpeed = new NumberSetting("BobSpeed", "Vertical bobbing oscillation frequency", 1.0, 0.1, 3.0, 0.1);
    private final NumberSetting radius = new NumberSetting("Radius", "Ring radius in blocks", 0.4, 0.2, 1.0, 0.05);

    public HaloCosmetic() {
        super("halo-cosmetic", "Halo Cosmetic", "Golden/neon halo floating and bobbing above player head", Category.RENDER);
        addSettings(haloColor, bobSpeed, radius);
    }

    public float getBobOffset(long timeMillis) {
        return (float) (Math.sin(timeMillis * 0.003 * bobSpeed.getValue()) * 0.08);
    }

    public int getHaloColor() { return haloColor.getValue(); }
    public double getRadius() { return radius.getValue(); }
}
