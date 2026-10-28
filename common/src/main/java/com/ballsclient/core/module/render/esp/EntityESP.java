package com.ballsclient.core.module.render.esp;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.ModeSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 3D bounding boxes and 2D corner brackets around entities with health gradient coloring.
 */
public class EntityESP extends Module {
    private final ModeSetting style = new ModeSetting("Style", "ESP rendering geometry", "BOX3D", "BOX3D", "BRACKET2D");
    private final BooleanSetting showHealth = new BooleanSetting("ShowHealth", "Gradient color box based on remaining health percentage", true);
    private final ColorSetting boxColor = new ColorSetting("BoxColor", "Bounding box line color", 0xFFFF0000);

    public static class EntityBox {
        public final double minX, minY, minZ;
        public final double maxX, maxY, maxZ;
        public final float healthPercent;
        public final String name;

        public EntityBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float healthPercent, String name) {
            this.minX = minX;
            this.minY = minY;
            this.minZ = minZ;
            this.maxX = maxX;
            this.maxY = maxY;
            this.maxZ = maxZ;
            this.healthPercent = Math.min(100.0f, Math.max(0.0f, healthPercent));
            this.name = name != null ? name : "";
        }
    }

    private final List<EntityBox> activeBoxes = new ArrayList<>();

    public EntityESP() {
        super("entity-esp", "Entity ESP", "3D bounding boxes and bracket highlights for surrounding entities", Category.RENDER);
        addSettings(style, showHealth, boxColor);
    }

    public synchronized void updateBoxes(List<EntityBox> boxes) {
        activeBoxes.clear();
        if (boxes != null) {
            activeBoxes.addAll(boxes);
        }
    }

    public synchronized List<EntityBox> getActiveBoxes() {
        return Collections.unmodifiableList(new ArrayList<>(activeBoxes));
    }

    public int getBoxColorForHealth(float healthPercent) {
        if (!showHealth.getValue()) return boxColor.getValue();
        float fraction = healthPercent / 100.0f;
        int r = (int) ((1.0f - fraction) * 255.0f);
        int g = (int) (fraction * 255.0f);
        return 0xFF000000 | (r << 16) | (g << 8);
    }

    public String getStyle() { return style.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // 3D rendering pass
    }
}
