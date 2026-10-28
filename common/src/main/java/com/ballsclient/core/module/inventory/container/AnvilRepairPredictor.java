package com.ballsclient.core.module.inventory.container;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * AnvilRepairPredictor — Precomputes Prior Work Penalty, level costs (2^n - 1), and Too Expensive! limits.
 */
public class AnvilRepairPredictor extends Module {
    private final BooleanSetting warnTooExpensive = new BooleanSetting("WarnTooExpensive", "Emit warning when repair cost reaches 40 levels", true);

    public AnvilRepairPredictor() {
        super("anvil-repair-predictor", "Anvil Repair Predictor", "Precomputes Prior Work Penalty, level costs, and Too Expensive! limits", Category.INVENTORY);
        addSettings(warnTooExpensive);
    }

    public BooleanSetting getWarnTooExpensive() {
        return warnTooExpensive;
    }

    public static int computePriorWorkPenalty(int priorWorkCount) {
        if (priorWorkCount <= 0) return 0;
        if (priorWorkCount >= 31) return Integer.MAX_VALUE;
        return (1 << priorWorkCount) - 1;
    }

    public static boolean isTooExpensive(int totalLevelCost) {
        return totalLevelCost >= 40;
    }
}
