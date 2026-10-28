package com.ballsclient.core.module.inventory.container;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * CrafterMatrixValidator — Evaluates 1.21+ Crafter 3x3 disabled-slot bitmasks and recipe completion readiness.
 */
public class CrafterMatrixValidator extends Module {
    private final BooleanSetting validateFullPattern = new BooleanSetting("ValidateFullPattern", "Verify all enabled slots are occupied before trigger", true);

    public CrafterMatrixValidator() {
        super("crafter-matrix-validator", "Crafter Matrix Validator", "Evaluates 1.21+ Crafter 3x3 disabled-slot bitmasks", Category.INVENTORY);
        addSettings(validateFullPattern);
    }

    public BooleanSetting getValidateFullPattern() {
        return validateFullPattern;
    }

    public static boolean isSlotEnabled(int disabledSlotsMask, int slotIndex) {
        if (slotIndex < 0 || slotIndex >= 9) return false;
        return (disabledSlotsMask & (1 << slotIndex)) == 0;
    }
}
