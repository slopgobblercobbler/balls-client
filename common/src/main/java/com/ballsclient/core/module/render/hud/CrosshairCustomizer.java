package com.ballsclient.core.module.render.hud;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Customizable HUD reticle with adjustable thickness, size, gap, dynamic bloom, and coloring.
 */
public class CrosshairCustomizer extends Module {
    private final NumberSetting size = new NumberSetting("Size", "Length of crosshair lines", 8.0, 1.0, 30.0, 1.0);
    private final NumberSetting thickness = new NumberSetting("Thickness", "Width of crosshair lines", 2.0, 1.0, 10.0, 1.0);
    private final NumberSetting gap = new NumberSetting("Gap", "Center gap distance", 3.0, 0.0, 20.0, 1.0);
    private final ColorSetting color = new ColorSetting("Color", "Crosshair line color", 0xFFFFFFFF);

    private float dynamicBloom = 0.0f;

    public CrosshairCustomizer() {
        super("crosshair-customizer", "Crosshair Customizer", "Configurable crosshair size, gap, thickness, and color", Category.RENDER);
        addSettings(size, thickness, gap, color);
    }

    public void applyRecoilSpread(float spreadAmount) {
        this.dynamicBloom = Math.min(15.0f, this.dynamicBloom + spreadAmount);
    }

    public void updateDecay(float deltaSeconds) {
        this.dynamicBloom = Math.max(0.0f, this.dynamicBloom - (deltaSeconds * 20.0f));
    }

    public double getEffectiveGap() {
        return gap.getValue() + dynamicBloom;
    }

    public double getSize() { return size.getValue(); }
    public double getThickness() { return thickness.getValue(); }
    public int getColor() { return color.getValue(); }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Render custom crosshair
    }
}
