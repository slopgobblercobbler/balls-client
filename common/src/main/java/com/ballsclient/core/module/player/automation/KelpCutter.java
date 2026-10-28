package com.ballsclient.core.module.player.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * KelpCutter — Severs tall kelp columns at root level to float items up water streams.
 */
public class KelpCutter extends Module {
    private final NumberSetting minKelpHeight = new NumberSetting("MinKelpHeight", "Minimum kelp plant height to cut", 5.0, 3.0, 25.0, 1.0);
    private final NumberSetting severOffset = new NumberSetting("SeverOffset", "Height offset above bottom root block to break", 1.0, 1.0, 3.0, 1.0);

    public KelpCutter() {
        super("kelp-cutter", "Kelp Cutter", "Severs tall kelp columns at root level to float items up water streams", Category.PLAYER);
        addSettings(minKelpHeight, severOffset);
    }

    public NumberSetting getMinKelpHeight() {
        return minKelpHeight;
    }

    public NumberSetting getSeverOffset() {
        return severOffset;
    }

    public static int getCutElevation(int rootY, int currentHeight, double minHeight, double offset) {
        if (currentHeight < minHeight) return -1;
        return rootY + (int) offset;
    }
}
