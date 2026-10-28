package com.ballsclient.core.module.combat.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Armor Drop Preventer — Intercepts drop key and container clicks to prevent accidental loss of equipped armor
 */
public class ArmorDropPreventer extends Module {
    private final BooleanSetting lockHotbarDrop = new BooleanSetting("LockHotbarDrop", "Lock Q key for hotbar weapons", true);
    private final BooleanSetting lockArmorSlots = new BooleanSetting("LockArmorSlots", "Block click-drop from armor slots", true);

    public ArmorDropPreventer() {
        super("armor-drop-preventer", "Armor Drop Preventer", "Intercepts drop key and container clicks to prevent accidental loss of equipped armor", Category.COMBAT);
        addSettings(lockHotbarDrop, lockArmorSlots);
    }

    public boolean isLockHotbarDrop() {
        return lockHotbarDrop.getValue();
    }

    public boolean isLockArmorSlots() {
        return lockArmorSlots.getValue();
    }

    public boolean isDropBlocked(int slotIndex, boolean isArmorSlot, boolean lockArmor) {
        return isArmorSlot && lockArmor;
    }
}
