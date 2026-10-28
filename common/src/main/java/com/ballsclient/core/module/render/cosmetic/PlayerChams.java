package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;

/**
 * Renders player models through walls with flat neon solid colors or textured glowing silhouettes.
 */
public class PlayerChams extends Module {
    private final ColorSetting wallColor = new ColorSetting("WallColor", "Color when occluded behind walls (Red)", 0x88FF0000);
    private final ColorSetting visibleColor = new ColorSetting("VisibleColor", "Color when directly visible (Green)", 0x8800FF00);
    private final BooleanSetting throughWalls = new BooleanSetting("ThroughWalls", "Bypass depth buffer test to render through geometry", true);

    public PlayerChams() {
        super("player-chams", "Player Chams", "Colored silhouette chams rendering player models through walls", Category.RENDER);
        addSettings(wallColor, visibleColor, throughWalls);
    }

    public int getWallColor() { return wallColor.getValue(); }
    public int getVisibleColor() { return visibleColor.getValue(); }
    public boolean isThroughWalls() { return throughWalls.getValue(); }
}
