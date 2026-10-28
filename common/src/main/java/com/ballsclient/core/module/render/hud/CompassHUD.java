package com.ballsclient.core.module.render.hud;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Skyrim-style horizontal ribbon compass with 360-degree cardinal markers.
 */
public class CompassHUD extends Module {
    private final NumberSetting ribbonWidth = new NumberSetting("RibbonWidth", "Width of horizontal compass bar", 240.0, 100.0, 500.0, 20.0);
    private final ColorSetting markerColor = new ColorSetting("MarkerColor", "Color of cardinal direction markers", 0xFFFFAA00);
    private final BooleanSetting showWaypoints = new BooleanSetting("ShowWaypoints", "Project waypoint icons onto compass ribbon", true);

    private float playerYaw = 0.0f;

    public CompassHUD() {
        super("compass-hud", "Compass HUD", "Skyrim-style 360-degree horizontal ribbon compass", Category.RENDER);
        addSettings(ribbonWidth, markerColor, showWaypoints);
    }

    public void setPlayerYaw(float yaw) {
        this.playerYaw = (yaw % 360 + 360) % 360;
    }

    public float getPlayerYaw() {
        return playerYaw;
    }

    public double calculateMarkerOffset(float markerAngle, double ribbonPixelWidth) {
        float diff = (markerAngle - playerYaw + 180 + 360) % 360 - 180;
        // Map 180 degrees to ribbon half-width
        return (diff / 180.0) * (ribbonPixelWidth / 2.0);
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Render horizontal compass ribbon
    }
}
