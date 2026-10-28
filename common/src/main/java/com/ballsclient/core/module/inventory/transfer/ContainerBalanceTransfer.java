package com.ballsclient.core.module.inventory.transfer;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ContainerBalanceTransfer — Equalizes matching item counts between player inventory and opened storage container.
 */
public class ContainerBalanceTransfer extends Module {
    private final NumberSetting targetRatio = new NumberSetting("TargetRatio", "Player target fraction of total combined item count", 0.5, 0.1, 0.9, 0.1);

    public ContainerBalanceTransfer() {
        super("container-balance-transfer", "Container Balance Transfer", "Equalizes matching item counts between player and storage", Category.INVENTORY);
        addSettings(targetRatio);
    }

    public NumberSetting getTargetRatio() {
        return targetRatio;
    }

    public static int calculateTransferDelta(int countPlayer, int countContainer, double targetRatio) {
        int total = countPlayer + countContainer;
        int targetPlayer = (int) Math.round(total * targetRatio);
        return targetPlayer - countPlayer; // > 0 means transfer from container to player; < 0 means player to container
    }
}
