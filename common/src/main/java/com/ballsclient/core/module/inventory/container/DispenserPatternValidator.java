package com.ballsclient.core.module.inventory.container;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.ModeSetting;

import java.util.List;

/**
 * DispenserPatternValidator — Analyzes 3x3 dropper and dispenser grids for valid redstone distribution patterns.
 */
public class DispenserPatternValidator extends Module {
    private final ModeSetting targetPattern = new ModeSetting("TargetPattern", "Required slot distribution pattern", "Symmetrical3x3", "Symmetrical3x3", "CenterOnly", "Checkerboard");

    public DispenserPatternValidator() {
        super("dispenser-pattern-validator", "Dispenser Pattern Validator", "Analyzes 3x3 dropper and dispenser grids for valid redstone patterns", Category.INVENTORY);
        addSettings(targetPattern);
    }

    public ModeSetting getTargetPattern() {
        return targetPattern;
    }

    public static boolean validatePattern(List<ItemSlot> slots3x3, String patternMode) {
        if (slots3x3 == null || slots3x3.size() < 9) return false;
        boolean[] occupied = new boolean[9];
        for (int i = 0; i < 9; i++) {
            ItemSlot s = slots3x3.get(i);
            occupied[i] = (s != null && !s.isEmpty());
        }

        if ("CenterOnly".equalsIgnoreCase(patternMode)) {
            for (int i = 0; i < 9; i++) {
                if (i == 4 && !occupied[i]) return false;
                if (i != 4 && occupied[i]) return false;
            }
            return true;
        } else if ("Checkerboard".equalsIgnoreCase(patternMode)) {
            int[] checker = {0, 2, 4, 6, 8};
            for (int idx : checker) {
                if (!occupied[idx]) return false;
            }
            return true;
        } else {
            // Symmetrical3x3: horizontal reflection symmetry (slot 0==2, 3==5, 6==8)
            return occupied[0] == occupied[2] && occupied[3] == occupied[5] && occupied[6] == occupied[8];
        }
    }
}
