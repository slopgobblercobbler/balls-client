package com.ballsclient.core.module.inventory.sorting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * PotionDurationSort — Sorts potions by potency amplifier tier and active duration descending.
 */
public class PotionDurationSort extends Module {
    private final BooleanSetting prioritizeAmplifier = new BooleanSetting("PrioritizeAmplifier", "Sort amplifier level before duration", true);

    public PotionDurationSort() {
        super("potion-duration-sort", "Potion Duration Sort", "Sorts potions by potency amplifier tier and active duration descending", Category.INVENTORY);
        addSettings(prioritizeAmplifier);
    }

    public BooleanSetting getPrioritizeAmplifier() {
        return prioritizeAmplifier;
    }

    public static int comparePotions(int ampA, int durA, int ampB, int durB, boolean priorAmp) {
        if (priorAmp) {
            int cmpAmp = Integer.compare(ampB, ampA); // descending
            if (cmpAmp != 0) return cmpAmp;
            return Integer.compare(durB, durA);
        } else {
            int cmpDur = Integer.compare(durB, durA);
            if (cmpDur != 0) return cmpDur;
            return Integer.compare(ampB, ampA);
        }
    }
}
