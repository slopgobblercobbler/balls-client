package com.ballsclient.core.module.automation.redstone;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * DropperDispenserCounter — Counts inventory items inside droppers/dispensers and computes comparator signal.
 */
public class DropperDispenserCounter extends Module {
    private final BooleanSetting showComparatorPower = new BooleanSetting("ShowComparatorPower", "Renders output redstone signal level", true);

    public DropperDispenserCounter() {
        super("dropper-dispenser-counter", "Dropper & Dispenser Counter", "Counts inventory items inside droppers/dispensers and computes comparator signal", Category.AUTOMATION);
        addSettings(showComparatorPower);
    }

    public BooleanSetting getShowComparatorPower() {
        return showComparatorPower;
    }

    public static int calculateComparatorOutput(int[] slotCounts, int[] slotMaxStacks) {
        if (slotCounts == null || slotMaxStacks == null || slotCounts.length == 0) {
            return 0;
        }
        int slots = Math.min(slotCounts.length, slotMaxStacks.length);
        double fullness = 0.0;
        int totalItems = 0;

        for (int i = 0; i < slots; i++) {
            totalItems += slotCounts[i];
            int max = slotMaxStacks[i] > 0 ? slotMaxStacks[i] : 64;
            fullness += (double) slotCounts[i] / max;
        }

        if (totalItems == 0) {
            return 0;
        }
        return (int) Math.floor(1.0 + 14.0 * (fullness / (double) slots));
    }
}
