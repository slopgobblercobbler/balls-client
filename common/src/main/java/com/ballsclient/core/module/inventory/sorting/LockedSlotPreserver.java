package com.ballsclient.core.module.inventory.sorting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * LockedSlotPreserver — Freezes user-designated slot indices as immovable fixed points during sorting.
 */
public class LockedSlotPreserver extends Module {
    private final BooleanSetting lockHotbar = new BooleanSetting("LockHotbar", "Lock all 9 hotbar slots from sorting", false);
    private final BooleanSetting lockArmor = new BooleanSetting("LockArmor", "Lock armor slots 36-39 from sorting", true);
    private final BooleanSetting lockOffhand = new BooleanSetting("LockOffhand", "Lock offhand slot 40 from sorting", true);

    public LockedSlotPreserver() {
        super("locked-slot-preserver", "Locked Slot Preserver", "Freezes user-designated slot indices as immovable fixed points during sorting", Category.INVENTORY);
        addSettings(lockHotbar, lockArmor, lockOffhand);
    }

    public BooleanSetting getLockHotbar() {
        return lockHotbar;
    }

    public BooleanSetting getLockArmor() {
        return lockArmor;
    }

    public BooleanSetting getLockOffhand() {
        return lockOffhand;
    }

    public static boolean isSlotLocked(int slotIndex, Set<Integer> customLocked, boolean lockHotbar, boolean lockArmor, boolean lockOffhand) {
        if (customLocked != null && customLocked.contains(slotIndex)) {
            return true;
        }
        if (lockHotbar && slotIndex >= 0 && slotIndex <= 8) {
            return true;
        }
        if (lockArmor && slotIndex >= 36 && slotIndex <= 39) {
            return true;
        }
        if (lockOffhand && slotIndex == 40) {
            return true;
        }
        return false;
    }

    public static List<ItemSlot> filterSortableSlots(List<ItemSlot> allSlots, Set<Integer> lockedIndices) {
        List<ItemSlot> sortable = new ArrayList<>();
        if (allSlots == null) return sortable;
        for (int i = 0; i < allSlots.size(); i++) {
            if (lockedIndices == null || !lockedIndices.contains(i)) {
                sortable.add(allSlots.get(i));
            }
        }
        return sortable;
    }
}
