package com.ballsclient.core.module.combat.tactical;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Surround Helper — Assists in placing 4 defensive obsidian blocks around player feet against crystals
 */
public class SurroundHelper extends Module {
    private final BooleanSetting centerPlayer = new BooleanSetting("CenterPlayer", "Center player on block before placing", true);
    private final BooleanSetting triggerOnHoleEntry = new BooleanSetting("TriggerOnHoleEntry", "Automatically activate upon falling into 1x1 hole", true);

    public SurroundHelper() {
        super("surround-helper", "Surround Helper", "Assists in placing 4 defensive obsidian blocks around player feet against crystals", Category.COMBAT);
        addSettings(centerPlayer, triggerOnHoleEntry);
    }

    public boolean isCenterPlayer() {
        return centerPlayer.getValue();
    }

    public boolean isTriggerOnHoleEntry() {
        return triggerOnHoleEntry.getValue();
    }

    public double[] calculateBlockCenter(double playerX, double playerZ) {
        return new double[]{Math.floor(playerX) + 0.5, Math.floor(playerZ) + 0.5};
    }
}
