package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * High-resolution custom heraldic emblem generator for offhand shields.
 */
public class ShieldBannerCustomizer extends Module {
    private final ModeSetting emblemType = new ModeSetting("Emblem", "Heraldic crest pattern", "DRAGON", "DRAGON", "SKULL", "PHOENIX", "CROSS");
    private final ColorSetting emblemColor = new ColorSetting("Color", "Heraldic emblem color tint", 0xFFFFD700);

    public ShieldBannerCustomizer() {
        super("shield-banner-customizer", "Shield Banner Customizer", "Procedural heraldic crest patterns for offhand shields", Category.RENDER);
        addSettings(emblemType, emblemColor);
    }

    public String getEmblemType() { return emblemType.getValue(); }
    public int getEmblemColor() { return emblemColor.getValue(); }
}
