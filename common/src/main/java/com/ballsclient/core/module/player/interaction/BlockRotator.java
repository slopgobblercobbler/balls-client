package com.ballsclient.core.module.player.interaction;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * BlockRotator — Computes sub-face click coordinates to orient directional blocks (pistons, observers).
 */
public class BlockRotator extends Module {
    private final ModeSetting defaultFacing = new ModeSetting("DefaultFacing", "Target directional block orientation", "PlayerFacing", "PlayerFacing", "Inverted", "Upward", "Downward");
    private final BooleanSetting autoPiston = new BooleanSetting("AutoPiston", "Automatically orient pistons towards target plane", true);

    public BlockRotator() {
        super("block-rotator", "Block Rotator", "Computes sub-face click coordinates to orient directional blocks (pistons, observers)", Category.PLAYER);
        addSettings(defaultFacing, autoPiston);
    }

    public ModeSetting getDefaultFacing() {
        return defaultFacing;
    }

    public BooleanSetting getAutoPiston() {
        return autoPiston;
    }

    public static int getTargetSubFace(double playerPitch, double playerYaw, String desiredFacing) {
        if ("Upward".equalsIgnoreCase(desiredFacing)) {
            return 1; // UP
        }
        if ("Downward".equalsIgnoreCase(desiredFacing)) {
            return 0; // DOWN
        }
        if (playerPitch > 55.0) return 0; // DOWN
        if (playerPitch < -55.0) return 1; // UP

        float yawNorm = (float) ((playerYaw % 360.0 + 360.0) % 360.0);
        int cardinalIndex = Math.round(yawNorm / 90.0f) % 4;
        // 0: South, 1: West, 2: North, 3: East
        int[] sideFaces = {3, 4, 2, 5};
        int selected = sideFaces[cardinalIndex];

        if ("Inverted".equalsIgnoreCase(desiredFacing)) {
            // Opposite face
            int[] opposite = {1, 0, 3, 2, 5, 4};
            return opposite[selected];
        }
        return selected;
    }
}
