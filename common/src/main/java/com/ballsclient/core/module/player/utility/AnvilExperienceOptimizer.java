package com.ballsclient.core.module.player.utility;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * AnvilExperienceOptimizer — Computes optimal Prior Work Penalty combination order to prevent "Too Expensive!".
 */
public class AnvilExperienceOptimizer extends Module {
    private final BooleanSetting findLowestCost = new BooleanSetting("FindLowestCost", "Compute permutation with minimal total XP level expenditure", true);
    private final BooleanSetting warnTooExpensive = new BooleanSetting("WarnTooExpensive", "Flag enchantments that exceed vanilla 39-level anvil limit", true);

    public AnvilExperienceOptimizer() {
        super("anvil-experience-optimizer", "Anvil XP Optimizer", "Computes optimal Prior Work Penalty combination order to prevent Too Expensive!", Category.PLAYER);
        addSettings(findLowestCost, warnTooExpensive);
    }

    public BooleanSetting getFindLowestCost() {
        return findLowestCost;
    }

    public BooleanSetting getWarnTooExpensive() {
        return warnTooExpensive;
    }

    public static int getPriorWorkPenalty(int priorWorkCount) {
        if (priorWorkCount <= 0) return 0;
        return (1 << priorWorkCount) - 1;
    }

    public static int getCombinedCost(int base, int penaltyA, int penaltyB) {
        return base + penaltyA + penaltyB;
    }
}
