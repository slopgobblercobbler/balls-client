package com.ballsclient.core.module.automation.redstone;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * RepeaterTickCalculator — Displays delay ticks and cumulative delay along repeater chains.
 */
public class RepeaterTickCalculator extends Module {
    private final BooleanSetting showCumulativeDelay = new BooleanSetting("ShowCumulativeDelay", "Aggregates total delay ticks across connected repeaters", true);
    private final ModeSetting unit = new ModeSetting("Unit", "Time unit display format", "RedstoneTicks", "RedstoneTicks", "GameTicks", "Milliseconds");

    public RepeaterTickCalculator() {
        super("repeater-tick-calculator", "Repeater Tick Calculator", "Displays delay ticks (1-4 redstone ticks) and cumulative delay along repeater chains", Category.AUTOMATION);
        addSettings(showCumulativeDelay, unit);
    }

    public BooleanSetting getShowCumulativeDelay() {
        return showCumulativeDelay;
    }

    public ModeSetting getUnit() {
        return unit;
    }

    public double convertDelay(int redstoneTicks) {
        String u = unit.getValue();
        if ("GameTicks".equalsIgnoreCase(u)) {
            return redstoneTicks * 2.0;
        } else if ("Milliseconds".equalsIgnoreCase(u)) {
            return redstoneTicks * 100.0;
        }
        return redstoneTicks;
    }
}
