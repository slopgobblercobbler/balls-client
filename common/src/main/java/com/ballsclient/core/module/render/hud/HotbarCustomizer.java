package com.ballsclient.core.module.render.hud;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Modern floating hotbar customizer with smooth animations and rounded slot outlines.
 */
public class HotbarCustomizer extends Module {
    private final NumberSetting opacity = new NumberSetting("Opacity", "Background container opacity", 0.8, 0.0, 1.0, 0.05);
    private final ColorSetting highlightColor = new ColorSetting("HighlightColor", "Slot selection highlight color", 0x80FFFFFF);
    private final BooleanSetting roundedBorders = new BooleanSetting("RoundedBorders", "Render rounded slot borders", true);

    private int selectedSlot = 0;
    private float animatedSlotX = 0.0f;

    public HotbarCustomizer() {
        super("hotbar-customizer", "Hotbar Customizer", "Modern minimalist floating hotbar with smooth slot transitions", Category.RENDER);
        addSettings(opacity, highlightColor, roundedBorders);
    }

    public void setSelectedSlot(int slot) {
        this.selectedSlot = Math.max(0, Math.min(8, slot));
    }

    public void updateAnimation(float delta) {
        float targetX = selectedSlot * 20.0f;
        animatedSlotX += (targetX - animatedSlotX) * Math.min(1.0f, delta * 15.0f);
    }

    public int getSelectedSlot() { return selectedSlot; }
    public float getAnimatedSlotX() { return animatedSlotX; }
    public double getOpacity() { return opacity.getValue(); }
    public boolean hasRoundedBorders() { return roundedBorders.getValue(); }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Render custom hotbar
    }
}
