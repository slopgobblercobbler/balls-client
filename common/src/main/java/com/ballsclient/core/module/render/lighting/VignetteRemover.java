package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * Removes the dark edge vignette overlay caused by low light levels or Nether fog.
 */
public class VignetteRemover extends Module {
    private final BooleanSetting removeNetherVignette = new BooleanSetting("NetherVignette", "Remove reddish Nether vignette", true);

    public VignetteRemover() {
        super("vignette-remover", "Vignette Remover", "Eliminates dark screen-edge vignette shadows", Category.RENDER);
        addSettings(removeNetherVignette);
    }

    public boolean isRemoveNetherVignette() { return removeNetherVignette.getValue(); }
}
