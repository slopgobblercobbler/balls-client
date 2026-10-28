package com.ballsclient.core.module.inventory.container;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ContainerSearchHighlighter — Real-time text/regex search highlighting matching items and dimming non-matches.
 */
public class ContainerSearchHighlighter extends Module {
    private final NumberSetting dimAlpha = new NumberSetting("DimAlpha", "Opacity of non-matching container slots", 0.3, 0.1, 1.0, 0.05);
    private final ColorSetting highlightColor = new ColorSetting("HighlightColor", "Highlight border color for matched items", 0xFF00FF00);

    public ContainerSearchHighlighter() {
        super("container-search-highlighter", "Container Search Highlighter", "Real-time search highlighting matching items and dimming non-matches", Category.INVENTORY);
        addSettings(dimAlpha, highlightColor);
    }

    public NumberSetting getDimAlpha() {
        return dimAlpha;
    }

    public ColorSetting getHighlightColor() {
        return highlightColor;
    }

    public static boolean matchesQuery(ItemSlot slot, String query) {
        if (slot == null || slot.isEmpty()) return false;
        if (query == null || query.trim().isEmpty()) return true;
        String q = query.trim().toLowerCase();
        if (slot.itemId() != null && slot.itemId().toLowerCase().contains(q)) {
            return true;
        }
        if (slot.displayName() != null && slot.displayName().toLowerCase().contains(q)) {
            return true;
        }
        return false;
    }
}
