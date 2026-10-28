package com.ballsclient.core.module.inventory.sorting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * HotbarPrioritizationSort — Prioritizes hotbar slots during inventory sorting passes.
 */
public class HotbarPrioritizationSort extends Module {
    private final BooleanSetting prioritizeHotbar = new BooleanSetting("PrioritizeHotbar", "Fill hotbar before main inventory", true);

    public HotbarPrioritizationSort() {
        super("hotbar-prioritization-sort", "Hotbar Prioritization Sort", "Prioritizes hotbar slots during inventory sorting passes", Category.INVENTORY);
        addSettings(prioritizeHotbar);
    }

    public BooleanSetting getPrioritizeHotbar() {
        return prioritizeHotbar;
    }
}
