package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;

/**
 * ArrowQuiverPriority — Reorders arrow stacks so bow fires priority arrows (Harming II > Spectral > Standard).
 */
public class ArrowQuiverPriority extends Module {
    private final ModeSetting priority = new ModeSetting("Priority", "Priority arrow ammunition type", "Harming", "Harming", "Spectral", "Regular");

    public ArrowQuiverPriority() {
        super("arrow-quiver-priority", "Arrow Quiver Priority", "Reorders arrow stacks so bow fires priority arrows", Category.INVENTORY);
        addSettings(priority);
    }

    public ModeSetting getPriority() {
        return priority;
    }

    public static int getArrowPriorityRank(String itemId, String arrowPriorityMode) {
        if (itemId == null) return 99;
        String id = itemId.toLowerCase();
        if ("Harming".equalsIgnoreCase(arrowPriorityMode)) {
            if (id.contains("tipped_arrow") && id.contains("harming")) return 1;
            if (id.contains("spectral_arrow")) return 2;
            if (id.contains("arrow")) return 3;
        } else if ("Spectral".equalsIgnoreCase(arrowPriorityMode)) {
            if (id.contains("spectral_arrow")) return 1;
            if (id.contains("tipped_arrow")) return 2;
            if (id.contains("arrow")) return 3;
        } else {
            if (id.equals("minecraft:arrow") || id.endsWith(":arrow")) return 1;
            if (id.contains("spectral_arrow")) return 2;
            if (id.contains("tipped_arrow")) return 3;
        }
        return 99;
    }
}
