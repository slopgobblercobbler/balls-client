package com.ballsclient.core.module.combat.defense;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Auto Totem — Automatically restocks Totem of Undying to offhand when empty or popped
 */
public class AutoTotem extends Module {
    private final NumberSetting swapDelayTicks = new NumberSetting("SwapDelayTicks", "Delay in ticks before swapping totem", 0.0, 0.0, 5.0, 1.0);
    private final BooleanSetting allowInventory = new BooleanSetting("AllowInventory", "Search full inventory if hotbar has no totems", true);
    private final BooleanSetting preserveHealth = new BooleanSetting("PreserveHealth", "Prioritize offhand gapple if HP is full", false);

    public AutoTotem() {
        super("auto-totem", "Auto Totem", "Automatically restocks Totem of Undying to offhand when empty or popped", Category.COMBAT);
        addSettings(swapDelayTicks, allowInventory, preserveHealth);
    }

    public double getSwapDelayTicks() {
        return swapDelayTicks.getValue();
    }

    public boolean isAllowInventory() {
        return allowInventory.getValue();
    }

    public boolean isPreserveHealth() {
        return preserveHealth.getValue();
    }

    public int findBestTotemSlot(int[] hotbarItems, int[] invItems, int totemId) {
        for (int i = 0; i < hotbarItems.length; i++) {
            if (hotbarItems[i] == totemId) return i;
        }
        for (int i = 0; i < invItems.length; i++) {
            if (invItems[i] == totemId) return i + 9;
        }
        return -1;
    }
}
