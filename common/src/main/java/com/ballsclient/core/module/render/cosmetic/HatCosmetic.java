package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Client-side decorative 3D headwear (Wizard Hat, Top Hat, Royal Crown, Santa Hat).
 */
public class HatCosmetic extends Module {
    private final ModeSetting hatType = new ModeSetting("Type", "Hat 3D geometry style", "CROWN", "WIZARD", "TOP_HAT", "CROWN", "SANTA");
    private final NumberSetting scale = new NumberSetting("Scale", "Hat geometry scale", 1.0, 0.5, 2.0, 0.1);

    public HatCosmetic() {
        super("hat-cosmetic", "Hat Cosmetic", "Decorative 3D headwear cosmetics for player character", Category.RENDER);
        addSettings(hatType, scale);
    }

    public String getHatType() { return hatType.getValue(); }
    public double getScale() { return scale.getValue(); }
}
