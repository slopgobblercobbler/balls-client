package com.ballsclient.core.module.render.esp;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 3D bounding boxes and floating stack count tags for dropped items.
 */
public class ItemESP extends Module {
    private final BooleanSetting showCount = new BooleanSetting("ShowCount", "Render item stack count next to name", true);
    private final BooleanSetting filterValuableOnly = new BooleanSetting("ValuableOnly", "Only highlight rare minerals, armor, and enchanted gear", false);
    private final ColorSetting defaultColor = new ColorSetting("DefaultColor", "Bounding box color for dropped items", 0xFF00FFFF);

    public static class DroppedItem {
        public final double x, y, z;
        public final String name;
        public final int count;
        public final boolean isValuable;

        public DroppedItem(double x, double y, double z, String name, int count, boolean isValuable) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.name = name != null ? name : "Item";
            this.count = count;
            this.isValuable = isValuable;
        }

        public String formatLabel(boolean includeCount) {
            return includeCount ? String.format("%s (x%d)", name, count) : name;
        }
    }

    private final List<DroppedItem> droppedItems = new ArrayList<>();

    public ItemESP() {
        super("item-esp", "Item ESP", "3D bounding boxes and labels for dropped ground items", Category.RENDER);
        addSettings(showCount, filterValuableOnly, defaultColor);
    }

    public synchronized void updateItems(List<DroppedItem> items) {
        droppedItems.clear();
        if (items != null) {
            for (DroppedItem it : items) {
                if (filterValuableOnly.getValue() && !it.isValuable) continue;
                droppedItems.add(it);
            }
        }
    }

    public synchronized List<DroppedItem> getDroppedItems() {
        return Collections.unmodifiableList(new ArrayList<>(droppedItems));
    }

    public boolean isShowCount() { return showCount.getValue(); }
    public int getDefaultColor() { return defaultColor.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render 3D item boxes
    }
}
