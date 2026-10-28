package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Exponential camera rotational inertia dampening for montage recording and cinematic panning.
 */
public class CinematicCameraEasing extends Module {
    private final NumberSetting inertiaFactor = new NumberSetting("Inertia", "Camera rotational dampening factor (0.1 to 0.99)", 0.85, 0.1, 0.99, 0.05);
    private final BooleanSetting rollStabilizer = new BooleanSetting("RollStabilizer", "Stabilize horizon tilt against sudden mouse movements", true);

    private float smoothYawVelocity = 0.0f;
    private float smoothPitchVelocity = 0.0f;

    public CinematicCameraEasing() {
        super("cinematic-camera-easing", "Cinematic Camera Easing", "Ultra-smooth exponential rotational inertia for filming montages", Category.RENDER);
        addSettings(inertiaFactor, rollStabilizer);
    }

    public float[] applyInertia(float targetYawVel, float targetPitchVel, float delta) {
        float inertia = inertiaFactor.getFloatValue();
        smoothYawVelocity = smoothYawVelocity * inertia + targetYawVel * (1.0f - inertia);
        smoothPitchVelocity = smoothPitchVelocity * inertia + targetPitchVel * (1.0f - inertia);
        return new float[]{smoothYawVelocity, smoothPitchVelocity};
    }

    public double getInertiaFactor() { return inertiaFactor.getValue(); }
    public boolean isRollStabilizer() { return rollStabilizer.getValue(); }
}
