package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * AutoToolSwapper — Automatically selects the most effective tool in inventory for the targeted block.
 */
public class AutoToolSwapper extends Module {
    private final BooleanSetting preferSilkTouch = new BooleanSetting("PreferSilkTouch", "Prioritize silk touch when breaking delicate blocks", false);

    public AutoToolSwapper() {
        super("auto-tool-swapper", "Auto Tool Swapper", "Selects the most effective tool for targeted block", Category.INVENTORY);
        addSettings(preferSilkTouch);
    }

    public BooleanSetting getPreferSilkTouch() {
        return preferSilkTouch;
    }
}
