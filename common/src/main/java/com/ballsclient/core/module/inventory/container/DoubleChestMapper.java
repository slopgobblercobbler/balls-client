package com.ballsclient.core.module.inventory.container;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * DoubleChestMapper — Maps single vs double chest geometry, coordinate pairings, and 54-slot linear indexing.
 */
public class DoubleChestMapper extends Module {
    private final BooleanSetting preferPrimaryHalf = new BooleanSetting("PreferPrimaryHalf", "Prioritize depositing into primary half of double chest", true);

    public DoubleChestMapper() {
        super("double-chest-mapper", "Double Chest Mapper", "Maps single vs double chest geometry and indexing", Category.INVENTORY);
        addSettings(preferPrimaryHalf);
    }

    public BooleanSetting getPreferPrimaryHalf() {
        return preferPrimaryHalf;
    }

    public static boolean isDoubleChest(int containerSize) {
        return containerSize == 54;
    }

    public static int mapSlotToHalf(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < 27) {
            return 0; // Top/Primary half
        } else if (slotIndex >= 27 && slotIndex < 54) {
            return 1; // Bottom/Secondary half
        }
        return -1;
    }
}
