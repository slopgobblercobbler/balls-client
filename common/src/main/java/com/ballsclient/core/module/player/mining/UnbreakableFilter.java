package com.ballsclient.core.module.player.mining;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * UnbreakableFilter — Cancels break packets targeted at bedrock, end portal frames, or barriers.
 */
public class UnbreakableFilter extends Module {
    private final BooleanSetting filterBedrock = new BooleanSetting("FilterBedrock", "Cancel break interaction on bedrock", true);
    private final BooleanSetting filterEndPortal = new BooleanSetting("FilterEndPortal", "Cancel break interaction on End portal frames", true);
    private final BooleanSetting filterBarriers = new BooleanSetting("FilterBarriers", "Cancel break interaction on barrier blocks", true);

    public UnbreakableFilter() {
        super("unbreakable-filter", "Unbreakable Filter", "Cancels break packets targeted at bedrock, end portal frames, or barriers", Category.PLAYER);
        addSettings(filterBedrock, filterEndPortal, filterBarriers);
    }

    public BooleanSetting getFilterBedrock() {
        return filterBedrock;
    }

    public BooleanSetting getFilterEndPortal() {
        return filterEndPortal;
    }

    public BooleanSetting getFilterBarriers() {
        return filterBarriers;
    }

    public static boolean isUnbreakable(double blockHardness) {
        return blockHardness < 0.0;
    }
}
