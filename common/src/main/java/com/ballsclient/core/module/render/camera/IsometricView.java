package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Orthographic projection camera recreating classic isometric ARPG / SimCity perspective.
 */
public class IsometricView extends Module {
    private final NumberSetting cameraHeight = new NumberSetting("Height", "Camera altitude above player", 20.0, 5.0, 50.0, 2.0);
    private final NumberSetting angleDegrees = new NumberSetting("Angle", "Pitch angle looking downward", 45.0, 15.0, 75.0, 5.0);

    public IsometricView() {
        super("isometric-view", "Isometric View", "Orthographic camera projection providing isometric ARPG perspective", Category.RENDER);
        addSettings(cameraHeight, angleDegrees);
    }

    public double getCameraHeight() { return cameraHeight.getValue(); }
    public double getAngleDegrees() { return angleDegrees.getValue(); }
}
