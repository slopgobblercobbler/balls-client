package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Customizes entity damage flash color (neon cyan, rainbow, crimson, or purple).
 */
public class HitColorCustomizer extends Module {
    private final ColorSetting hitColor = new ColorSetting("HitColor", "Damage tint color", 0x80FF0000);
    private final NumberSetting flashDurationMs = new NumberSetting("FlashDurationMs", "Flash persistence in milliseconds", 200.0, 50.0, 1000.0, 50.0);

    public HitColorCustomizer() {
        super("hit-color-customizer", "Hit Color Customizer", "Customizes the red flash overlay when entities take damage", Category.RENDER);
        addSettings(hitColor, flashDurationMs);
    }

    public int getHitColor() { return hitColor.getValue(); }
    public double getFlashDurationMs() { return flashDurationMs.getValue(); }
}
