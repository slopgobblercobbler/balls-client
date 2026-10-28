package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;

/**
 * Transforms elytra appearance into hang glider wings or high-tech energy jetpack thrusters.
 */
public class GliderCosmetics extends Module {
    private final ModeSetting model = new ModeSetting("Model", "Flight cosmetic model", "GLIDER", "GLIDER", "HANG_GLIDER", "JETPACK");

    public GliderCosmetics() {
        super("glider-cosmetics", "Glider Cosmetics", "Replaces standard elytra with stylized glider wings or jetpack models", Category.RENDER);
        addSettings(model);
    }

    public String getGliderModel() { return model.getValue(); }
}
