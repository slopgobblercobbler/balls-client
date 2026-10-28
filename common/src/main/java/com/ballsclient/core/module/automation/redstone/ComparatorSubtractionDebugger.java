package com.ballsclient.core.module.automation.redstone;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * ComparatorSubtractionDebugger — Visualizes comparator comparison vs subtraction modes.
 */
public class ComparatorSubtractionDebugger extends Module {
    private final BooleanSetting showVectorArrows = new BooleanSetting("ShowVectorArrows", "Renders 3D side input indicator vectors", true);

    public ComparatorSubtractionDebugger() {
        super("comparator-subtraction-debugger", "Comparator Debugger", "Visualizes comparator comparison vs subtraction modes (S = max(0, A - B))", Category.AUTOMATION);
        addSettings(showVectorArrows);
    }

    public BooleanSetting getShowVectorArrows() {
        return showVectorArrows;
    }

    public static int computeOutput(int rearA, int sideB1, int sideB2, boolean isSubtractionMode) {
        int maxSide = Math.max(sideB1, sideB2);
        if (isSubtractionMode) {
            return Math.max(0, rearA - maxSide);
        } else {
            return rearA >= maxSide ? rearA : 0;
        }
    }
}
