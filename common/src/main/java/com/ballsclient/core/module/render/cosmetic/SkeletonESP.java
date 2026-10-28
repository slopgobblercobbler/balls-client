package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Renders stick-figure bone joints of players and mobs showing exact limb positions and aiming angles.
 */
public class SkeletonESP extends Module {
    private final ColorSetting boneColor = new ColorSetting("BoneColor", "Skeletal wireframe line color", 0xFFFFFFFF);
    private final NumberSetting lineWidth = new NumberSetting("LineWidth", "Bone joint line thickness", 1.5, 0.5, 4.0, 0.5);

    public SkeletonESP() {
        super("skeleton-esp", "Skeleton ESP", "Renders stick-figure joint lines showing exact player limb rotations", Category.RENDER);
        addSettings(boneColor, lineWidth);
    }

    public int getBoneColor() { return boneColor.getValue(); }
    public double getLineWidth() { return lineWidth.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render skeletal joint vectors
    }
}
