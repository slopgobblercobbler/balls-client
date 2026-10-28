package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Modifies the camera viewport aspect ratio (e.g., 4:3 stretched, 21:9 ultrawide letterboxing).
 */
public class AspectRationChanger extends Module {
    private final NumberSetting aspectRatio = new NumberSetting("Ratio", "Camera viewport aspect ratio (Width / Height)", 1.777, 0.5, 2.5, 0.05);

    public AspectRationChanger() {
        super("aspect-ratio-changer", "Aspect Ratio Changer", "Stretches or squashes projection matrix aspect ratio", Category.RENDER);
        addSettings(aspectRatio);
    }

    public double getAspectRatio() { return aspectRatio.getValue(); }
}
