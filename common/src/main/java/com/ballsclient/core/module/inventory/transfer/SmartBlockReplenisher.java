package com.ballsclient.core.module.inventory.transfer;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * SmartBlockReplenisher — Replaces building blocks in active hand before stack reaches zero to prevent placing air.
 */
public class SmartBlockReplenisher extends Module {
    private final BooleanSetting swapBeforeDeplete = new BooleanSetting("SwapBeforeDeplete", "Swap stack when 1 block remains", true);

    public SmartBlockReplenisher() {
        super("smart-block-replenisher", "Smart Block Replenisher", "Replaces blocks in hand before stack reaches zero", Category.INVENTORY);
        addSettings(swapBeforeDeplete);
    }

    public BooleanSetting getSwapBeforeDeplete() {
        return swapBeforeDeplete;
    }

    public static boolean shouldSwap(int activeCount, boolean swapBeforeDeplete) {
        return activeCount <= (swapBeforeDeplete ? 1 : 0);
    }
}
