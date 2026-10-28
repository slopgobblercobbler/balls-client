package com.ballsclient.core.module.combat.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Offhand Totem Cycle — Automated queue cycling ensuring offhand is never left without a Totem of Undying
 */
public class OffhandTotemCycle extends Module {
    private final NumberSetting maxConsecutiveSwaps = new NumberSetting("MaxConsecutiveSwaps", "Maximum totem swaps allowed per combat engagement", 6.0, 1.0, 10.0, 1.0);
    private final BooleanSetting alertOnLastTotem = new BooleanSetting("AlertOnLastTotem", "Play warning tone when only 1 totem remains", true);

    public OffhandTotemCycle() {
        super("offhand-totem-cycle", "Offhand Totem Cycle", "Automated queue cycling ensuring offhand is never left without a Totem of Undying", Category.COMBAT);
        addSettings(maxConsecutiveSwaps, alertOnLastTotem);
    }

    public double getMaxConsecutiveSwaps() {
        return maxConsecutiveSwaps.getValue();
    }

    public boolean isAlertOnLastTotem() {
        return alertOnLastTotem.getValue();
    }

    public boolean isLastTotemRemaining(int totalInventoryTotems) {
        return totalInventoryTotems == 1;
    }
}
