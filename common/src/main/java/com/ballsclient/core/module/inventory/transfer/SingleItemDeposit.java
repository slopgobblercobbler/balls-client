package com.ballsclient.core.module.inventory.transfer;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * SingleItemDeposit — Right-click transfer mechanism placing exactly one item per click into target container.
 */
public class SingleItemDeposit extends Module {
    private final BooleanSetting rightClickEmulation = new BooleanSetting("RightClickEmulation", "Emulate single right-click drop into slot", true);

    public SingleItemDeposit() {
        super("single-item-deposit", "Single Item Deposit", "Places exactly one item per click into target container", Category.INVENTORY);
        addSettings(rightClickEmulation);
    }

    public BooleanSetting getRightClickEmulation() {
        return rightClickEmulation;
    }

    public static int getItemsToTransfer() {
        return 1;
    }
}
