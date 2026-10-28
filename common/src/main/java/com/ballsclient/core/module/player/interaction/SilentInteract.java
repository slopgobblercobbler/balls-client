package com.ballsclient.core.module.player.interaction;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * SilentInteract — Dispatches packet-side interaction angles without moving player camera.
 */
public class SilentInteract extends Module {
    private final BooleanSetting pitchOnly = new BooleanSetting("PitchOnly", "Only adjust packet pitch, leave yaw untouched", false);
    private final NumberSetting maxAngleDiff = new NumberSetting("MaxAngleDiff", "Maximum allowed divergence from camera angle", 60.0, 15.0, 180.0, 5.0);
    private final BooleanSetting lookCorrection = new BooleanSetting("LookCorrection", "Smooth packet angles toward target center", false);

    public SilentInteract() {
        super("silent-interact", "Silent Interact", "Dispatches packet-side interaction angles without moving player camera", Category.PLAYER);
        addSettings(pitchOnly, maxAngleDiff, lookCorrection);
    }

    public BooleanSetting getPitchOnly() {
        return pitchOnly;
    }

    public NumberSetting getMaxAngleDiff() {
        return maxAngleDiff;
    }

    public BooleanSetting getLookCorrection() {
        return lookCorrection;
    }

    public static float[] calculatePacketAngles(double eyeX, double eyeY, double eyeZ, double targetX, double targetY, double targetZ) {
        double dx = targetX - eyeX;
        double dy = targetY - eyeY;
        double dz = targetZ - eyeZ;
        double horizDist = Math.sqrt(dx * dx + dz * dz);

        float yaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
        float pitch = (float) Math.toDegrees(-Math.atan2(dy, horizDist));
        return new float[]{yaw, pitch};
    }
}
