package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Customizable first-person hand and held item position (X, Y, Z translation) and scaling.
 */
public class ViewModelModifier extends Module {
    private final NumberSetting posX = new NumberSetting("PosX", "Horizontal hand offset", 0.0, -2.0, 2.0, 0.05);
    private final NumberSetting posY = new NumberSetting("PosY", "Vertical hand offset", 0.0, -2.0, 2.0, 0.05);
    private final NumberSetting posZ = new NumberSetting("PosZ", "Forward/backward hand offset", 0.0, -2.0, 2.0, 0.05);
    private final NumberSetting scale = new NumberSetting("Scale", "Item and hand model scale", 1.0, 0.2, 2.0, 0.05);

    public ViewModelModifier() {
        super("view-model-modifier", "View Model Modifier", "Adjusts first-person hand and held item position, depth, and scale", Category.RENDER);
        addSettings(posX, posY, posZ, scale);
    }

    public double getPosX() { return posX.getValue(); }
    public double getPosY() { return posY.getValue(); }
    public double getPosZ() { return posZ.getValue(); }
    public double getScale() { return scale.getValue(); }
}
