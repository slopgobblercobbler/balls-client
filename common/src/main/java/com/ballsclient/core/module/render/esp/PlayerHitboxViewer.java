package com.ballsclient.core.module.render.esp;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;

/**
 * Renders actual server vs client hitboxes, eye-height lines, and look direction vectors for players.
 */
public class PlayerHitboxViewer extends Module {
    private final BooleanSetting showLookRay = new BooleanSetting("ShowLookRay", "Draw 3D forward line indicating target aiming direction", true);
    private final ColorSetting hitboxColor = new ColorSetting("HitboxColor", "Wireframe hitbox color", 0xFFFFFFFF);

    public PlayerHitboxViewer() {
        super("player-hitbox-viewer", "Player Hitbox Viewer", "Displays exact client and server hitboxes and look direction vectors", Category.RENDER);
        addSettings(showLookRay, hitboxColor);
    }

    public boolean isShowLookRay() {
        return showLookRay.getValue();
    }

    public int getHitboxColor() {
        return hitboxColor.getValue();
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Draw hitbox wireframes and look rays
    }
}
