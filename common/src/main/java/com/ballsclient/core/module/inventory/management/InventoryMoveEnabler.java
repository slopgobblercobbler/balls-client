package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * InventoryMoveEnabler — Permits player WASD walking and jumping while viewing storage container screens.
 */
public class InventoryMoveEnabler extends Module {
    private final BooleanSetting allowSprint = new BooleanSetting("AllowSprint", "Permit sprinting while in container screen", true);

    public InventoryMoveEnabler() {
        super("inventory-move-enabler", "Inventory Move Enabler", "Permits player movement while viewing containers", Category.INVENTORY);
        addSettings(allowSprint);
    }

    public BooleanSetting getAllowSprint() {
        return allowSprint;
    }
}
