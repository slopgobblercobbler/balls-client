package com.ballsclient.core.module.movement.locomotion;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * Inverts movement keyboard inputs (WASD) for disorientation anti-aim.
 */
public class InvertedControls extends Module {
    private final BooleanSetting invertWASD = new BooleanSetting("InvertWASD", "Invert forward and strafe keyboard inputs", true);
    private final BooleanSetting invertPitch = new BooleanSetting("InvertPitch", "Invert pitch look direction", false);
    private final BooleanSetting invertYaw = new BooleanSetting("InvertYaw", "Invert yaw look direction", false);

    public InvertedControls() {
        super("inverted-controls", "Inverted Controls", "Inverts movement keyboard inputs (WASD) for disorientation anti-aim", Category.MOVEMENT);
        addSettings(invertWASD, invertPitch, invertYaw);
    }

    public boolean isInvertWASD() { return invertWASD.getValue(); }
    public boolean isInvertPitch() { return invertPitch.getValue(); }
    public boolean isInvertYaw() { return invertYaw.getValue(); }

    public double modifyForward(double forward) { return isEnabled() && isInvertWASD() ? -forward : forward; }
    public double modifyStrafe(double strafe) { return isEnabled() && isInvertWASD() ? -strafe : strafe; }
    public float modifyPitch(float pitch) { return isEnabled() && isInvertPitch() ? -pitch : pitch; }
    public float modifyYaw(float yaw) { return isEnabled() && isInvertYaw() ? (yaw + 180.0f) % 360.0f : yaw; }
}
