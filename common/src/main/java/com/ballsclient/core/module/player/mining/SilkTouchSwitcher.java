package com.ballsclient.core.module.player.mining;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * SilkTouchSwitcher — Dynamic tool switcher between Silk Touch and Fortune based on block drops.
 */
public class SilkTouchSwitcher extends Module {
    private final BooleanSetting autoSwitchGlass = new BooleanSetting("AutoSwitchGlass", "Require Silk Touch for glass blocks and panes", true);
    private final BooleanSetting autoSwitchIce = new BooleanSetting("AutoSwitchIce", "Require Silk Touch for ice, packed ice, and blue ice", true);
    private final BooleanSetting autoSwitchSpawner = new BooleanSetting("AutoSwitchSpawner", "Preserve mob spawner blocks with Silk Touch", true);

    public SilkTouchSwitcher() {
        super("silk-touch-switcher", "Silk Touch Switcher", "Dynamic tool switcher between Silk Touch and Fortune based on block drops", Category.PLAYER);
        addSettings(autoSwitchGlass, autoSwitchIce, autoSwitchSpawner);
    }

    public BooleanSetting getAutoSwitchGlass() {
        return autoSwitchGlass;
    }

    public BooleanSetting getAutoSwitchIce() {
        return autoSwitchIce;
    }

    public BooleanSetting getAutoSwitchSpawner() {
        return autoSwitchSpawner;
    }

    public static boolean requiresSilkTouch(String blockId, boolean glass, boolean ice, boolean spawner) {
        if (blockId == null) return false;
        String idLower = blockId.toLowerCase();
        if (glass && idLower.contains("glass")) return true;
        if (ice && idLower.contains("ice")) return true;
        if (spawner && idLower.contains("spawner")) return true;
        if (idLower.contains("ender_chest") || idLower.contains("amethyst_cluster") || idLower.contains("bookshelf")) return true;
        return false;
    }
}
