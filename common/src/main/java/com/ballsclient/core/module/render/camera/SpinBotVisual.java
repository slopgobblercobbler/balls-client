package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Purely cosmetic client-side model spin animation rotating rapidly at up to 1440 RPM.
 */
public class SpinBotVisual extends Module {
    private final NumberSetting spinSpeedRpm = new NumberSetting("SpeedRPM", "Rotation speed in revolutions per minute", 360.0, 60.0, 1440.0, 60.0);
    private final BooleanSetting yawOnly = new BooleanSetting("YawOnly", "Rotate only horizontal yaw axis", true);

    private float currentSpinAngle = 0.0f;

    public SpinBotVisual() {
        super("spin-bot-visual", "SpinBot Visual", "Client-side high-speed model rotation for cosmetic swagger", Category.RENDER);
        addSettings(spinSpeedRpm, yawOnly);
    }

    public void tickSpin(float deltaSeconds) {
        float degPerSec = (float) (spinSpeedRpm.getValue() * 6.0); // 360 deg / 60 sec = 6
        currentSpinAngle = (currentSpinAngle + degPerSec * deltaSeconds) % 360.0f;
    }

    public float getCurrentSpinAngle() { return currentSpinAngle; }
    public boolean isYawOnly() { return yawOnly.getValue(); }
}
