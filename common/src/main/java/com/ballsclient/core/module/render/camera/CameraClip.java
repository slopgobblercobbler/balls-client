package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Allows the third-person camera ray to clip through solid blocks without snapping forward.
 */
public class CameraClip extends Module {
    private final NumberSetting maxDistance = new NumberSetting("MaxDistance", "Maximum third-person distance in blocks", 8.0, 2.0, 20.0, 0.5);
    private final BooleanSetting clipBlocks = new BooleanSetting("ClipBlocks", "Pass through solid block geometry", true);

    public CameraClip() {
        super("camera-clip", "Camera Clip", "Allows 3rd-person camera to pass through blocks without collision snapping", Category.RENDER);
        addSettings(maxDistance, clipBlocks);
    }

    public double getMaxDistance() { return maxDistance.getValue(); }
    public boolean isClipBlocks() { return clipBlocks.getValue(); }
}
