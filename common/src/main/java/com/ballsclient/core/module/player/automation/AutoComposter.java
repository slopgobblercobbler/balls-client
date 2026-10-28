package com.ballsclient.core.module.player.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * AutoComposter — Feeds composters with organic surplus until bone meal generates and collects it.
 */
public class AutoComposter extends Module {
    private final NumberSetting targetLevel = new NumberSetting("TargetLevel", "Composter fill level to trigger harvest", 7.0, 1.0, 8.0, 1.0);
    private final BooleanSetting autoCollect = new BooleanSetting("AutoCollect", "Automatically right click composter to take bone meal", true);

    public AutoComposter() {
        super("auto-composter", "Auto Composter", "Feeds composters with organic surplus until bone meal generates and collects it", Category.PLAYER);
        addSettings(targetLevel, autoCollect);
    }

    public NumberSetting getTargetLevel() {
        return targetLevel;
    }

    public BooleanSetting getAutoCollect() {
        return autoCollect;
    }

    public static boolean isCompostFull(int level) {
        return level >= 7;
    }
}
