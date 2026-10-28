package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * ChestStealerFilter — Filters item selection during automated container looting.
 */
public class ChestStealerFilter extends Module {
    private final BooleanSetting ignoreTrash = new BooleanSetting("IgnoreTrash", "Skip low-value block and item types", true);

    public ChestStealerFilter() {
        super("chest-stealer-filter", "Chest Stealer Filter", "Filters item selection during automated looting", Category.INVENTORY);
        addSettings(ignoreTrash);
    }

    public BooleanSetting getIgnoreTrash() {
        return ignoreTrash;
    }
}
