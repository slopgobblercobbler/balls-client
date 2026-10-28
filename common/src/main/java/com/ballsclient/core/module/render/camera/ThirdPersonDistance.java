package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Extends third-person camera distance up to 25 blocks with customizable horizontal and vertical offsets.
 */
public class ThirdPersonDistance extends Module {
    private final NumberSetting distance = new NumberSetting("Distance", "Camera distance in blocks", 5.0, 1.0, 25.0, 0.5);
    private final NumberSetting horizontalOffset = new NumberSetting("HorizontalOffset", "Shoulder horizontal offset in blocks", 0.0, -5.0, 5.0, 0.25);
    private final NumberSetting verticalOffset = new NumberSetting("VerticalOffset", "Over-the-shoulder vertical offset", 0.0, -5.0, 5.0, 0.25);

    public ThirdPersonDistance() {
        super("third-person-distance", "Third Person Distance", "Customizable third-person camera distance and shoulder offsets", Category.RENDER);
        addSettings(distance, horizontalOffset, verticalOffset);
    }

    public double getDistance() { return distance.getValue(); }
    public double getHorizontalOffset() { return horizontalOffset.getValue(); }
    public double getVerticalOffset() { return verticalOffset.getValue(); }
}
