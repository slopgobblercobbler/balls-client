package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * Removes the carved pumpkin head screen cutout overlay when wearing pumpkin helmets.
 */
public class PumpkinOverlayRemover extends Module {
    private final BooleanSetting keepCarvedBorder = new BooleanSetting("KeepBorder", "Keep subtle transparent outer border", false);

    public PumpkinOverlayRemover() {
        super("pumpkin-overlay-remover", "Pumpkin Overlay Remover", "Removes black carved pumpkin cutout screen obstructions", Category.RENDER);
        addSettings(keepCarvedBorder);
    }

    public boolean isKeepCarvedBorder() { return keepCarvedBorder.getValue(); }
}
