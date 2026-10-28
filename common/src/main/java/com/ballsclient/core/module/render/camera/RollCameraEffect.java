package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Rolls camera bank angle slightly on horizontal strafing for cockpit flight simulator feel.
 */
public class RollCameraEffect extends Module {
    private final NumberSetting maxRollDegrees = new NumberSetting("MaxRoll", "Maximum camera roll tilt in degrees", 5.0, 1.0, 15.0, 1.0);
    private final NumberSetting rollSpeed = new NumberSetting("RollSpeed", "Roll transition response speed", 4.0, 1.0, 10.0, 0.5);

    private float currentRoll = 0.0f;

    public RollCameraEffect() {
        super("roll-camera-effect", "Roll Camera Effect", "Banks camera roll angle during horizontal strafing movement", Category.RENDER);
        addSettings(maxRollDegrees, rollSpeed);
    }

    public void updateRoll(float strafeDirection, float deltaSeconds) {
        float targetRoll = -strafeDirection * maxRollDegrees.getFloatValue();
        float diff = targetRoll - currentRoll;
        currentRoll += diff * Math.min(1.0f, deltaSeconds * rollSpeed.getFloatValue());
    }

    public float getCurrentRoll() { return currentRoll; }
    public double getMaxRollDegrees() { return maxRollDegrees.getValue(); }
}
