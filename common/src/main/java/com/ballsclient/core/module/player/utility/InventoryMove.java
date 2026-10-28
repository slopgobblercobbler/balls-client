package com.ballsclient.core.module.player.utility;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * InventoryMove — Unlocks player locomotion (WASD, sprint, jump) while navigating container screens.
 */
public class InventoryMove extends Module {
    private final BooleanSetting allowJump = new BooleanSetting("AllowJump", "Allow spacebar jumping while screen open", true);
    private final BooleanSetting allowSprint = new BooleanSetting("AllowSprint", "Maintain sprint state while screen open", true);
    private final BooleanSetting allowSneak = new BooleanSetting("AllowSneak", "Allow crouching while inventory is active", false);

    public InventoryMove() {
        super("inventory-move", "Inventory Move", "Unlocks player locomotion (WASD, sprint, jump) while navigating container screens", Category.PLAYER);
        addSettings(allowJump, allowSprint, allowSneak);
    }

    public BooleanSetting getAllowJump() {
        return allowJump;
    }

    public BooleanSetting getAllowSprint() {
        return allowSprint;
    }

    public BooleanSetting getAllowSneak() {
        return allowSneak;
    }

    public static boolean canMoveInContainer(boolean allowJump, boolean allowSprint, boolean allowSneak) {
        return allowJump || allowSprint || allowSneak;
    }
}
