package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.HashSet;
import java.util.Set;

/**
 * Highlights matching search query items inside container GUIs and dims non-matching slots.
 */
public class ChestSearchHighlight extends Module {
    private final ColorSetting matchColor = new ColorSetting("MatchColor", "Outline color on matching items", 0xFFFFD700);
    private final NumberSetting dimAlpha = new NumberSetting("DimAlpha", "Darkness opacity on non-matching items", 0.4, 0.1, 0.9, 0.05);

    private String searchQuery = "";
    private final Set<Integer> matchingSlots = new HashSet<>();

    public ChestSearchHighlight() {
        super("chest-search-highlight", "Chest Search Highlight", "Highlights matching items in container GUIs and dims others", Category.RENDER);
        addSettings(matchColor, dimAlpha);
    }

    public void setSearchQuery(String query) {
        this.searchQuery = query != null ? query.trim().toLowerCase() : "";
    }

    public void updateMatchingSlots(Set<Integer> slots) {
        matchingSlots.clear();
        if (slots != null) {
            matchingSlots.addAll(slots);
        }
    }

    public boolean isSlotMatching(int slotIndex) {
        if (searchQuery.isEmpty()) return true;
        return matchingSlots.contains(slotIndex);
    }

    public String getSearchQuery() { return searchQuery; }
    public int getMatchColor() { return matchColor.getValue(); }
    public double getDimAlpha() { return dimAlpha.getValue(); }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Highlight slots in container screen
    }
}
