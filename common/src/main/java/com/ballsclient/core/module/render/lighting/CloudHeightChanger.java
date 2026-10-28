package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Adjusts the Y-altitude of the world cloud layer.
 */
public class CloudHeightChanger extends Module {
    private final NumberSetting cloudHeightY = new NumberSetting("HeightY", "Altitude Y of volumetric cloud layer", 192.0, 64.0, 384.0, 8.0);

    public CloudHeightChanger() {
        super("cloud-height-changer", "Cloud Height Changer", "Customizes Y-altitude placement of world clouds", Category.RENDER);
        addSettings(cloudHeightY);
    }

    public double getCloudHeightY() { return cloudHeightY.getValue(); }
}
