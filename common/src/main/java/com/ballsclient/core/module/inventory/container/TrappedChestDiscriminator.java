package com.ballsclient.core.module.inventory.container;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * TrappedChestDiscriminator — Discriminates trapped chests from standard chests to prevent redstone trigger traps.
 */
public class TrappedChestDiscriminator extends Module {
    private final BooleanSetting alertOnTrapped = new BooleanSetting("AlertOnTrapped", "Display danger alert when looking at trapped chests", true);

    public TrappedChestDiscriminator() {
        super("trapped-chest-discriminator", "Trapped Chest Discriminator", "Discriminates trapped chests from standard chests", Category.INVENTORY);
        addSettings(alertOnTrapped);
    }

    public BooleanSetting getAlertOnTrapped() {
        return alertOnTrapped;
    }

    public static boolean isTrappedChest(String blockId) {
        return blockId != null && blockId.toLowerCase().contains("trapped_chest");
    }
}
