package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Animated 3D cosmetic dragon, angel, or fairy wings attached to player back.
 */
public class WingsCosmetic extends Module {
    private final ModeSetting wingType = new ModeSetting("Type", "Wing aesthetic model", "DRAGON", "DRAGON", "ANGEL", "FAIRY");
    private final NumberSetting wingScale = new NumberSetting("Scale", "Wing 3D geometry scale factor", 1.0, 0.5, 2.5, 0.1);
    private final ColorSetting wingColor = new ColorSetting("Color", "Wing feather/membrane color", 0xFFFFFFFF);

    private float flapAngle = 0.0f;

    public WingsCosmetic() {
        super("wings-cosmetic", "Wings Cosmetic", "Client-rendered animated 3D dragon and angel wings attached to player back", Category.RENDER);
        addSettings(wingType, wingScale, wingColor);
    }

    public void tickFlap(float deltaSeconds, boolean isFlying) {
        float speed = isFlying ? 12.0f : 4.0f;
        flapAngle = (float) Math.sin(System.currentTimeMillis() * 0.005 * speed) * 35.0f;
    }

    public float getFlapAngle() { return flapAngle; }
    public String getWingType() { return wingType.getValue(); }
    public double getWingScale() { return wingScale.getValue(); }
    public int getWingColor() { return wingColor.getValue(); }
}
