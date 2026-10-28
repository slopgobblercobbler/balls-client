package com.ballsclient.core.module.player.mining;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * AutoMine — Maintains continuous mining input while targeting valid blocks.
 */
public class AutoMine extends Module {
    private final ModeSetting mineDirection = new ModeSetting("MineDirection", "Excavation heading mode", "ForwardFacing", "ForwardFacing", "DownwardsStair", "UpwardStair");
    private final BooleanSetting stopOnLava = new BooleanSetting("StopOnLava", "Halt mining immediately upon uncovering lava", true);
    private final BooleanSetting stopOnGravel = new BooleanSetting("StopOnGravel", "Halt mining when unsupported gravel or sand above", true);

    public AutoMine() {
        super("auto-mine", "Auto Mine", "Maintains continuous mining input while targeting valid blocks", Category.PLAYER);
        addSettings(mineDirection, stopOnLava, stopOnGravel);
    }

    public ModeSetting getMineDirection() {
        return mineDirection;
    }

    public BooleanSetting getStopOnLava() {
        return stopOnLava;
    }

    public BooleanSetting getStopOnGravel() {
        return stopOnGravel;
    }

    public static boolean shouldHaltMining(boolean lavaAdjacent, boolean gravelAbove, boolean stopLava, boolean stopGravel) {
        if (stopLava && lavaAdjacent) return true;
        if (stopGravel && gravelAbove) return true;
        return false;
    }
}
