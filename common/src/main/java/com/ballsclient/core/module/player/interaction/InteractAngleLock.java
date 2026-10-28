package com.ballsclient.core.module.player.interaction;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * InteractAngleLock — Snaps pitch and yaw to exact cardinal 90°/45° angles for precision placement.
 */
public class InteractAngleLock extends Module {
    private final ModeSetting cardinalMode = new ModeSetting("CardinalMode", "Angle quantization grid", "SnapNearest90", "SnapNearest90", "SnapNearest45", "FixedPitch");
    private final NumberSetting fixedPitch = new NumberSetting("FixedPitch", "Target placement pitch angle", 82.5, -90.0, 90.0, 0.5);
    private final BooleanSetting sneakOnly = new BooleanSetting("SneakOnly", "Activate snapping only while sneaking", false);

    public InteractAngleLock() {
        super("interact-angle-lock", "Interact Angle Lock", "Snaps pitch and yaw to exact cardinal 90°/45° angles for precision placement", Category.PLAYER);
        addSettings(cardinalMode, fixedPitch, sneakOnly);
    }

    public ModeSetting getCardinalMode() {
        return cardinalMode;
    }

    public NumberSetting getFixedPitch() {
        return fixedPitch;
    }

    public BooleanSetting getSneakOnly() {
        return sneakOnly;
    }

    public static float snapYaw(float currentYaw, String mode) {
        float normalized = (currentYaw % 360.0f + 360.0f) % 360.0f;
        if ("SnapNearest45".equalsIgnoreCase(mode)) {
            return Math.round(normalized / 45.0f) * 45.0f;
        }
        // SnapNearest90 or default
        return Math.round(normalized / 90.0f) * 90.0f;
    }

    public static float snapPitch(float currentPitch, float fixedVal) {
        return fixedVal;
    }
}
