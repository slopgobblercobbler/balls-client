package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * RepairCostCalculator — Computes cumulative anvil work penalties and warns when repair cost approaches 40.
 */
public class RepairCostCalculator extends Module {
    private final BooleanSetting showRemainingRepairs = new BooleanSetting("ShowRemainingRepairs", "Display estimated repair uses before Too Expensive limit", true);

    public RepairCostCalculator() {
        super("repair-cost-calculator", "Repair Cost Calculator", "Computes cumulative anvil work penalties", Category.INVENTORY);
        addSettings(showRemainingRepairs);
    }

    public BooleanSetting getShowRemainingRepairs() {
        return showRemainingRepairs;
    }

    public static int calculatePriorWorkPenalty(int priorWork) {
        if (priorWork <= 0) return 0;
        if (priorWork >= 31) return Integer.MAX_VALUE;
        return (1 << priorWork) - 1;
    }

    public static int calculateRemainingRepairs(int currentPriorWork, int baseCost) {
        int work = currentPriorWork;
        int repairs = 0;
        while (work < 31) {
            int penalty = calculatePriorWorkPenalty(work);
            if (penalty + baseCost >= 40) {
                break;
            }
            repairs++;
            work++;
        }
        return repairs;
    }
}
