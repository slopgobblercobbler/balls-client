package com.ballsclient.core.module.render.esp;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Highlights shulker boxes with outlines dynamically tinted to match their exact dye color.
 */
public class ShulkerColorESP extends Module {
    private final NumberSetting outlineWidth = new NumberSetting("OutlineWidth", "Thickness of wireframe edges", 2.0, 1.0, 5.0, 1.0);
    private final NumberSetting boxAlpha = new NumberSetting("BoxAlpha", "Translucency alpha of bounding faces", 0.5, 0.1, 1.0, 0.05);

    public static class ColoredShulker {
        public final int x, y, z;
        public final int dyeColorRgb;

        public ColoredShulker(int x, int y, int z, int dyeColorRgb) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.dyeColorRgb = dyeColorRgb;
        }
    }

    private final List<ColoredShulker> shulkers = new ArrayList<>();

    public ShulkerColorESP() {
        super("shulker-color-esp", "Shulker Color ESP", "Dynamic wireframes tinted to match individual shulker box dye colors", Category.RENDER);
        addSettings(outlineWidth, boxAlpha);
    }

    public synchronized void updateShulkers(List<ColoredShulker> list) {
        shulkers.clear();
        if (list != null) {
            shulkers.addAll(list);
        }
    }

    public synchronized List<ColoredShulker> getShulkers() {
        return Collections.unmodifiableList(new ArrayList<>(shulkers));
    }

    public int getShulkerColorWithAlpha(int rgb) {
        int a = (int) (boxAlpha.getValue() * 255.0);
        return (a << 24) | (rgb & 0x00FFFFFF);
    }

    public double getOutlineWidth() { return outlineWidth.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render 3D shulker boxes
    }
}
