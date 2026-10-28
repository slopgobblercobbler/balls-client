package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * High-resolution custom crest and emblem renderer on shield faces.
 */
public class ShieldBannerOverlay extends Module {
    private final BooleanSetting customCrest = new BooleanSetting("CustomCrest", "Render high-res crest on front shield face", true);
    private final NumberSetting resolution = new NumberSetting("Resolution", "Texture resolution (16 to 256)", 64.0, 16.0, 256.0, 16.0);

    public ShieldBannerOverlay() {
        super("shield-banner-overlay", "Shield Banner Overlay", "Renders high-resolution custom heraldic emblems on player shields", Category.RENDER);
        addSettings(customCrest, resolution);
    }

    public boolean isCustomCrest() { return customCrest.getValue(); }
    public int getResolution() { return resolution.getIntValue(); }
}
